pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {

                // Run commands to install dependencies and build the Python app
                sh 'python -m venv venv'
                sh 'source venv/bin/activate'
                sh 'pip install -r requirements.txt'

                // Configuring and installing Python packages or libraries.
                //sh 'python setup.py build'

                // Build a distribution package for a Python project that can be easily shared and installed by others.
                   // Create source distribution and a compressed archive of the project's source code.
                   // Create a binary distribution of the project that can be installed on different platforms. 
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
