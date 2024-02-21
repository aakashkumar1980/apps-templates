from pyspark.sql import SparkSession

print("##### Creating Spark session... #####")
spark = SparkSession.builder \
  .appName("HelloWorldToHDFSv1.1") \
  .config("spark.hadoop.fs.defaultFS", "hdfs://namenode:9000") \
  .config("spark.master", "spark://spark-master:7077") \
  .getOrCreate()

print("### Writing Hello World to HDFS... ###")
df = spark.createDataFrame([("Hello World",)], ["text"])
df.write.mode("overwrite").text("hdfs://namenode:9000/poc/file-download/hello_world1.1.txt")

print("##### Stopping Spark session... #####")
spark.stop()
