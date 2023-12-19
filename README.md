Initial jenkins deployment references tasks from your application repo
​
1. An application to deploy (Python) website (https://flask.palletsprojects.com/en/3.0.x/quickstart/#a-minimal-application)
2. Jenkins build pipeline
   - Build and save binary artifact
   - Download binary artifact and test
   - Deploy to dev
     - Deploy dev infrastructure stack (Should be independent of application deployment)
     - Deploy binary artifact to dev stack
       -- Environment=Dev
       -- ConnectionString="https://dev-db"
       -- BuildNumber=123
   - Run end to end tests on dev
   - Deploy to production
     - Deploy production infrastructure stack (Should be independent of application deployment)
     - Deploy binary artifact to production stack
       -- Environment=Prod
       -- ConnectionString="https://prod-db"
       -- BuildNumber=123
3. Jenkins needs to pull code from github application repo on branch change
4. We need to tag binaries with build numbers and build number should be a tag in the application stack infra and perhaps names
5. We need separate config per environment
6. Deploy only the develop branch
7. Jenkins should reference the build pipeline in your application repo (This might be hard may need to chat about this)
​
master -> develop -> feature-add-metrics (Pull Request)
​
The application
​
Will be a website, it will need a root endpoint that returns "I am in <dev|prod> and it works!"
Add metrics endpoint using (https://pypi.org/project/prometheus-flask-exporter/).
Website need to have something configured by environment variables.
Add logging to the website and export logs to Cloudwatch
Make it public for now.
​
Structure (Application code repo github)
​
pipelines/
pipeline.groovy
deployment/
infrastructure/
... CDK code for infra
application/
... CDK code for application infra
src/
.... python code for website
build.sh
test.sh
deploy.sh
