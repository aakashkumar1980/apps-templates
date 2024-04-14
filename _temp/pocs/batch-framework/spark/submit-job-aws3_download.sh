#!/bin/bash

# Spark Submit (TEST ON: M6A.8XLARGE | 32 vCPU | 128 GB Memory | 12.5 Gibps [1.56 GBps] Network)
# Optimization Rules:
# - [per Node Level] Reserve 1 core and 1GB RAM for Hadoop/YARN/OS domains.
# - [Cluster Level] Reserve an additional 1 core and 1GB RAM for the YARN application manager.
# - [Executor Level] Assign 4 cores to each executor.
# - [Executor Level] Subtract 10% of calculated memory for overhead.

# Reservations
reservedCoresPerNode=1    # Reserved cores per node for system
reservedMemoryPerNode=1   # Reserved memory per node in GB for system
reservedCoresCluster=1    # Reserved cores at cluster level for YARN Application Manager
reservedMemoryCluster=1   # Reserved memory at cluster level in GB for YARN Application Manager

# Calculations for resources available for Spark
availableCores=$(echo "32 - $reservedCoresPerNode - $reservedCoresCluster" | bc)
availableMemory=$(echo "128 - $reservedMemoryPerNode - $reservedMemoryCluster" | bc)
# Executor configurations
numWorkers=6
executorCores=4   # Assigning 4 cores to each executor as per rule
executorMemoryGB=$(echo "0.9 * ($availableMemory / $numWorkers)" | bc | awk '{print int($1+0.5)}') # Subtracting 10% for memory overhead

spark-submit --class com.aadityadesigners.poc.s3filesdownload.S3FileDownloadApp \
  --master spark://ip-172-31-7-170.us-west-1.compute.internal:7077 \
  --num-executors $numWorkers \
  --executor-cores $executorCores \
  --executor-memory ${executorMemoryGB}G \
  --conf spark.executor.memoryOverhead=$(echo "0.1 * $executorMemoryGB" | bc | awk '{print int($1+0.5)}')M \
  --conf spark.dynamicAllocation.enabled=false \
  --conf spark.default.parallelism=$((numWorkers * executorCores)) \
  --conf spark.sql.shuffle.partitions=$((numWorkers * executorCores)) \
  --driver-memory 2G \
  build/libs/app-1.0.0.jar


# DEPRECATED #
#numvCPU=4
#numMemoryGB=6
#numWorkers=6
#executorMemoryGB=$(echo "0.75 * $numMemoryGB" | bc | awk '{print int($1+0.5)}')
#spark-submit --class com.aadityadesigners.poc.s3filesdownload.S3FileDownloadApp \
#  --master spark://ip-172-31-7-170.us-west-1.compute.internal:7077 \
#  --num-executors $numWorkers \
#  --executor-cores $((numvCPU-1)) \
#  --executor-memory ${executorMemoryGB}G \
#  --conf spark.executor.memoryOverhead=512M \
#  --conf spark.dynamicAllocation.enabled=false \
#  --conf spark.default.parallelism=$((numWorkers * (numvCPU-1))) \
#  --conf spark.sql.shuffle.partitions=$((numWorkers * (numvCPU-1))) \
#  --driver-memory 2G \
#  build/libs/app-1.0.0.jar
 
