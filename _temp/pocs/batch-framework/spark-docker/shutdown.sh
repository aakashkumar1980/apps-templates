#!/bin/bash

# Stop all containers
docker-compose down

# Remove all containers and data
docker ps -aq | xargs -r docker stop; \
docker ps -aq | xargs -r docker rm; \
docker images -q | xargs -r docker rmi -f; \
docker volume ls -q | xargs -r docker volume rm

find ./data/namenode_data -mindepth 1 ! -name 'empty.txt' -exec rm -rf {} +
find ./data/datanode_data1 -mindepth 1 ! -name 'empty.txt' -exec rm -rf {} +
find ./data/datanode_data2 -mindepth 1 ! -name 'empty.txt' -exec rm -rf {} +

