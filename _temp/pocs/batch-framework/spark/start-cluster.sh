#!/bin/bash

# Start Spark Master in the background
export SPARK_IDENT_STRING=master
start-master.sh &
sleep 10

numWorkers=3
# Start Spark Workers in the background
for ((i=1; i<=numWorkers; i++)); do
    echo "Starting slave$i"
    export SPARK_IDENT_STRING=slave$i
    start-slave.sh --cores 2 --memory 3G spark://ip-172-31-7-170.us-west-1.compute.internal:7077 &
    sleep 2
done

# Spark Submit (TEST ON: MD5.12XLARGE)
#spark-submit --class com.aadityadesigners.poc.fileupdate.FileColumnUpdateApp \
#  --master spark://ip-172-31-7-170.us-west-1.compute.internal:7077 \
#  --executor-memory 2560M \
#  --conf "spark.executor.memoryOverhead=512M" \
#  --driver-memory 2G \
#  build/libs/app-1.0.0.jar  