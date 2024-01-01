#!/bin/bash
set -e
echo 'build start...'

cd ./src/demo-py-app
sudo su

#Create Virtual Environment (recommended)
sudo rm -rf venv
sudo apt install python3-venv
sudo python3 -m venv venv
#source venv/bin/activate
. venv/bin/activate

#Install Python dependencies
sudo apt-get install python3-pip
sudo pip install -r requirements.txt

#Create Artifacts
#sdist: creates a source distribution package which contains source code, along with the necessary files for installation.
#bdist_wheel: creates a binary distribution, specifically in the Wheel format
#creating both a source distribution and a binary distribution of your Python package.
#This allows users to choose whether they want to install from the source or from a pre-built binary, depending on their preference or system compatibility.
#These files can be uploaded to the Python Package Index (PyPI) for distribution or shared directly with others.

sudo pip3 install setuptools wheel
sudo python3 setup.py sdist bdist_wheel

mkdir -p artifacts
sudo mv dist/* artifacts/