#!/bin/bash

numvCPU=3
numMemoryGB=4
numWorkers=3

###########################
##### SHUTDOWN SCRIPT #####
###########################
executorMemoryGB=$(echo "0.75 * $numMemoryGB" | bc | awk '{print int($1+0.5)}')
calculatedWorkerMemory=$(echo "$executorMemoryGB" | bc)
export numvCPU numWorkers calculatedWorkerMemory
# Stop all containers
docker-compose down


##########################
##### CLEANUP SCRIPT #####
##########################
# Stop all running containers
docker ps -q | xargs -r docker stop
# Remove all containers
docker ps -aq | xargs -r docker rm
# Force remove all images
docker images -q | xargs -r docker rmi -f
# Remove all volumes
docker volume ls -q | xargs -r docker volume rm
# Remove all stopped containers, unused networks, and dangling images
docker system prune -f
# Remove unused volumes
docker volume prune -f
# Remove unused networks
docker network prune -f
# Optionally, remove all unused images, not just dangling ones
docker image prune -a -f


find ./data/namenode_data -mindepth 1 ! -name 'empty.txt' -exec rm -rf {} +
find ./data/datanode_data1 -mindepth 1 ! -name 'empty.txt' -exec rm -rf {} +
find ./data/datanode_data2 -mindepth 1 ! -name 'empty.txt' -exec rm -rf {} +
find ./data/spark_master -mindepth 1 ! -name 'empty.txt' -exec rm -rf {} +
find ./data/spark_workers -mindepth 1 ! -name 'empty.txt' -exec rm -rf {} +
find ./data/notebooks -mindepth 1 ! -name 'empty.txt' -exec rm -rf {} +

