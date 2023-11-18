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
        AWS_S3_BUCKET 'mustafa.py.app.demo.cdk.artifacts'      
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

                // building S3 bucket and tag it with the build tag
                def BUILD_TAG_NAME = env.BUILD_TAG              
                echo "Build Tag: ${BUILD_TAG_NAME}"

                // Upload artifacts to S3 bucket
                withAWS(region:"${AWS_REGION}",
                        credentials:'awscredentials',  //Use Jenkins AWS credentials information (AWS Access Key: AccessKeyId, AWS Secret Key: SecretAccessKey):
                    //    profile:'~/.aws/credentials',
                    //    role:'AWS-DevOps-Identity',
                    //    roleAccount:'144358027444'
                        )
                            {                  
                                s3Upload(file: "${ARTIFACTS_FILE}",
                                    tags: "${BUILD_TAG_NAME}",
                                    bucket:"${AWS_S3_BUCKET}",
                                    path: "${AWS_S3_BUCKET_PATH}",
                                    workingDir:"${WORKING_DIR}",
                                    includePathPattern:'**/*.gz,**/*.whl'

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
