#!/bin/bash
set -e

sudo apt update -y
sudo apt install -y curl software-properties-common
curl -sL https://deb.nodesource.com/setup_14.x | sudo -E bash -
sudo apt-get update
sudo apt-get install -y nodejs
npm install
                                
cd  /var/lib/jenkins/workspace/pyapp-test-pipeline/cdk-infra-app-code/bin
sudo tsc                               
sudo cdk synth --app "npx ts-node cdk-infra-app-code.js" CdkInfraAppCodeStack
#sudo cdk deploy