#!/bin/sh
set -e

# Create a virtual environment
sudo -n python3 -m venv venv
sudo -n source venv/bin/activate
sudo -n pip install Flask

# Install python pacakges and dependencies 
sudo -n pip install -r requirements.txt

# Build a distribution package for a Python project that can be easily shared and installed by others.
  #Create source distribution and a compressed archive of the project's source code.
  #Create a binary distribution of the project that can be installed on different platforms.

sudo -n pip install setuptools
sudo -n python setup.py sdist bdist_wheel

# Move the artifacts to a designated directory
sudo -n mkdir -p artifacts
sudo -n mv dist/* artifacts/
                    
#Archive the generated artifacts
sudo -n archiveArtifacts artifacts: 'dist/*', fingerprint: true