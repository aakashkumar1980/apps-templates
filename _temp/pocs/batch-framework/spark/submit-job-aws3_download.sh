#!/bin/bash

# Spark Submit (TEST ON: M6A.16XLARGE | 64 vCPU | 256 GB Memory | 25 Gbps Network)
numvCPU=2
numMemoryGB=4
numWorkers=2
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
 