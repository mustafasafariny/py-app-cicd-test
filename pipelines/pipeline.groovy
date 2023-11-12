CODE_CHANGES = getGitChanges()
pipeline {
    agent any
    parameteres{
     // string
     // choice
     // booleanParam(name: 'ececuteTests', defaultValue: true, description: '')  
    }
    tools {
        
    }
    environment {
        NEW_VERSION = '1.0.0'
    //    SERVER_CREDENTIALS = credentials('6a1594b8-1e6e-4338-bf32-b2918ef9a157')
    }
    stages {
        stage('Build') {
            when {
                expression {
                    BRANCH_NAME == 'main' && CODE_CHANGES == true
                }
            //when {
            //    expression {
            //        params.executeTests == true
            //    }

            steps {
                echo 'Building the application....}'    
                echo "Building version ${new_version}"

                // Print the build number and build URL
                echo "Build Number: ${env.BUILD_NUMBER}"
                echo "Build URL: ${env.BUILD_URL}"

                script {
                    buildscript = load "build.sh"
                    sh './build.sh'
                }   
            }
        }

        stage('Test') {
            steps {
                when {
                    expression {
                        BRANCH_NAME == 'main'
                    }
                }
                echo 'Testing...'
                // Run tests for Python app
                script {
                    testscript = load "test.sh"
                }   

            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying...'
            //    withCredentials([
            //        usernamePassword(credentials: 'server-credentials', usernameVariable:USER, passwordVariable:PWD )
            //    ])

            // Deploy your Python app (e.g., to a server or a cloud platform)
                script {
                    deploycript = load "deploy.sh"
                }   
            }
        }
    }

    post {
            always {
                echo 'Sending email...'
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
