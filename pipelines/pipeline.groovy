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
        AWS_REGION = 'ap-sountheaast-2'
        AWS_S3_BUCKET 'your-s3-bucket-name'
        ARTIFACTS_PATH = 'dist'
    }

    stages {
        stage('Build') {
            steps {
                echo "Building....."
                sh 'printenv'

                echo "Building environment is ${params.Env}"
                echo "Build Number: ${env.BUILD_NUMBER}"
                echo "Build URL: ${env.BUILD_URL}"
                  
                // Set environment variable based on branch name
   
                echo "I am in ${env.GIT_BRANCH} and it works!"
                //echo "I am in ${env.BRANCH_NAME} and it works!"

                script {
                    sh './scripts/build.sh'
                }

                // saving artifacts
                archiveArtifacts artifacts: 'artifacts/*.tar.gz, artifacts/*.whl', fingerprint: true

                // building S3 bucket and tag it with the build tag
                def BUILD_TAG_NAME = env.BUILD_TAG
                echo "Build Tag: ${BUILD_TAG_NAME}"

                // Deploying AWS S3 bucket
                sh 'npm install'
                sh 'npm install @aws-cdk/core @aws-cdk/aws-s3'
                sh 'npm install aws-cdk-lib/aws-iam'
'               sh 'npm install aws-cdk-lib/aws-iam'
                sh 'cdk deploy'

                // Upload artifacts to S3 bucket
                withAWS(region:"${AWS_REGION}",
                        credentials:'awscredentials',
                    //  endpointUrl:'https://minio.mycompany.com'),
                    //    profile:'~/.aws/credentials',
                    //    roleAccount:'123456789012'
                    {                  
                        s3Upload(file:"${BUILD_TAG_NAME}", bucket:"${AWS_S3_BUCKET}", path:"${ARTIFACTS_PATH}/")

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

                script {
                    sh './scripts/deploy.sh'
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
