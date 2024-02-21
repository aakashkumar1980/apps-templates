#!/bin/bash

# Start by executing the first command without launching an interactive shell
docker exec spark-docker-namenode-1 /bin/bash -c "hadoop fs -mkdir -p /poc/file-download"

# Then, execute the second command
docker exec spark-docker-namenode-1 /bin/bash -c "hdfs dfs -chmod -R 777 /poc"


