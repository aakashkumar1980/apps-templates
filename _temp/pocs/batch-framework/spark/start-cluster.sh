#!/bin/bash

numvCPU=4
numMemoryGB=6
numWorkers=6
# MASTER
export SPARK_IDENT_STRING=master
start-master.sh &
sleep 10

# WORKER NODES
for ((i=1; i<=numWorkers; i++)); do
    echo "Starting slave$i"
    export SPARK_IDENT_STRING=slave$i
    start-slave.sh --cores $numvCPU --memory ${numMemoryGB}G spark://ip-172-31-7-170.us-west-1.compute.internal:7077 &
    sleep 2
done


# HDFS and YARN
start-dfs.sh
start-yarn.sh
# $ jps
# $ ls -l /opt/hadoop/2.7.7/logs/

 