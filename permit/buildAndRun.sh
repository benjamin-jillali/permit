#!/bin/sh
mvn clean package && docker build -t permit/permit .
docker rm -f permit || true && docker run -d -p 8080:8080 -p 4848:4848 --name permit permit/permit 
