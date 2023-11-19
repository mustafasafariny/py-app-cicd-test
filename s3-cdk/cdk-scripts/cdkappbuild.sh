#!/bin/sh
# Typescript dependencies installation (packages and lib/modules)
sh 'sudo npm install -g typescript'
sh 'sudo npm install'
sh 'sudo npm install @aws-cdk/core @aws-cdk/aws-s3'
sh 'sudo npm install aws-cdk-lib/aws-iam'
sh 'sudo npm install aws-cdk-lib/aws-iam'
#sh 'tsc'
#cdk synth
sh 'sudo npm run build'
sh 'sudo npm run cdk synth' 