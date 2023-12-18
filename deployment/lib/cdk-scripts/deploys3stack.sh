#!/bin/bash
set -e

echo 'deploy.sh start'
pushd  ./deployment/bin
    pwd 
    cdk deploy --app "npx ts-node cdk-infra-app-code.js" CdkInfraAppCodeStack -v
popd
echo 'deploy.sh end'    