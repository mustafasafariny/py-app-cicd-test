#!/bin/bash
set -e
# Typescript dependencies installation (packages and lib/modules)

#echo 'cdk build start...'
#npm run build
tsc

echo 'cdk synth start...'
#npm run cdk synth
cdk synth cdk-infra-stack

echo 'cdk deploy start...'
#npm run cdk deploy
cdk deploy cdk-infra-stack