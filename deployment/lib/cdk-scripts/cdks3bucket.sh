#!/bin/bash
set -e

# sudo apt update -y
# sudo apt install -y curl software-properties-common
# curl -sL https://deb.nodesource.com/setup_14.x | sudo -E bash -
# sudo apt-get update
# sudo apt-get install -y nodejs

pushd  ./deployment
    npm install
    npm run build
popd

pushd  ./deployment/bin                                 
    cdk synth --app "npx ts-node cdk-infra-app-code.js" CdkInfraAppCodeStack

    withCredentials(bindings: [sshUserPrivateKey(credentialsId: 'awssshcredentials', keyFileVariable: 'sshcrd')]) {
        cdk deploy --app "npx ts-node cdk-infra-app-code.js" CdkInfraAppCodeStack
    }
popd