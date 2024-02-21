#!/bin/bash

# Create HDFS directories
docker exec spark-docker-namenode-1 /bin/bash -c "hadoop fs -mkdir -p /poc/file-download"
docker exec spark-docker-namenode-1 /bin/bash -c "hdfs dfs -chmod -R 777 /poc"

# Copy the program file to the master node
docker exec spark-docker-spark-master-1 rm -rf /var/poc-workspace/downloads3files.py
docker cp ./programs/downloads3files.py spark-docker-spark-master-1:/var/poc-workspace
# Next, we will run the program to download the files from S3 to HDFS
docker exec spark-docker-spark-master-1 /bin/bash -c "spark-submit \
    --master spark://spark-master:7077 \
    --conf spark.executorEnv.PYSPARK_PYTHON=/opt/bitnami/spark \
    /var/poc-workspace/downloads3files.py"


