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
        AWS_REGION = 'ap-sountheast-2'
        AWS_S3_BUCKET = 'mustafa.py.app.demo.cdk.artifacts'      
        AWS_S3_BUCKET_PATH = 'cicd-demo/'
        ARTIFACTS_FILE = 'artifacts'
        WORKING_DIR = 'dist'
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
                }

                // saving artifacts
                archiveArtifacts artifacts: 'artifacts/*.tar.gz, artifacts/*.whl', fingerprint: true

                // building S3 tag
                // def BUILD_TAG_NAME = env.BUILD_TAG              
                echo "Build Tag: ${env.BUILD_TAG}"

                // create AWS S3 Bucket and Upload artifacts into it
                // but first get authorization - security access credentials 

                withAWS(region:"${AWS_REGION}",
                        credentials:'awscredentials'  //Use Jenkins AWS credentials information (AWS Access Key: AccessKeyId, AWS Secret Key: SecretAccessKey):
                    //    profile:'~/.aws/credentials',
                    //    role:'AWS-DevOps-Identity',
                    //    roleAccount:'144358027444'
                        )

                        {   dir('./s3-cdk') {
                                echo 'changed dir'
                                sh 'sudo apt clean'
                                sh 'sudo apt --fix-broken install'
                                sh 'sudo apt-get update'
                                sh 'sudo apt-get install -f'
                                sh 'sudo apt-get autoclean -y'
                                sh 'sudo apt-get autoremove -y'
                                sh 'sudo apt remove nodejs -y'
                                sh 'sudo apt remove nodejs-doc -y'
                                sh 'sudo dpkg --remove --force-remove-reinstreq libnode-dev'
                                sh 'sudo dpkg --remove --force-remove-reinstreq libnode72:amd64'

                                sh 'sudo apt update -y'

                                sh 'sudo apt install -y curl software-properties-common'
                                sh 'curl -sL https://deb.nodesource.com/setup_14.x | sudo -E bash -'

                                sh 'sudo apt-get update'
                                sh 'sudo apt-get install -y nodejs'
                                                                                            
                                sh 'sudo npm install'
                      
                                //sh 'sudo npm install typescript'  
                                sh './cdk-scripts/cdkappbuild.sh'

                                echo "before s3 upload...!"

                                s3Upload(file: "${ARTIFACTS_FILE}",
                                    tags: "${env.BUILD_TAG}",
                                    bucket:"${AWS_S3_BUCKET}",
                                    path: "${AWS_S3_BUCKET_PATH}",
                                    workingDir:"${WORKING_DIR}",
                                    includePathPattern:'**/*.gz,**/*.whl')
                            }
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
            }
            success {
                echo 'Build successful! Deploying...'                           
               }
            failure {
                echo 'Build failed! Not deploying...'
            }
        }  
}
