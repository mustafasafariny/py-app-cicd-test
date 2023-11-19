#!/bin/sh
# Typescript dependencies installation (packages and lib/modules)
sh 'echo cdk infra build script..'
sh 'npm install -g typescript'
sh 'npm install'
sh 'npm install @aws-cdk/core @aws-cdk/aws-s3'
sh 'npm install aws-cdk-lib/aws-iam'
sh 'npm install aws-cdk-lib/aws-iam'
#sh 'tsc'
#cdk synth
sh 'npm run build'
sh 'npm run cdk synth' 