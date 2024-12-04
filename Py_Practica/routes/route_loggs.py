from flask import Blueprint, request, render_template, redirect, url_for, flash
import requests

route_loggs = Blueprint('route_loggs', __name__)

BASE_URL = "http://localhost:8088/myapp/evento"

@route_loggs.route('/', methods=['GET'])
def home():
    try:
        r = requests.get(BASE_URL + "/list")
        r.raise_for_status()
        data = r.json()
        return render_template('historial.html', lista=data["data"])
    except requests.exceptions.RequestException as e:
        flash(f"Error al obtener los eventos: {str(e)}", "error")
        return render_template('historial.html', lista=[])
    
@route_loggs.route('/last/<int:registros>', methods=['GET'])
def obtener_ultimos(registros):
    try:
        r = requests.get(f"{BASE_URL}/list/last/{registros}")
        r.raise_for_status()
        data = r.json()
        return render_template('last.html', lista=data["data"])
    except requests.exceptions.RequestException as e:
        flash(f"Error al obtener los eventos: {str(e)}", "error")
        return render_template('historial.html', lista=[])