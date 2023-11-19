#!/bin/sh
# Typescript dependencies installation (packages and lib/modules)
npm install
npm install -g typescript
npm install @aws-cdk/core @aws-cdk/aws-s3
npm install aws-cdk-lib/aws-iam
npm install aws-cdk-lib/aws-iam
#tsc
#cdk synth
npm run build
npm run cdk synth