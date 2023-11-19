#!/bin/bash
# Typescript dependencies installation (packages and lib/modules)
#sudo npm install -g typescript
sudo npm install @aws-cdk/core @aws-cdk/aws-s3
sudo npm install aws-cdk-lib/aws-iam
sudo npm install aws-cdk-lib/aws-iam
#tsc
#cdk synth
sudo npm run build
sudo npm run cdk synth