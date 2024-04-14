#!/bin/bash

### SPARK SUBMIT (Configuration for an example cluster environment) ###
# Configuration of machine resources
totalNodes=5
corePerNode=12
memoryPerNode=48 # Memory per node in GB

# Optimization Rules:
# - [per Node Level] Reserve 1 core and 1GB RAM for Hadoop/YARN/OS domains.
# - [Cluster Level] Reserve an additional 1 core and 1GB RAM for the YARN application manager.
# - [Executor Level] Assign 4 cores to each executor.
# - [Executor Level] Subtract 10% of its calculated memory for memory overhead compensation.
# Reservations
reservedCoresPerNode=1    # Reserved cores per node for system
reservedMemoryPerNode=1   # Reserved memory per node in GB for system
reservedCoresCluster=1    # Reserved cores at cluster level for YARN Application Manager
reservedMemoryCluster=1   # Reserved memory at cluster level in GB for YARN Application Manager
# Calculations for resources available for Spark
availableCoresPerNode=$(echo "$corePerNode - $reservedCoresPerNode" | bc)
totalAvailableCores=$(echo "$totalNodes * $availableCoresPerNode - $reservedCoresCluster" | bc)
availableMemoryPerNode=$(echo "$memoryPerNode - $reservedMemoryPerNode" | bc)
totalAvailableMemory=$(echo "$totalNodes * $availableMemoryPerNode - $reservedMemoryCluster" | bc)
# Executor configurations
executorCores=4   # Assigning 4 cores to each executor as per rule
executorsPerNode=$(echo "$availableCoresPerNode / $executorCores" | bc)
numExecutors=$(echo "$totalNodes * $executorsPerNode" | bc)  # Total executors across the cluster
executorMemoryGB=$(echo "0.9 * ($totalAvailableMemory / $numExecutors)" | bc | awk '{print int($1+0.5)}') # Subtracting 10% for memory overhead

spark-submit --class com.aadityadesigners.poc.s3filesdownload.S3FileDownloadApp \
  --master spark://ip-172-31-7-170.us-west-1.compute.internal:7077 \
  --num-executors $numExecutors \
  --executor-cores $executorCores \
  --executor-memory ${executorMemoryGB}G \
  --conf spark.executor.memoryOverhead=$(echo "0.1 * $executorMemoryGB" | bc | awk '{print int($1+0.5)}')M \
  --conf spark.dynamicAllocation.enabled=false \
  --conf spark.default.parallelism=$((numExecutors * executorCores)) \
  --conf spark.sql.shuffle.partitions=$((numExecutors * executorCores)) \
  --driver-memory 2G \
  build/libs/app-1.0.0.jar





# DEPRECATED #
#numvCPU=4
#numMemoryGB=6
#numExecutors=6
#executorMemoryGB=$(echo "0.75 * $numMemoryGB" | bc | awk '{print int($1+0.5)}')
#spark-submit --class com.aadityadesigners.poc.s3filesdownload.S3FileDownloadApp \
#  --master spark://ip-172-31-7-170.us-west-1.compute.internal:7077 \
#  --num-executors $numExecutors \
#  --executor-cores $((numvCPU-1)) \
#  --executor-memory ${executorMemoryGB}G \
#  --conf spark.executor.memoryOverhead=512M \
#  --conf spark.dynamicAllocation.enabled=false \
#  --conf spark.default.parallelism=$((numExecutors * (numvCPU-1))) \
#  --conf spark.sql.shuffle.partitions=$((numExecutors * (numvCPU-1))) \
#  --driver-memory 2G \
#  build/libs/app-1.0.0.jar
 
