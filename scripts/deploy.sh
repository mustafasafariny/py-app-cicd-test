#!/bin/bash
set -e

pushd  ./deployment/bin 
    cdk deploy --app "npx ts-node cdk-infra-app-code.js" CdkInfraAppCodeStack
popd    