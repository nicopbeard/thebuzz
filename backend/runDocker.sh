#!/bin/bash

docker rm phase0-postgres
docker pull postgres
docker run -p5432:5432 --name phase0-postgres -e POSTGRES_PASSWORD=abc123 -e POSTGRES_USER=some_user_name -d postgres

