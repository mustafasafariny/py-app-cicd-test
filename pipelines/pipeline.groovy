pipeline {
    agent any

    parameters{
        string(name: 'VERSION', defaultValue: '', description: 'version to deploy')
        choice(name: 'VERSION', choices: ['1.1.0', '1.2.0', '1.3.0'], description: '')
        booleanParam(name: 'executeTests', defaultValue: true, description: '')
    }
    //tools{
    //    maven 'Maven'
    //}
    environment {
        DISABLE_AUTH = 'true'
        NEW_VERSION = '1.0.0'
        SERVER_CREEDENTIALS = credentials('server-credentials')
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
                echo "Building version ${NEW_VERSION}"
                echo "Build Number: ${env.BUILD_NUMBER}"
                echo "Build URL: ${env.BUILD_URL}"
                 
                // Set environment variable based on branch name
   
                echo "I am in ${env.GIT_BRANCH} and it works!"
                //echo "I am in ${env.BRANCH_NAME} and it works!"

                script {
                    sh './scripts/build.sh'
                }      
            }
        }

        stage('Test') {
            when {
                expression {
                    params.executeTests
                }
            }
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
                echo "Building version ${parms.VERSION}"
            //  echo "deploying with ${SERVER_CREEDENTIALS}"

                //withCredentials([
                //    usrnamePassword(credentials: 'server-credentials', usernameVariable: USER, passwordVariable: PWD)
                //]) {
                //    sh "some script to deploy ${USER} ${PWD}"
                //
                //}

                // Deploy your Python app (e.g., to a server or a cloud platform)
                script {
                    sh './scripts/deploy.sh'
                //    sh "${SERVER_CREEDENTIALS}"
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
