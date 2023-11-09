pipeline {
    agent any

    }
    stages {
        stage('Setup') {
            steps {
                  // Create a Virtual Env
                  sh 'python -m venv venv'
                  sh 'source venv/bin/activate'
    
                 // Install the Python dependencies
                  sh 'pip install -r requirements.txt'
                
                environment {
                    AWS_ACCESS_KEY_ID     = credentials('jenkins-aws-secret-key-id')
                    AWS_SECRET_ACCESS_KEY = credentials('jenkins-aws-secret-access-key')
                }
             }
        }    
        stage('Checkout') {
            // get python app code from Git Repo
            steps {
                script {
                   def gitRepoURL = 'git@github.com:mustafasafariny/py-app-cicd-test.git'
                   def gitCredentialsId = 'ubuntu (cdk test new)'  

                   checkout([$class: 'GitSCM', 
                             branches: [[name: '*/main']],  
                             userRemoteConfigs: [[url: gitRepoURL, credentialsId: gitCredentialsId]]])
                    }
                }
         }

         stage('Build') {
            if (env.BRANCH_NAME == 'main') {
                // Configure production environment
                env.ENVIRONMENT = 'PROD'
                }
            else {
                // Configure development environment
                 env.ENVIRONMENT = 'DEV'
                 }
                
            when {
                expression {
                   if env.ENVIRONMENT = 'DEV' || CODE_CHANGES == true
               }
            }
            steps {

                // Print Jenkins Variables on Console
                    echo "The build number is ${env.BUILD_NUMBER}"    // or echo "The build Id is ${env.BUILD_ID}"                
                    echo "The build URL is ${env.BUILD_URL}"
                    echo "Running in ${env.ENVIRONMENT} environment"

                // Build or generate binary artifacts (e.g., compiled binaries)

                }
            }
        }

        stage('Test') {
            when {
               expression {
                   if env.ENVIRONMENT = 'DEV' || BRANCH_NAME == 'main'
               }
            }
            steps {
                script {
                    // Use the environment-specific configuration for testing
                    // sh 'run_tests.sh'
                    sh 'python manage.py test'
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    pip install setuptools
                    from setuptools import setup, find_packages
                    
                    setup(
                        name="pyApp",
                        version="1.0.0",
                        description="A sample Python cicd app",
                        author="Mustafa",
                        author_email="mustafa.mustafa@domain.com.au",
                        url="https://github.com/mustafasafariny/py-app-cicd-test/blob/main/src/PyApp",
                        packages=find_packages(),
                        install_requires=["dependency1", "dependency2"],
                )
                // create a source distribution
                    python setup.py sdist bdist_wheel
                    
                    // Move the binary artifacts to a specific directory
                    sh 'mkdir -p binary_artifacts'
                    sh 'mv dist/* binary_artifacts/'
            }
        }

        stage('Deploy') {
            // For example, you might copy your application to a server using SSH or other deployment methods
            // copying the application files to a production server
            // setting up a virtual environment
            // Running the Python application
            
            steps {
                script {
                    if (env.BRANCH_NAME == 'main') {
                        // Deployment to production
                        echo "Deploying to PROD environment"
                        // Add deployment steps for production
                    } 
                    else {
                        // Deployment to development or other non-production environments
                        echo "Deploying to DEV environment"
                        // Add deployment steps for development
                    }
                }
            }
        }
    }

    post {
        always {
            // Archive build artifacts or perform other post-build tasks
            archiveArtifacts artifacts: 'build/*', allowEmptyArchive: true
        }
        success {
            
        }
        failure {
            
        }
    }
}

