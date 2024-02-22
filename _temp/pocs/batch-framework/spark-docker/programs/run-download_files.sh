#!/bin/bash

###############################
##### EXECUTE THE PROGRAM #####
###############################
# clean up the HDFS directory
docker exec spark-docker-namenode-1 /bin/bash -c "hadoop fs -rm -r -f /poc/file-download/*"

# copy the program file to the master node
docker exec spark-docker-spark-master-1 bash -c '[ -e /var/poc-workspace/download_files.py ] && rm -rf /var/poc-workspace/download_files.py || echo "File does not exist, nothing to remove."'
docker cp ./download_files.py spark-docker-spark-master-1:/var/poc-workspace
# next, we will run the program to download the files from S3 to HDFS
docker exec spark-docker-spark-master-1 /bin/bash -c "spark-submit \
    --master spark://spark-master:7077 \
    --conf spark.executorEnv.PYSPARK_PYTHON=/opt/bitnami/spark \
    /var/poc-workspace/download_files.py"


### VERIFICATION ###
# check the HDFS directory to see if the files were downloaded
docker exec spark-docker-namenode-1 /bin/bash -c "hadoop fs -ls /poc/file-download"

docker exec spark-docker-namenode-1 /bin/bash -c " \
  hadoop fs -get /poc/file-download/dataV3.parquet/part-00000-*.snappy.parquet /tmp/temp_parquet_file.parquet"
docker exec spark-docker-namenode-1 /bin/bash -c " \
    java -cp /opt/parquet-tools/parquet-tools.jar:/opt/hadoop/share/hadoop/common/*:/opt/hadoop/share/hadoop/common/lib/*:/opt/hadoop/share/hadoop/mapreduce/*:/opt/hadoop/share/hadoop/mapreduce/lib/* \
    org.apache.parquet.tools.Main head -n 3 /tmp/temp_parquet_file.parquet"
docker exec spark-docker-namenode-1 /bin/bash -c " \
    rm -rf /tmp/temp_parquet_file.parquet"
        

