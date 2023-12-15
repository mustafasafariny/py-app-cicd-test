#!/bin/bash
set -e

sudo apt update -y
sudo apt install -y curl software-properties-common
curl -sL https://deb.nodesource.com/setup_14.x | sudo -E bash -
sudo apt-get update
sudo apt-get install -y nodejs
npm install
                                
cd  /var/lib/jenkins/workspace/pyapp-test-pipeline/deployment/bin
sudo tsc                               
sudo cdk synth --app "npx ts-node deployment.js" CdkInfraAppCodeStack
sudo cdk deploy --app "npx ts-node deployment.js" CdkInfraAppCodeStack
cd                                
cd  /var/lib/jenkins/workspace/pyapp-test-pipeline/artifacts
pwd