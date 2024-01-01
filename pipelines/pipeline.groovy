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
        AWS_DEFAULT_REGION = 'ap-southeast-2'
        AWS_DEFAULT_ACCOUNT = '144358027444'
        AWS_ROLE = 'AWS-DevOps-Identity'
        AWS_S3_BUCKET = 'mus.cicd.cdk.demo'      

    }

    stages {
        stage('Build') {
            steps {
                echo "Building....."
                sh 'printenv'                  
                echo "I am in ${env.GIT_BRANCH} and it works!"
 
                script {
                    sh 'chmod +x ./src/scripts/build.sh'
                }
                
                //Create Aritcats files in different formats
                //while tar.gz is often used for distributing source code that needs to be built, 
                //the wheel format is used for distributing pre-built binary packages that can be installed more quickly. 

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
                    sh '''                  
                    cd ./src/demo-py-app
                    pytest flaskapp.py
                    '''
                    //sh 'chmod +x ./src/scripts/test.sh'
                }

            }
        }

        stage('Deploy') {
            //when {
            //  expression {
            //       currentBuild.result == null || currentBuild.result == 'SUCCESS' 
            //  }
            //}  
            steps {
                echo 'Deploying...'
                echo " Deployment environment is ${params.Env}"

                echo 'Uploading S3 Bucket...'

                //Option 1 - using AWS CLI cp api to copy both artifacts to the S3 Bucket (copy articats files separately)
                //Option 2 - using Jenkins Pipeline: AWS Steps Plugin (preffered because i can copy articats dir not files separately)

                withAWS(roleAccount:"${AWS_DEFAULT_ACCOUNT}", role:"${AWS_ROLE}", region: "${AWS_DEFAULT_REGION}" )
                    {
                        //sh 'chmod +x ./src/scripts/deploy.sh'   // option 1                   
                        
                        // option 2
                        s3Upload(
                            file: "./src/demo-py-app/artifacts/",
                            bucket: 'mus.cicd.cdk.demo',
                            path: 'py-app-artifacts/',
                            tags: '[tag1:${env.BUILD_TAG}, tag2:${env.GIT_BRANCH}]',
                            metadatas: ["repo:${env.JOB_NAME}", "branch:${env.BRANCH}", "commit:${env.GIT_COMMIT}"]
                        )

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
