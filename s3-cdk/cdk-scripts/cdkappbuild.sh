#!/bin/bash
set -e
# Typescript dependencies installation (packages and lib/modules)
npm install aws-cdk-lib
npm install aws-cdk-lib/aws-s3
npm install aws-cdk-lib/aws-iam
npm install aws-cdk-lib/aws-kms

#tsc
#cdk synth
echo 'cdk build start...'
npm run build

echo 'cdk synth start...'
npm run cdk synth

#sudo -s 'npm run build && cdk synth --all'
#sudo -s 'cdk destroy'