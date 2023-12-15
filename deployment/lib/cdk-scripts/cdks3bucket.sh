#!/bin/bash
set -e

# sudo apt update -y
# sudo apt install -y curl software-properties-common
# curl -sL https://deb.nodesource.com/setup_14.x | sudo -E bash -
# sudo apt-get update
# sudo apt-get install -y nodejs
npm install
#npm run build
npx tsc
pushd  ./deployment/bin                                 
    cdk synth --app "npx ts-node deployment.js" CdkInfraAppCodeStack
    cdk deploy --app "npx ts-node deployment.js" CdkInfraAppCodeStack
popd