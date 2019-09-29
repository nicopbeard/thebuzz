#!/bin/bash

cd ..
cd web
sh deploy.sh
cd ..
cd backend
mvn clean; mvn package
POSTGRES_IP=127.0.0.1 POSTGRES_PORT=5432 POSTGRES_USER=some_user_name POSTGRES_PASS=abc123 mvn exec:java
