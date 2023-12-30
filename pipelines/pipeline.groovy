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

                //withAWS(roleAccount:'144358027444', role:'AWS-DevOps-Identity')
                withAWS(roleAccount:"${AWS_DEFAULT_ACCOUNT}", role:"${AWS_ROLE}", region: "${AWS_DEFAULT_REGION}" )
                    {
                    sh '''
                        cd ./src/demo-py-app/artifacts
                        aws --version
                        #aws sts get-caller-identity
                        #aws ec2 describe-instances --region=ap-southeast-2
                        #aws s3 ls

                        echo 'before upload...'
                        #s3Upload(bucket:"mus.cicd.cdk.demo", path:'py-app-artifacts/', workingDir:'artifacts', includePathPattern:'**/*')
                        #s3Upload(file: 'file.txt', bucket: 'my-bucket', tags: '[tag1:value1, tag2:value2]')

                        #def tags=[:]
                        #tags["tag1"]="${env.BUILD_TAG}"
                        #tags["tag2"]=""

                        s3Upload(
                            file: "artifacts/",
                            bucket: 'mus.cicd.cdk.demo',
                            path: 'py-app-artifacts/',
                            metadatas: ["repo:${env.JOB_NAME}", "branch:${env.BRANCH}", "commit:${env.GIT_COMMIT}"]
                        )

                        #s3Upload(file:${env.BUILD_TAG}, bucket:"${AWS_S3_BUCKET}", path:"${ARTIFACTS_DIR}/")

                        #aws s3 cp *.whl s3://mus.cicd.cdk.demo/py-app-artifacts/
                        #aws s3 cp *.gz s3://mus.cicd.cdk.demo/py-app-artifacts/
                        echo 'after upload...'
                    '''
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
