#!/bin/bash

# Create HDFS directories
docker exec spark-docker-namenode-1 /bin/bash -c "hadoop fs -mkdir -p /poc/file-download"
docker exec spark-docker-namenode-1 /bin/bash -c "hdfs dfs -chmod -R 777 /poc"

# Parquet-tools JAR
docker exec spark-docker-namenode-1 /bin/bash -c "\
mkdir -p /opt/parquet-tools && \
curl -L https://repo1.maven.org/maven2/org/apache/parquet/parquet-tools/1.11.2/parquet-tools-1.11.2.jar -o /opt/parquet-tools/parquet-tools.jar && \
chmod +x /opt/parquet-tools/parquet-tools.jar"
