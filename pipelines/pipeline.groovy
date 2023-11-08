pipeline {
    agent any
    // Create a Virtual Env
    sh 'python -m venv venv'
    sh 'source venv/bin/activate'
    // Install the Python dependencies
    sh 'pip install -r requirements.txt'
   // environment {
   //    DISABLE_AUTH = 'true'
    stages {
        
        stage('Build') {
           // environment {
           //        ENABLE_AUTH = ‘false’
            steps {
                script {
                    if (env.BRANCH_NAME == 'main') {
                        // Configure production environment
                        env.ENVIRONMENT = 'PROD'
                        env.DB_CONNECTION_STRING = 'production-db.example.com'
                    } else {
                        // Configure development environment
                        env.ENVIRONMENT = 'DEV'
                        env.DB_CONNECTION_STRING = 'development-db.example.com'
                    }
                    echo "The build number is ${env.BUILD_NUMBER}"
                    echo "Running in ${env.ENVIRONMENT} environment"
                    // echo "Database connection: ${env.DB_CONNECTION_STRING}"
                    // echo env.DISABLE_AUTH
                                       
                    // Build or generate binary artifacts (e.g., compiled binaries)
                    from setuptools import setup, find_packages
                    setup(
                        name="pyApp",
                        version="1.0.0",
                        packages=find_packages(),
                        install_requires=[
                        # List your project dependencies here
                        ],
                        )
                    
                    // create a source distribution
                    python setup.py sdist
                    // create a binary wheel distribution
                    python setup.py bdist_wheel
                    
                    // Move the binary artifacts to a specific directory
                    sh 'mkdir -p binary_artifacts'
                    sh 'mv dist/* binary_artifacts/'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    // Use the environment-specific configuration for testing
                    sh 'run_tests.sh'
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'main') {
                        // Deployment to production
                        echo "Deploying to PROD environment"
                        // Add deployment steps for production
                    } else {
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
    }
}

