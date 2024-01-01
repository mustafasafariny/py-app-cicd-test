#!/bin/bash
set -e
echo 'start testing'
pwd
#pytest testRoutes.py
#cd ./src/demo-py-app
pytest flaskapp.py
echo 'end testing'