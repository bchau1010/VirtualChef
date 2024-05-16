from flask import Flask
from firebase_admin import credentials, initialize_app

cred = credentials.Certificate("API\\DBkey.json")
default_app = initialize_app(cred)


#create the app with firebase with key
#Also register user
# /user connected to local host to make HTTPs request
def create_app():
    app = Flask(__name__)
    #app.config['SECRET_KEY'] = 'WHATEVER YOU WANT'
    
    #get the user 
    from.userAPI import userAPI
    app.register_blueprint(userAPI,url_prefix='/user')
    
    return app