#!/bin/bash
set -e
# Typescript dependencies installation (packages and lib/modules)

echo 'cdk build start...'
sh 'sudo tsc'
#npm run build

echo 'cdk synth start...'
sh 'sudo cdk synth'
#npm run cdk synth

echo 'cdk deploy start...'
sh 'sudo cdk deploy'
#npm run cdk deploy