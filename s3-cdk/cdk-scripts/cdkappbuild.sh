#!/bin/sh
set -e
# Typescript dependencies installation (packages and lib/modules)
sh 'sudo apt update'
sh 'sudo apt install -y curl software-properties-common'
sh 'curl -sL https://deb.nodesource.com/setup_14.x | sudo -E bash -'
sh 'sudo apt install -y nodejs'
sh 'sudo apt-get install -y Node.js '
sh 'sudo apt install npm'
sh 'echo Node.js -v && npm --version '

sudo npm install -g typescript
sudo npm install @aws-cdk/core @aws-cdk/aws-s3
sudo npm install aws-cdk-lib/aws-iam
sudo npm install aws-cdk-lib/aws-iam
#tsc
#cdk synth
sudo npm run build
sudo npm run cdk synth