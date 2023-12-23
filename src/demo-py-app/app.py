# app.py

from flask import Flask

#By passing __name__ as an argument to the Flask constructor,
#you are telling Flask to set up the application with the appropriate configurations based on the location of the script.
# This is important for Flask to know where to look for templates, static files, and other resources associated with the application.

#Putting it all together, the line app = Flask(__name__) creates a Flask application object named app based on the script's name.
#This object is then used to define routes, handle HTTP requests, and configure various aspects of the web application.
# In a Flask application, you would typically go on to define routes, add functionality, and finally, run the application using the app.run() method
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