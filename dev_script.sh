#!/bin/bash
pwdir=$(pwd)

source ~/projects/microservices/scripts/set_environment_variable.sh

cd ~/projects/microservices/education/
docker rm -f education
docker build .
mvn clean package

cd ~/projects/microservices/scripts/
docker compose up --build education &
sleep 7
docker compose up -d &

cd $pwdir
