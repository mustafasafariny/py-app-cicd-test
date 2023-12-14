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
        ARTIFACTS_FILE = 'artifacts'
        //AWS_S3_BUCKET_PATH = 'cicd-demo/'
        //WORKING_DIR = 'dist'
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

                withAWS(profile:"${AWS_PROFILE}")

                //withAWS(
                //      region:"${AWS_REGION}"
                //    , credentials:'awscredentials'
                //    , role: "${AWS_ROLE}"
                //    , roleAccount: "${AWS_ACCOUNT}"
                //    )

                        {

                            sh """
                                sudo apt update -y
                                sudo apt install -y curl software-properties-common
                                curl -sL https://deb.nodesource.com/setup_14.x | sudo -E bash -
                                sudo apt-get update
                                sudo apt-get install -y nodejs
                                npm install
                                
                                cd  /var/lib/jenkins/workspace/pyapp-test-pipeline/cdk-infra-app-code/bin
                                sudo npx tsc                               
                                sudo cdk synth --app "npx ts-node cdk-infra-app-code.js" CdkInfraAppCodeStack
                                 
                                #sudo cdk bootstrap aws://144358027444/ap-sountheast-2
                                sudo cdk deploy --app "npx ts-node cdk-infra-app-code.js" CdkInfraAppCodeStack
                            """
                            //sh './lib/cdk-scripts/cdks3bucket.sh'

                            sh 'cd  /var/lib/jenkins/workspace/pyapp-test-pipeline/artifacts'
                            echo "before s3 upload...!"

                            echo "${ARTIFACTS_FILE}"
                            echo "${env.BUILD_TAG}"
                            echo "${AWS_S3_BUCKET}"

                                
                            s3Upload(file: "${ARTIFACTS_FILE}",
                                    tags: "${env.BUILD_TAG}",
                                    bucket:"${AWS_S3_BUCKET}",
                                //  path: "${AWS_S3_BUCKET_PATH}",
                                //  workingDir:"${WORKING_DIR}",
                                //  includePathPattern:'**/*.gz,**/*.whl',
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
