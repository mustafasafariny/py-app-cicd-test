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

    environment {
        AWS_PROFILE = 'ids-devops-sandpit'  // Use profile information from ~/.aws/config
        AWS_REGION = 'ap-sountheast-2'
        AWS_ACCOUNT = '144358027444'
        AWS_ROLE = 'AWS-DevOps-Identity'
        AWS_S3_BUCKET = 'CicdDemoBucket'      
        ARTIFACTS_DIR = 'artifacts'
        AWS_CREDENTIALS ='awssshcredentials'

    }

    stages {
        stage('Build') {
            steps {
                echo "Building....."
                sh 'printenv'

                echo "Building environment is ${params.Env}"
                echo "Build Number: ${env.BUILD_NUMBER}"
                echo "Build URL: ${env.BUILD_URL}"
                   
                echo "I am in ${env.GIT_BRANCH} and it works!"
 
                script {
                    sh './scripts/build.sh'
                    // def BUILD_TAG_NAME = env.BUILD_TAG
                    echo "Build Tag: ${env.BUILD_TAG}" 
                }

                archiveArtifacts artifacts: 'artifacts/*.tar.gz, artifacts/*.whl', fingerprint: true
 
                // create AWS S3 Bucket 
                script {
                    withCredentials([sshUserPrivateKey(credentialsId: 'awssshcredentials', keyFileVariable: 'sshcrd')]) {
                        sh './deployment/lib/cdk-scripts/cdks3bucket.sh'
                    }
                }

                echo "${ARTIFACTS_DIR}"
                echo "${env.BUILD_TAG}"
                echo "${AWS_S3_BUCKET}"

                // Upload artifacts into the created S3 bucket
                // but first get authorization - security access credentials

                echo "before s3 upload...!"

                withAWS(profile:"${AWS_PROFILE}")
                          {  
                            s3Upload(
                                //    file: 'artifacts',
                                    bucket:'CicdDemoBucket',
                                    includePathPattern:'**/*.gz,**/*.whl',
                                    workingDir: '/var/lib/jenkins/workspace',
                                    tags: '[tag1:mustafacdkbucket]'
                                    )                            
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

                script {
                    sh 'chmod +x ./scripts/test.sh'

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

                script {
                    sh 'chmod +x ./scripts/deploy.sh'
                }   
            }
        }
    }

    post {
            always {
                echo 'Send email...'
            //    sh 'cdk destroy --force'
            }
            success {
                echo 'Build successful! Deploying...'                           
               }
            failure {
                echo 'Build failed! Not deploying...'
            }
        }  
}
