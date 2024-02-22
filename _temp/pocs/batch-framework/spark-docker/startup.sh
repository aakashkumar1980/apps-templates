#!/bin/bash

numvCPU=4
numMemoryGB=6
numWorkers=10

##########################
##### STARTUP SCRIPT #####
##########################
# compile the Dockerfile and create a custom image
#docker build --no-cache -t custom-spark:v1 .
docker build -t custom-spark:v1 .

executorMemoryGB=$(echo "0.75 * $numMemoryGB" | bc | awk '{print int($1+0.5)}')
calculatedWorkerMemory=$(echo "$executorMemoryGB" | bc)
export numvCPU numWorkers calculatedWorkerMemory
echo "Starting DockerCompose with $numvCPU vCPU, $calculatedWorkerMemory GB memory, and $numWorkers workers"
docker-compose up -d
# docker-compose up -d --scale spark-worker=$numWorkers


sleep 30
###############################
##### POST_STARTUP SCRIPT #####
###############################
./post-startup.sh
