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
        AWS_PROFILE = 'cdk-sandpit'  // Use profile information from ~/.aws/config
        AWS_DEFAULT_REGION = 'ap-southeast-2'
        AWS_DEFAULT_ACCOUNT = '144358027444'
        AWS_ROLE = 'AWS-DevOps-Identity'
        AWS_S3_BUCKET = 'mus.cicd.cdk.demo'      
        ARTIFACTS_DIR = 'artifacts'
    //  AWS_S3_AUTH_TOKEN =credentials('mustafa-aws-creds')



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
                    sh 'chmod +x ./src/scripts/build.sh'
                    //def BUILD_TAG_NAME = env.BUILD_TAG
                    echo "Build Tag: ${env.BUILD_TAG}" 
                }

                archiveArtifacts artifacts: 'src/demo-py-app/artifacts/*.tar.gz, src/demo-py-app/artifacts/*.whl', fingerprint: true
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
                    sh 'chmod +x ./src/scripts/test.sh'

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
                        sh 'chmod +x ./src/scripts/deploy.sh'              
                        }

                echo 'Uploading S3 Bucket...'
                echo "Region: ${AWS_DEFAULT_REGION}"
                echo "Account: ${AWS_DEFAULT_ACCOUNT}"
                echo "Access Key ID: ${AWS_ACCESS_KEY_ID}"
                echo "Access Key Secret: ${AWS_SECRET_ACCESS_KEY}"

                //withCredentials([string(credentialsId: 'mustafa-aws-creds', variable: 'AWS_SESSION_TOKEN')])
                withCredentials([aws(accessKeyVariable:'AWS_ACCESS_KEY_ID', credentialsid:'mustafa-aws-cli-creds', 
                                    secretKeyVariable:'AWS_SECRET_ACCESS_KEY')])
                    {
                    sh '''
                        aws --version
                        aws sts get-caller-identity
                        aws ec2 describe-instances
                        #aws s3 cp --sse AES256 file.txt s3://mus.cicd.cdk.demo/
                    '''
                    }

                //withAWS(credentials: 'awscredentials', profile: 'cdk-sandpit', region: 'ap-southeast-2', role: 'AWS-DevOps-Identity', roleAccount: '144358027444') 
                //    {
                //    s3Upload(file:'artifacts', bucket:'mus.cicd.cdk.demo')
                //    }
                      
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
