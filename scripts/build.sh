#!/bin/bash
set -e

# Create a virtual environment
sudo -s apt-get install python3-venv
sudo -s python3 -m venv venv
sudo -s source venv/bin/activate
sudo -s pip3 install Flask

# Install python pacakges and dependencies 
sudo -s pip3 install -r ./src/demo-py-app/requirements.txt


# Build a distribution package for a Python project that can be easily shared and installed by others.
  #Create source distribution and a compressed archive of the project's source code.
  #Create a binary distribution of the project that can be installed on different platforms.
sudo -s pip3 install setuptools wheel
sudo -s python3 setup.py sdist bdist_wheel

# Move the artifacts to a designated directory
sudo -s mkdir -p artifacts
sudo -s mv dist/* artifacts/
