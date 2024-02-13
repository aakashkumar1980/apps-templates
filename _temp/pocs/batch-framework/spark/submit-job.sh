#!/bin/bash

# Spark Submit (TEST ON: MD5.12XLARGE)
# $ start-dfs.sh
# $ start-yarn.sh
# $ ls -l /opt/hadoop/2.7.7/logs/
numvCPU=4
numMemoryGB=8
numWorkers=4
executorMemoryGB=$(echo "0.75 * $numMemoryGB" | bc | awk '{print int($1+0.5)}')
spark-submit --class com.aadityadesigners.poc.fileupdate.FileColumnUpdateApp \
  --master spark://ip-172-31-7-170.us-west-1.compute.internal:7077 \
  --num-executors $numWorkers \
  --executor-cores $((numvCPU-1)) \ # NOTE: this is one executor per worker node. (multiple executors per worker node is good for high CPU workloads)
  --executor-memory ${executorMemoryGB}G \
  --conf spark.executor.memoryOverhead=512M \
  --conf spark.dynamicAllocation.enabled=false \
  --conf spark.default.parallelism=$((numWorkers * (numvCPU-1))) \
  --conf spark.sql.shuffle.partitions=$((numWorkers * (numvCPU-1))) \
  --driver-memory 2G \
  build/libs/app-1.0.0.jar
 