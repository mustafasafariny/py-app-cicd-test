#!/bin/sh
set -e

# Create a virtual environment
sudo -s python3 -m venv venv
sudo -s source venv/bin/activate
sudo -s pip3 install Flask

# Install python pacakges and dependencies 
sudo python install -r requirements.txt

# Build a distribution package for a Python project that can be easily shared and installed by others.
  #Create source distribution and a compressed archive of the project's source code.
  #Create a binary distribution of the project that can be installed on different platforms.

sudo python -m pip install setuptools
sudo python -m pip setup.py sdist bdist_wheel

# Move the artifacts to a designated directory
sudo -s mkdir -p artifacts
sudo -s mv dist/* artifacts/
                    
#Archive the generated artifacts
sudo -s archiveArtifacts artifacts: 'dist/*', fingerprint: true