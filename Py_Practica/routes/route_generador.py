from flask import Blueprint, request, render_template, redirect, url_for, flash
import requests

route_generador = Blueprint('route_generador', __name__)

BASE_URL = "http://localhost:8088/myapp/generador"


@route_generador.route('/')
def home():
    try:
        r = requests.get(f"{BASE_URL}/list")
        r.raise_for_status()
        data = r.json()
        return render_template('/generadores/generador.html', lista=data["data"])
    except requests.exceptions.RequestException as e:
        return f"Error al obtener la lista de generadores: {str(e)}", 500


@route_generador.route('/agregar')
def agregar_generador():
    return render_template('/generadores/agregar_generador.html')


@route_generador.route('/save', methods=['POST'])
def save_generador():
    try:
        form_data = request.form.to_dict()
        r = requests.post(f"{BASE_URL}/save", json=form_data)
        r.raise_for_status()
        return redirect(url_for('route_generador.home'))
    except requests.exceptions.RequestException as e:
        flash(f"Error al guardar el generador: {str(e)}", "error")
        return redirect(url_for('route_generador.agregar_generador'))


@route_generador.route('/<int:id>/modificar')
def get_generador_by_id(id):
    try:
        r = requests.get(f"{BASE_URL}/{id}")
        r.raise_for_status()
        data = r.json()
        return render_template('/generadores/modificar_generador.html', generador=data["data"])
    except requests.exceptions.RequestException as e:
        return f"Error al obtener el generador: {str(e)}", 500


@route_generador.route('/<int:id>/update', methods=['POST'])
def update_generador(id):
    if request.form.get("_method") == "PUT":
        try:
            data = {
                "nombre": requests.form.get("nombre"),
                "precio": request.form.get("precio"),
                "consumoPorHora": request.form.get("consumoPorHora"),
                "energiaGenerada": request.form.get("energiaGenerada"),
                "uso": request.form.get("uso")
            }
            r = requests.put(f"{BASE_URL}/{id}/update", json=data)
            r.raise_for_status()
            return redirect(url_for('route_generador.home'))
        except requests.exceptions.RequestException as e:
            print(f"[ERROR] Falló el PUT: {str(e)}")
            flash(f"Error al actualizar el generador: {str(e)}", "error")
            return redirect(url_for('route_generador.home'))
    else:
        flash("Método no permitido", "error")
        return redirect(url_for('route_generador.home'))
    
    

@route_generador.route('/<id>/eliminar', methods=['POST'])
def eliminar_generador(id):
    try:
        r = requests.delete(f"{BASE_URL}/{id}/delete")
        r.raise_for_status()
        data = r.json()
        return redirect(url_for('route_generador.home'))
    except requests.exceptions.RequestException as e:
        return f"Error al obtener el generador: {str(e)}", 500


@route_generador.route('list/ordenar/<attribute>/<type>/<metodo>', methods=['GET'])
def metodosOrdenacion(attribute, type, metodo):
    try: 
        r = requests.get(f"{BASE_URL}/list/order/{attribute}/{type}/{metodo}");
        r.raise_for_status()
        data = r.json()  
        return render_template('/generadores/generador.html', lista=data["data"])
    except requests.exceptions.RequestException as e:
        flash(f"Error al obtener las familias: {str(e)}", "error")
        return render_template('/generadores/generador.html', lista=[])

@route_generador.route('/list/search/<attribute>/<value>', methods=['GET'])
def search_criteria(attribute, value):
    try:
        r = requests.get(f'{BASE_URL}/list/search/{attribute}/{value}')
        r.raise_for_status()
        response = r.json()  
        
        if response.get("status") == "ERROR":
            print(f"Error desde la API: {response.get('msg', 'Error desconocido')}")
            return render_template('/generadores/generador.html', lista=[], error=response.get('msg'))
        
        data = response.get("data", [])
        
        if not isinstance(data, list):
            data = [data]
        
        return render_template('/generadores/generador.html', lista=data,)
    
    except requests.RequestException as e:
        print(f"Error al conectar con la API: {e}")
        return render_template('/generadores/generador.html', lista=[], error="Error")
    