#!/bin/bash

# Start Spark Master in the background
export SPARK_IDENT_STRING=master
start-master.sh &
sleep 10


numvCPU=4
numMemoryGB=8
numWorkers=4
# Start Spark Workers in the background
for ((i=1; i<=numWorkers; i++)); do
    echo "Starting slave$i"
    export SPARK_IDENT_STRING=slave$i
    start-slave.sh --cores $numvCPU --memory ${numMemoryGB}G spark://ip-172-31-7-170.us-west-1.compute.internal:7077 &
    sleep 2
done

 