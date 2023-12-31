#!/bin/bash
set -e
echo 'build start...'

cd ./src/demo-py-app
sudo su

#Create Virtual Environment
sudo rm -rf venv
sudo apt install python3-venv
sudo python3 -m venv venv
#source venv/bin/activate
. venv/bin/activate

#Install Python dependencies
sudo apt-get install python3-pip
sudo pip install -r requirements.txt

#Create Artifacts
sudo pip3 install setuptools wheel
sudo python3 setup.py sdist bdist_wheel

## Clean up virtual environment
deactivate

mkdir -p artifacts
sudo mv dist/* artifacts/