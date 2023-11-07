pipeline {
    agent any

    stages {
        stage('Build') {
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

                    echo "Running in ${env.ENVIRONMENT} environment"
                    echo "Database connection: ${env.DB_CONNECTION_STRING}"
                  
                    // Build or generate binary artifacts (e.g., compiled binaries)
                    sh 'make'  // Replace with your build commands

                    // Move the binary artifacts to a specific directory
                    sh 'mkdir -p binary_artifacts'
                    sh 'mv binary/* binary_artifacts/'
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

