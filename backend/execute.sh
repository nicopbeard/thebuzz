#!/bin/bash

<<<<<<< HEAD
cd .. 
cd web 
sh deploy.sh
cd ..
cd backend 
mvn clean; mvn package
POSTGRES_IP=127.0.0.1 POSTGRES_PORT=5432 POSTGRES_USER=kaspesi POSTGRES_PASS=S010698docker mvn exec:java
=======
cd ..
cd web
sh deploy.sh
cd ..
cd backend
mvn clean; mvn package
POSTGRES_IP=127.0.0.1 POSTGRES_PORT=5432 POSTGRES_USER=some_user_name POSTGRES_PASS=abc123 mvn exec:java
>>>>>>> 5a3617cc3e0a64e3631dd850d43ba216d3b5320b
