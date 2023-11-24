#!/bin/bash
set -e
# Typescript dependencies installation (packages and lib/modules)

echo 'cdk build start...'
npm run build

echo 'cdk synth start...'
npm run cdk synth

echo 'cdk deploy start...'
npm run cdk deploy