#!/bin/sh
set -e

# Create a virtual environment
sudo python3 -m venv venv
sudo source venv/bin/activate
sudo pip install Flask

# Install python pacakges and dependencies 
sudo pip install -r requirements.txt

# Build a distribution package for a Python project that can be easily shared and installed by others.
  #Create source distribution and a compressed archive of the project's source code.
  #Create a binary distribution of the project that can be installed on different platforms.

sudo pip install setuptools
sudo python setup.py sdist bdist_wheel

# Move the artifacts to a designated directory
sudo mkdir -p artifacts
sudo mv dist/* artifacts/
                    
#Archive the generated artifacts
sudo archiveArtifacts artifacts: 'dist/*', fingerprint: true