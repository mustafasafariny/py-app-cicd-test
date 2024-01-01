#!/bin/bash
set -e

echo 'deploy.sh start'
cd ./src/demo-py-app/artifacts

aws s3 cp *.whl s3://mus.cicd.cdk.demo/py-app-artifacts/
aws s3 cp *.gz s3://mus.cicd.cdk.demo/py-app-artifacts/

echo 'deploy.sh end'    