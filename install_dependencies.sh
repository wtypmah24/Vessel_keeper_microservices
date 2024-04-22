#!/bin/bash

# install Git
sudo yum install -y git

# install Docker
sudo yum install -y docker

# install Java
sudo yum install -y java

# install Maven
sudo yum install -y maven

# install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
