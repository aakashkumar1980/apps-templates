#!/bin/bash

### SPARK SUBMIT (Configuration for an example cluster environment) ###
# Configuration of machine resources (SAMPLE VALUES HERE, Please update with actual values)
totalNodes=3
corePerNode=16
memoryPerNode=48 # Memory per node in GB

# Optimization Rules:
# - [Per Node Level] Reserve 1 core and 1GB RAM for Hadoop/YARN/OS domains.
# - [Cluster Level] Reserve an additional 1 core and 1GB RAM for the YARN application manager.

# Reservations
reservedCoresPerNode=1    # Reserved cores per node for system
reservedMemoryPerNode=1   # Reserved memory per node in GB for system
reservedCoresCluster=1    # Reserved cores at cluster level for YARN Application Manager
reservedMemoryCluster=1   # Reserved memory at cluster level in GB for YARN Application Manager

# Calculations for resources available for Spark
# Step 1: Calculate available cores and memory per node after per node reservations
availableCoresPerNode=$(echo "$corePerNode - $reservedCoresPerNode" | bc)
availableMemoryPerNode=$(echo "$memoryPerNode - $reservedMemoryPerNode" | bc)

# Step 2: Calculate total resources available at cluster level before YARN manager reservations
totalCoreCluster=$(echo "$totalNodes * $availableCoresPerNode" | bc)
totalMemoryCluster=$(echo "$totalNodes * $availableMemoryPerNode" | bc)

# Step 3: Subtract cluster level reservations for YARN Application Manager
totalAvailableCores=$(echo "$totalCoreCluster - $reservedCoresCluster" | bc)
totalAvailableMemory=$(echo "$totalMemoryCluster - $reservedMemoryCluster" | bc)

# Executor configurations
executorCores=4   # Assigning 4 cores to each executor as per rule
numExecutors=$(echo "$totalAvailableCores / $executorCores" | bc)  # Calculate total executors across the cluster
# Step 4: Calculate memory allocation per executor and subtract 10% for overhead
initialExecutorMemory=$(echo "$totalAvailableMemory / $numExecutors" | bc -l)
executorMemoryGB=$(echo "$initialExecutorMemory * 0.9" | bc | awk '{print int($1)}') # Subtracting 10% for memory overhead
memoryOverheadMB=$(echo "$initialExecutorMemory * 0.1" | bc | awk '{print int($1 * 1024)}') # Calculating memory overhead in MB

spark-submit --class com.aadityadesigners.poc.s3filesdownload.S3FileDownloadApp \
  --master spark://ip-172-31-7-170.us-west-1.compute.internal:7077 \
  --num-executors $numExecutors \
  --executor-cores $executorCores \
  --executor-memory ${executorMemoryGB}G \
  --conf spark.executor.memoryOverhead=${memoryOverheadMB}M \
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
 
