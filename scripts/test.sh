#!/bin/sh
set -e

#discover and run all unit tests in the current directory and its subdirectories. 
sudo -s python3 -m unittest discover

#Rnning app on the web flask server
sudo -s python3 src/demo-py-app/main.py