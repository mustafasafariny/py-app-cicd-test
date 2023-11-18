#!/bin/sh
set -e

// Deploying AWS S3 bucket
sh 'npm install'
sh 'npm install @aws-cdk/core @aws-cdk/aws-s3'
sh 'npm install aws-cdk-lib/aws-iam'
sh 'npm install aws-cdk-lib/aws-iam'

sh 'cdk deploy'