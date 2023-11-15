pipeline {
    agent any

    stages {
        stage('Build') {
            
            //when {
            //    expression {
            //        env.BRANCH_NAME == 'main'
            //        // && CODE_CHANGES == true
            //    }
            //}
            steps {
                sh 'printenv'

                echo "Building....."
                echo "Build Number: ${env.BUILD_NUMBER}"
                echo "Build URL: ${env.BUILD_URL}"
                 
            //    def currentBranch = env.GIT_BRANCH

                // Set environment variable based on branch name
            //    def environmentName = currentBranch == 'main' ? 'Prod' : 'Dev'

                echo "I am in ${env.GIT_BRANCH} and it works!"

                script {
                    sh './scripts/build.sh'
                }      
            }
        }

        stage('Test') {
            steps {
                echo 'Testing....'
                // Run tests for Python app
                script {
                    sh './scripts/test.sh'
                }   

            }
        }

        stage('Deploy') {
            when {
              expression {
                   currentBuild.result == null || currentBuild.result == 'SUCCESS' 
              }
            }

            steps {
                echo 'Deploying...'
            // Deploy your Python app (e.g., to a server or a cloud platform)
                script {
                    sh './scripts/deploy.sh'
                }   
            }
        }
    }

    post {
            always {
                echo 'Send email...'
                // Add deployment steps here
                //script {
                //    sh 'source ${VENV}/deactivate'
                //}
            }
            success {
                echo 'Build successful! Deploying...'                           
                archiveArtifacts artifacts: 'artifacts/*.tar.gz, artifacts/*.whl', fingerprint: true
               }
            failure {
                echo 'Build failed! Not deploying...'
                // Add failure handling steps here
            }
        }  
}
