#!/bin/bash

# copy the program file to the master node
docker exec spark-docker-spark-master-1 bash -c '[ -e /var/poc-workspace/download_files.py ] && rm -rf /var/poc-workspace/download_files.py || echo "File does not exist, nothing to remove."'
docker cp ./download_files.py spark-docker-spark-master-1:/var/poc-workspace
# next, we will run the program to download the files from S3 to HDFS
docker exec spark-docker-spark-master-1 /bin/bash -c "spark-submit \
    --master spark://spark-master:7077 \
    --conf spark.executorEnv.PYSPARK_PYTHON=/opt/bitnami/spark \
    /var/poc-workspace/download_files.py"