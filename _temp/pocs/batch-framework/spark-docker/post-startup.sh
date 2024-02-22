#!/bin/bash

# Create HDFS directories
docker exec spark-docker-namenode-1 /bin/bash -c "hadoop fs -mkdir -p /poc/file-download"
docker exec spark-docker-namenode-1 /bin/bash -c "hdfs dfs -chmod -R 777 /poc"



