#!/bin/bash
set -e
# Typescript dependencies installation (packages and lib/modules)

echo 'cdk build start...'
npm run build

echo 'cdk bootstrap start...'
cdk bootstrap

echo 'cdk synth start...'
npm run cdk synth

#sudo -s 'npm run build && cdk synth --all'
#sudo -s 'cdk destroy'

#Bootstrap 
#Bootstrapping is the process of provisioning resources for the AWS CDK before you can deploy AWS CDK apps into an AWS environment.
#(An AWS environment is a combination of an AWS account and Region).
#These resources include an Amazon S3 bucket for storing files and IAM roles that grant permissions needed to perform deployments.
#The required resources are defined in an AWS CloudFormation stack, called the bootstrap stack, which is usually named CDKToolkit.
#Like any AWS CloudFormation stack, it appears in the AWS CloudFormation console once it has been deployed.