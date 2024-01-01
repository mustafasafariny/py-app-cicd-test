#When you run pytest testRoutes.py in the terminal, pytest will discover and execute all the test cases in the specified file,
#providing output indicating whether the tests passed or failed.
#This is an essential part of a continuous integration/Deployment (CICD) process to ensure that changes to your codebase don't break existing functionality.

import pytest

from flask import *

def test_home_route():
    client = app.test_client()
    response = client.get('/')
    assert response.status_code == 200
    assert b'Welcome to Jenkins Learning Home by Mustafa' in response.data

def test_about_route():
    client = app.test_client()
    response = client.get('/about')
    assert response.status_code == 200
    assert b'About Us' in response.data