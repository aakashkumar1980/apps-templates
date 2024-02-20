#!/bin/bash

# Input variables
numvCPU=2
numMemoryGB=3
numWorkers=2

# Calculate the executor memory (75% of numMemoryGB, adjust as needed)
executorMemoryGB=$(echo "0.75 * $numMemoryGB" | bc | awk '{print int($1+0.5)}')
# Calculate memory for Docker Compose environment variable
calculatedWorkerMemory=$(echo "$executorMemoryGB" | bc)

# Export variables for Docker Compose
export numvCPU numWorkers calculatedWorkerMemory
# Start the Docker Compose process
echo "Starting DockerCompose with $numvCPU vCPU, $calculatedWorkerMemory GB memory, and $numWorkers workers"
docker-compose up
