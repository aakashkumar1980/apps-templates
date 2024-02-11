#!/bin/bash

# Start Spark Master in the background
export SPARK_IDENT_STRING=master
start-master.sh &
sleep 10

numWorkers=3
# Start Spark Workers in the background
for ((i=1; i<=numWorkers; i++)); do
    export SPARK_IDENT_STRING=worker$i
    start-worker.sh --cores 2 --memory 2G spark://ip-172-31-7-170.us-west-1.compute.internal:7077 &
    sleep 5
done