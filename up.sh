#!/bin/bash

if [ "$1" == "rebuild" ]; then
  mvn clean package -DskipTests && docker-compose up -d --build;
elif [ "$1" == "recreate" ]; then
    docker-compose up -d --force-recreate;
  else
    docker-compose up -d;
fi
