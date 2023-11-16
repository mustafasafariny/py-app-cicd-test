pipeline {
    agent any

    parameters{       
        choice(
            name: 'Env',
            choices: ['DEV', 'PROD'],
            description: 'Passing the Environment'
            )
        booleanParam(
            name: 'executeTests',
            defaultValue: true,
            description: ''
            )
    }

    //tools{
    //    maven 'Maven'
    //}

    //environment {
    //    DISABLE_AUTH = 'true'
    //    SERVER_CREEDENTIALS = credentials('server-credentials')
    //    CURRENT_VERSION = '1.0.0'
    //}

    stages {
        stage('Build') {
            //when {
            //    expression {
            //        env.BRANCH_NAME == 'main'
            //        // && CODE_CHANGES == true
            //    }
            //}

            steps {
                echo "Building....."
                sh 'printenv'

                echo " Building environment is ${params.Env}"
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
                echo " Testing environment is ${params.Env}"
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
                echo " Deployment environment is ${params.Env}"
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
