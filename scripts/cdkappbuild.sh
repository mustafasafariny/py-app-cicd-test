#!/bin/sh
# Typescript dependencies installation (packages and lib/modules)

sh 'npm install -g typescript'
sh 'npm install'
sh 'npm install @aws-cdk/core @aws-cdk/aws-s3'
sh 'npm install aws-cdk-lib/aws-iam'
sh 'npm install aws-cdk-lib/aws-iam'
#sh 'tsc'
sh 'npm run build'
sh 'npm run cdk synth' 