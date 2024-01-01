#!/bin/bash
set -e
echo 'start testing'
pytest testRoutes.py
#pytest flaskapp.py
echo 'end testing'