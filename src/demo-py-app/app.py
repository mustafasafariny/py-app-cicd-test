# app.py

from flask import Flask

app = Flask(__name__)

#Flask uses @app.route to connect URL endpoints with code contained in functions.
#The argument @app.route defines the URL’s path component, which is the root path ("/") in this case.
#function def index() defines what should be executed if the defined URL endpoint is requested by a user and when the  page is loaded

#In other words, if a user types the base URL of your web app into their browser, then Flask runs index() 
# and the user sees the returned text.
@app.route("/")
def index():
    return "Python Application Deployment to Host works!"

#It tells the application to run and listen for incoming requests on the specified host (127.0.0.1, Flask’s development server) and port (5000). 
if __name__ == "__main__":
    #app.run(debug=True,host='0.0.0.0')
    app.run(host="127.0.0.1", port=5000, debug=True)

# To test on Flask server (using PowerSell Terminal)
# Within the created virtual env (.venv) Navigate to src lib aapplication folder and $ flask --app main.py run