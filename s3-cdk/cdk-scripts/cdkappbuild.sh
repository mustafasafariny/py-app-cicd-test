#!/bin/bash
set -e
# Typescript dependencies installation (packages and lib/modules)
sudo -s npm install @aws-cdk/core @aws-cdk/aws-s3
sudo -s npm install aws-cdk-lib/aws-iam
sudo -s npm install aws-cdk-lib/aws-kms
#tsc
#cdk synth
echo 'cdk build start...'
sudo -s npm run build

echo 'cdk synth start...'
sudo -s npm run cdk synth