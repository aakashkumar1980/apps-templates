from pyspark.sql import SparkSession
spark = SparkSession.builder \
    .appName("HelloWorldToHDFSv10") \
    .config("spark.hadoop.fs.defaultFS", "hdfs://namenode:9000") \
    .getOrCreate()
df = spark.createDataFrame([("Hello World",)], ["text"])
df.write.mode("overwrite").text("hdfs://namenode:9000/poc/file-download/hello_world10.txt")
spark.stop()
