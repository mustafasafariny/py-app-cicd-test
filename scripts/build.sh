#!/bin/sh
set -e

# Create a virtual environment
python3 -m venv venv
source venv/bin/activate
pip install Flask

# Install python pacakges and dependencies 
pip install -r requirements.txt

# Build a distribution package for a Python project that can be easily shared and installed by others.
  #Create source distribution and a compressed archive of the project's source code.
  #Create a binary distribution of the project that can be installed on different platforms.

pip install setuptools
python setup.py sdist bdist_wheel

# Move the artifacts to a designated directory
mkdir -p artifacts
mv dist/* artifacts/
                    
#Archive the generated artifacts
archiveArtifacts artifacts: 'dist/*', fingerprint: true