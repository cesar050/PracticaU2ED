from flask import Blueprint, request, render_template, redirect, url_for, flash
import requests

route_familia = Blueprint('route_familia', __name__)

BASE_URL = "http://localhost:8088/myapp/familia"


@route_familia.route('/', methods=['GET'])
def home():
    try:
        r = requests.get(BASE_URL + "/list")  
        r.raise_for_status()  
        data = r.json()  
        return render_template('/familias/familia.html', lista=data["data"])  
    except requests.exceptions.RequestException as e:
        flash(f"Error al obtener las familias: {str(e)}", "error")
        return render_template('familia.html', lista=[])
    
@route_familia.route('/agregar', methods=['GET'])
def agregar_familia():
    return render_template('/familias/agregar_familia.html')



@route_familia.route('/save', methods=['POST']) 
def save_familia():
    try:
        
        presupuesto = request.form.get("presupuesto")
        costo_generador = request.form.get("costoGenerador")
        tiene_generador = request.form.get("tieneGenerador") == "true"

       
        if tiene_generador:
            if not presupuesto or float(presupuesto) <= 0:
                flash("El presupuesto debe ser mayor a cero.", "error")
                return redirect(url_for('route_familia.agregar_familia'))
            if not costo_generador or float(costo_generador) <= 0:
                flash("El costo del generador debe ser válido.", "error")
                return redirect(url_for('route_familia.agregar_familia'))
            if float(presupuesto) < float(costo_generador):
                flash("El presupuesto no es suficiente para adquirir el generador. Debe ser mayor al costo del generador.", "error")
                return redirect(url_for('route_familia.agregar_familia'))

        data = {
            "nombre": request.form.get("nombre"),
            "presupuesto": presupuesto,
            "costoGenerador": costo_generador,
            "tieneGenerador": tiene_generador,
            "consumoPorHora": request.form.get("consumoPorHora"),
            "energiaGenerada": request.form.get("energiaGenerada"),
            "usoGenerador": request.form.get("usoGenerador")
        }
        r = requests.post(f"{BASE_URL}/save", json=data)  
        r.raise_for_status()  
        flash("Familia agregada exitosamente.", "success")  
        return redirect(url_for('route_familia.home'))  
    except requests.exceptions.RequestException as e:
        flash(f"Error al agregar la familia: {str(e)}", "error")
        return redirect(url_for('route_familia.agregar_familia'))
    


@route_familia.route('/<int:id>/modificar', methods=['GET'])
def get_familia_by_id(id):
    try:
        r = requests.get(f"{BASE_URL}/{id}") 
        r.raise_for_status()
        data = r.json()
        return render_template('/familias/modificar_famila.html', familia=data["data"])  
    except requests.exceptions.RequestException as e:
        flash(f"Error al obtener la familia: {str(e)}", "error")
        return redirect(url_for('route_familia.home'))

@route_familia.route('/<int:id>/update', methods=['POST'])
def update_familia(id):
    if request.form.get("_method") == "PUT":
        try:
            data = {
                "nombre": request.form.get("nombre"), 
                "presupuesto": request.form.get("presupuesto")
            }
            r = requests.put(f"{BASE_URL}/{id}/update", json=data)

            r.raise_for_status()  

            flash("Familia actualizada exitosamente.", "success")
            return redirect(url_for('route_familia.home'))  

        except requests.exceptions.RequestException as e:
            print(f"[ERROR] Falló el PUT: {str(e)}")
            flash(f"Error al actualizar la familia: {str(e)}", "error")
            return redirect(url_for('route_familia.home'))  
    else:
        flash("Método no permitido", "error")
        return redirect(url_for('route_familia.home'))
    
@route_familia.route('/<id>/eliminar', methods=['POST'])
def eliminar_familia(id):
    try:
        r = requests.delete(f"{BASE_URL}/{id}/delete") 
        r.raise_for_status()
        data = r.json()
        return redirect(url_for('route_familia.home')) 
    except requests.exceptions.RequestException as e:
        flash(f"Error al obtener la familia: {str(e)}", "error")
        return redirect(url_for('route_familia.home'))
    
@route_familia.route('<id>', methods=['GET'])
def get_familia(id):
    try:
        r_familia = requests.get(f"{BASE_URL}/{id}")
        r_familia.raise_for_status()
        familia_data = r_familia.json()['data']

        r_disponibles = requests.get(f"{BASE_URL}/{id}/disponible")
        r_disponibles.raise_for_status()
        disponibles_data = r_disponibles.json()['data']

        return render_template(
            "/familias/familia_id.html",
            familia=familia_data,
            disponibles=disponibles_data
        )
    except requests.exceptions.RequestException as e:
        flash(f"Error al obtener los datos: {str(e)}", "error")
        return redirect(url_for('route_familia.home'))
    
@route_familia.route('tiene_generador', methods=['GET'])
def familias_con_generador():
    try:
        r = requests.get(f"{BASE_URL}/list/tiene_generador")
        r.raise_for_status() 
        data = r.json()  
        return render_template('/familias/familia.html', lista=data["data"])
    except requests.exceptions.RequestException as e:
        flash(f"Error al obtener las familias: {str(e)}", "error")
        return render_template('/familias/familia.html', lista=[])
    

@route_familia.route('list/ordernar/<attribute>/<type>/<metodo>', methods=['GET'])
def metodosOrdenacion(attribute, type, metodo):
    try: 
        r = requests.get(f"{BASE_URL}/list/order/{attribute}/{type}/{metodo}");
        r.raise_for_status()
        data = r.json()  
        return render_template('/familias/familia.html', lista=data["data"])
    except requests.exceptions.RequestException as e:
        flash(f"Error al obtener las familias: {str(e)}", "error")
        return render_template('/familias/familia.html', lista=[])
    
@route_familia.route('/list/search/<attribute>/<value>', methods=['GET'])
def search_criteria(attribute, value):
    try:
        r = requests.get(f'{BASE_URL}/list/search/{attribute}/{value}')
        r.raise_for_status()
        response = r.json()  
        
        if response.get("status") == "ERROR":
            print(f"Error desde la API: {response.get('msg', 'Error desconocido')}")
            return render_template('/familias/familia.html', lista=[], error=response.get('msg'))
        
        data = response.get("data", [])
        
        if not isinstance(data, list):
            data = [data]
        
        return render_template('/familias/familia.html', lista=data,)
    
    except requests.RequestException as e:
        print(f"Error al conectar con la API: {e}")
        return render_template('/familias/familia.html', lista=[], error="Error")
    