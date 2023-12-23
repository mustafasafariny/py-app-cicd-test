#!/bin/bash
set -e
echo 'build start...'
# Create a virtual environment
#sudo rm -rf venv
sudo apt install python3.10-venv
sudo python3.10 -m venv venv

#sudo apt-get install python3-venv
#sudo python3 -m venv venv

sudo source venv/bin/activate
#sudo source /var/lib/jenkins/workspace/myapp-test-pipeline/venv/bin/activate
sudo pip3 install Flask

# Install python pacakges and dependencies 
sudo pip3 install -r ./src/demo-py-app/requirements.txt


# Build a distribution package for a Python project that can be easily shared and installed by others.
  #Create source distribution and a compressed archive of the project's source code.
  #Create a binary distribution of the project that can be installed on different platforms.
sudo pip3 install setuptools wheel
sudo python3 setup.py sdist bdist_wheel

# Move the artifacts to a designated directory
sudo mkdir -p artifacts
sudo mv dist/* artifacts/

#pushd  ./deployment
#    npm install
#    npm run build
#popd

#pushd  ./deployment/bin                                 
#    cdk synth --app "npx ts-node cdk-infra-app-code.js" CdkInfraAppCodeStack
#popd