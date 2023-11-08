pipeline {
    agent any
    sh 'python -m venv myenv'
    sh 'source myenv/bin/activate'
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
                    sh 'python /src/pyApp.py sdist bdist_wheel'

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

