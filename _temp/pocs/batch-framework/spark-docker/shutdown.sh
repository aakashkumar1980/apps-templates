#!/bin/bash


while IFS='=' read -r key value
do
  if [[ "$key" == "numvCPU" ]]; then
    numvCPU="$value"
  elif [[ "$key" == "numMemoryGB" ]]; then
    numMemoryGB="$value"
  elif [[ "$key" == "numWorkers" ]]; then
    numWorkers="$value"
  fi
done < config.properties
###########################
##### SHUTDOWN SCRIPT #####
###########################
executorMemoryGB=$(echo "0.75 * $numMemoryGB" | bc | awk '{print int($1+0.5)}')
calculatedWorkerMemory=$(echo "$executorMemoryGB" | bc)
export numvCPU numWorkers calculatedWorkerMemory

# Stop & Clean all containers
docker-compose down -v
docker network prune -f
docker rmi apache/hadoop:3 custom-spark:v1

find /mnt/spark_volume/poc/namenode_data -mindepth 1 ! -name 'empty.txt' -exec rm -rf {} +
find /mnt/spark_volume/poc/datanode_data1 -mindepth 1 ! -name 'empty.txt' -exec rm -rf {} +
find /mnt/spark_volume/poc/datanode_data2 -mindepth 1 ! -name 'empty.txt' -exec rm -rf {} +
find ./data/spark_master -mindepth 1 ! -name 'empty.txt' -exec rm -rf {} +
find ./data/spark_workers -mindepth 1 ! -name 'empty.txt' -exec rm -rf {} +
find ./data/notebooks -mindepth 1 ! -name 'empty.txt' -exec rm -rf {} +

