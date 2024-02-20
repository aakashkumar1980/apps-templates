#!/bin/bash

numvCPU=2
numMemoryGB=4
numWorkers=1

# compile the Dockerfile and create a custom image
docker build -t custom-spark:v1 .

executorMemoryGB=$(echo "0.75 * $numMemoryGB" | bc | awk '{print int($1+0.5)}')
calculatedWorkerMemory=$(echo "$executorMemoryGB" | bc)
export numvCPU numWorkers calculatedWorkerMemory
# Start the Docker Compose process
echo "Starting DockerCompose with $numvCPU vCPU, $calculatedWorkerMemory GB memory, and $numWorkers workers"
docker-compose up 
# docker-compose up -d --scale spark-worker=$numWorkers
