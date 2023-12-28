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
        //AWS_DEFAULT_PROFILE = 'cdk-sandpit'  // Use profile information from ~/.aws/config
        AWS_DEFAULT_REGION = 'ap-southeast-2'
        AWS_DEFAULT_ACCOUNT = '144358027444'
        AWS_ROLE = 'AWS-DevOps-Identity'
        AWS_S3_BUCKET = 'mus.cicd.cdk.demo'      
        ARTIFACTS_DIR = 'artifacts'

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

                withCredentials([aws(credentialsId: 'mus-jenkins-cicd-demo', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')])
                    {
                    sh '''
                        cd ./src/demo-py-app
                        pwd
                        aws --version
                        aws sts get-caller-identity

                        aws s3 ls --profile cdk-sandpit
                        #aws configure set role_arn arn:aws:iam::144358027444:role/MusCdkTypescriptStackStac-MusCdkInstanceRole1AF4F1B-QgED4yClCjKH
                        #aws ec2 describe-instances
                        #aws s3 ls

                        cd artifacts
                        aws s3 cp *.whl s3://mus.cicd.cdk.demo/py-app-artifacts/
                        #aws s3 cp mustest1.txt s3://mus.cicd.cdk.demo/py-app-artifacts/

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
