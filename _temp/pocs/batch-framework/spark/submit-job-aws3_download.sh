#!/bin/bash

# Spark Submit (TEST ON: M6A.8XLARGE | 32 vCPU | 128 GB Memory | 12.5 Gibps [13.42 GBps] Network)
numvCPU=4
numMemoryGB=6
numWorkers=6
executorMemoryGB=$(echo "0.75 * $numMemoryGB" | bc | awk '{print int($1+0.5)}')
spark-submit --class com.aadityadesigners.poc.s3filesdownload.S3FileDownloadApp \
  --master spark://ip-172-31-7-170.us-west-1.compute.internal:7077 \
  --num-executors $numWorkers \
  --executor-cores $((numvCPU-1)) \
  --executor-memory ${executorMemoryGB}G \
  --conf spark.executor.memoryOverhead=512M \
  --conf spark.dynamicAllocation.enabled=false \
  --conf spark.default.parallelism=$((numWorkers * (numvCPU-1))) \
  --conf spark.sql.shuffle.partitions=$((numWorkers * (numvCPU-1))) \
  --driver-memory 2G \
  build/libs/app-1.0.0.jar
 