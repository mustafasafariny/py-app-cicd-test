#!/bin/bash
set -e
# Typescript dependencies installation (packages and lib/modules)
npm install

#tsc
#cdk synth
echo 'cdk build start...'
npm run build

echo 'cdk synth start...'
npm run cdk synth

#sudo -s 'npm run build && cdk synth --all'
#sudo -s 'cdk destroy'