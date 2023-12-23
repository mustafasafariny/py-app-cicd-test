
from app import app

def test_Home():
    response = app.test_client().get('/')
    assert b"Welcome to Jenkins Tutorials" in response.data
    assert response.status_code == 200