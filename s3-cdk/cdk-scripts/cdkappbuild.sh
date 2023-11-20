#!/bin/bash
set -e
# Typescript dependencies installation (packages and lib/modules)
sudo -s npm install @aws-cdk/core @aws-cdk/aws-s3
sudo -s npm install aws-cdk-lib/aws-iam

#tsc
#cdk synth
sudo -s npm run build
sudo -s npm run cdk synth