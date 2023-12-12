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

                withAWS(region:"${AWS_REGION}",
                        credentials:'awscredentials'  //Use Jenkins AWS credentials information (AWS Access Key: AccessKeyId, AWS Secret Key: SecretAccessKey):
                    //    profile:'~/.aws/credentials',
                    //    role:'AWS-DevOps-Identity',
                    //    roleAccount:'144358027444'
                        )

                        {   dir('./cdk-infra-app-code')
                             {
                                echo 'changed dir'
                                sh """
                                    sudo apt update -y
                                    sudo apt install -y curl software-properties-common
                                    curl -sL https://deb.nodesource.com/setup_14.x | sudo -E bash -
                                    sudo apt-get update

                                    sudo apt-get install -y nodejs
                                    npm install
                                    npx tsc
                                    cd /bin
                                    sudo cdk synth --app "npx ts-node cdk-infra-app-code.ts" MusCdkS3Stack                        
                                    #sh 'cdk bootstrap aws://144358027444/'ap-sountheast-2'
                                    cdk deploy
                                """
                                //sh './lib/cdk-scripts/cdks3bucket.sh'
                                echo "before s3 upload...!"

                                //withAWS(roleAccount:"${DEFAULT_ACCOUNT}", role:"${DEFAULT_ACCOUNT_JENKINS_ROLE}") {
                                //sh "aws cloudformation deploy --template-file sample/pipeline-s3/cfn-s3.yaml  --stack-name sample-s3-stack --parameter-overrides BucketName=jenkins-zzz-demox-${BUILD_NUMBER}"
                                //    }

                                echo "${ARTIFACTS_FILE}"
                                echo "${env.BUILD_TAG}"
                                echo "${AWS_S3_BUCKET}"

                                
                                s3Upload(file: "${ARTIFACTS_FILE}",
                                         tags: "${env.BUILD_TAG}",
                                         bucket:"${AWS_S3_BUCKET}",
                                //    path: "${AWS_S3_BUCKET_PATH}",
                                //    workingDir:"${WORKING_DIR}",
                                //    includePathPattern:'**/*.gz,**/*.whl',
                                        )
                                 
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
