#!/bin/bash

# Input variables
numvCPU=2
numMemoryGB=3
numWorkers=2

executorMemoryGB=$(echo "0.75 * $numMemoryGB" | bc | awk '{print int($1+0.5)}')
calculatedWorkerMemory=$(echo "$executorMemoryGB" | bc)
export numvCPU numWorkers calculatedWorkerMemory
# Start the Docker Compose process
echo "Starting DockerCompose with $numvCPU vCPU, $calculatedWorkerMemory GB memory, and $numWorkers workers"
docker-compose up


