from setuptools import setup, find_packages

setup(
    name='demo_app',
    version='0.1.0',
    packages=find_packages(),
    author="Mustafa",
    author_email="mustafa.mustafa@domain.com.au",
    description="package for python application",
#    url="https://pypi.example.org/pypi/somedep/",
    install_requires=[
        # List your project dependencies here
    ],
#    data_files=[
#    ('.../MyProgram/img/logo.ico'),
#    ('.../MyProgram/media/head.txt'),
#    ],
#    entry_points={
#        'console_scripts': [
#            'your_script = your_module.your_script:main',
#        ],
#    },
)
