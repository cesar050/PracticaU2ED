from flask import Flask, render_template

def create_app():
    app = Flask(__name__, instance_relative_config=False)

    # Establecer la clave secreta
    app.secret_key = 'tu_clave_secreta_aqui'  # Cambia esto a una clave secreta y única

    @app.route('/')
    def inicio():
        return render_template('inicio.html')

    with app.app_context():
        from routes.route_familia import route_familia
        from routes.route_generador import route_generador  # Importa el nuevo blueprint
        from routes.route_loggs import route_loggs
        app.register_blueprint(route_familia, url_prefix='/familia')

        app.register_blueprint(route_generador, url_prefix='/generador')
        app.register_blueprint(route_loggs, url_prefix='/loggs')
    return app