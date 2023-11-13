pipeline {
    agent any

    stages {
        stage('Build') {
            //when {
            //    expression {
            //        env.BRANCH_NAME == 'main'
            //    }
            //}
            steps {
                echo "Building....."

                // Print the build number and build URL
                echo "Build Number: ${env.BUILD_NUMBER}"
                echo "Build URL: ${env.BUILD_URL}"

                script {
                    sh 'chmod +x ./scripts/build.sh'
                    sh './scripts/build.sh'
                }   
            }
        }

        stage('Test') {
            steps {
                //when {
                //    expression {
                //        env.BRANCH_NAME == 'main'
                //    }
                //}
                echo 'Testing....'
                // Run tests for Python app
                script {
                    sh 'chmod +x ./scripts/test.sh'
                    sh './scripts/test.sh'
                }   

            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying...'
            // Deploy your Python app (e.g., to a server or a cloud platform)
                script {
                    sh 'chmod +x ./scripts/deploy.sh'
                    sh './scripts/deploy.sh'
                }   
            }
        }
    }

    post {
            always {
                echo 'Send email...'
                // Add deployment steps here
            }
            success {
                echo 'Build successful! Deploying...'
                // Add deployment steps here
            }
            failure {
                echo 'Build failed! Not deploying...'
                // Add failure handling steps here
            }
        }  
}
