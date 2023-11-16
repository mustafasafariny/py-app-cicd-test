# app.py
from flask import Flask

app = Flask(__name__)

#Flask uses @app.route to connect URL endpoints with code contained in functions.
#The argument @app.route defines the URL’s path component, which is the root path ("/") in this case.
#function def index() defines what should be executed if the defined URL endpoint is requested by a user and when the  page is loaded

#In other words, if a user types the base URL of your web app into their browser, then Flask runs index() 
# and the user sees the returned text.

##branch = env.GIT_BRANCH
##@app.route("/<branch>")
##def index():
## return "I am in ${env.GIT_BRANCH} and it works!"

@app.route("/")
def index():
    return "Python Application Deployment to Host works!"

#Tell Python to start Flask’s development server when the script is executed from the command line. 
#It’ll be used only when you run the script locally (127.0.0.1 is the localhost for local testing)
if __name__ == "__main__":
    app.run(host="127.0.0.1", port=8080, debug=True)

# To test on Flask server (using PowerSell Terminal)
# Within the created virtual env (.venv) Navigate to src lib aapplication folder and $ flask --app main.py run