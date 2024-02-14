#!/bin/bash

# WORKER NODES
numWorkers=25
# Start Spark Workers in the background
for ((i=1; i<=numWorkers; i++)); do
    export SPARK_IDENT_STRING=slave$i
    stop-slave.sh spark://ip-172-31-7-170.us-west-1.compute.internal:7077
done
sleep 5

# MASTER
export SPARK_IDENT_STRING=master
stop-master.sh


# HADFS and YARN
stop-dfs.sh
stop-yarn.sh
sudo rm -f /opt/hadoop/2.7.7/logs/hadoop-ubuntu-*
sudo rm -f /opt/hadoop/2.7.7/logs/yarn-ubuntu-*
# $ ls -l /opt/hadoop/2.7.7/logs/


# CLEAN-UP
echo "Cleaning up..."
rm -rf $SPARK_HOME/work/*
rm -rf /tmp/spark-*
rm -rf $SPARK_HOME/logs/*
rm -rf ~/.gradle/caches/
rm -rf ~/.gradle/wrapper/dists/