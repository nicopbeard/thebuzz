#!/bin/bash

docker rm phase0-postgres
docker pull postgres
docker run -p5432:5432 --name phase0-postgres -e POSTGRES_PASSWORD=S010698docker -e POSTGRES_USER=kaspesi -d postgres

