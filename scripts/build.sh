#!/bin/bash
set -e

# Create a virtual environment
sh 'python -m venv venv'
sh 'source venv/bin/activate'
sh 'pip install Flask'

# Install dependencies 
sh 'pip install -r requirements.txt'

# Build a distribution package for a Python project that can be easily shared and installed by others.
  #Create source distribution and a compressed archive of the project's source code.
  #Create a binary distribution of the project that can be installed on different platforms.

sh 'apt-get update'
sh 'apt-get install -y python3'

sh 'pip install setuptools'
sh 'python setup.py sdist bdist_wheel'

# Move the artifacts to a designated directory
sh 'mkdir -p artifacts'
sh 'mv dist/* artifacts/
                    
#Archive the generated artifacts
archiveArtifacts artifacts: 'dist/*', fingerprint: true