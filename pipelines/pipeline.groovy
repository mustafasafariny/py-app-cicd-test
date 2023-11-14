//CODE_CHANGES = getGitChanges()
pipeline {
    agent any
    environment {
   
    }
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
                 
                def currentBranch = env.GIT_BRANCH

                // Set environment variable based on branch name
                def environmentName = '';
                if (currentBranch == 'main')  {
                    environmentName = 'Prod'
                } 
                else {
                    environmentName = 'Dev'
                }
   
                echo "I am in ${environmentName} and it works!"

                script {
                    sh 'chmod +x ./scripts/build.sh'
                    sh './scripts/build.sh'
                }   
            }
        }

        stage('Test') {
            steps {
                echo 'Testing....'
                // Run tests for Python app
                script {
                    sh 'chmod +x ./scripts/test.sh'
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
                script {
                    sh 'deactivate'
                }
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
