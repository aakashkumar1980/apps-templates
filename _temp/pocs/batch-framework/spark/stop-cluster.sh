#!/bin/bash

numWorkers=10
# Start Spark Workers in the background
for ((i=1; i<=numWorkers; i++)); do
    export SPARK_IDENT_STRING=slave$i
    stop-slave.sh spark://ip-172-31-7-170.us-west-1.compute.internal:7077
done
sleep 5

export SPARK_IDENT_STRING=master
stop-master.sh