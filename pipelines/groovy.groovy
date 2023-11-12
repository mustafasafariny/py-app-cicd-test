pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                // Run commands to install dependencies and build the Python app
                sh 'pip install -r requirements.txt'
                sh 'python setup.py build'

                // Create a distribution package (e.g., a tarball or a wheel)
                sh 'python setup.py sdist bdist_wheel'

                // Move the artifacts to a designated directory
                    sh 'mkdir -p artifacts'
                    sh 'mv dist/* artifacts/
                    
                // Archive the generated artifacts
                archiveArtifacts artifacts: 'dist/*', fingerprint: true
            }
        }

        stage('Test') {
            steps {
                // Run tests for Python app
                sh 'python -m unittest discover'
            }
        }

        stage('Deploy') {
            steps {
                // Deploy your Python app (e.g., to a server or a cloud platform)
                sh 'python deploy.py'
            }
        }
    }
}
