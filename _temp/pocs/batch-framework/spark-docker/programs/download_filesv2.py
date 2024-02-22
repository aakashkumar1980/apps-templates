from pyspark.sql import SparkSession
import boto3
import os

os.environ["APP_NAME"] = "DownloadFilesV2"
os.environ["AWS_ACCESS_KEY_ID"] = "AKIA4UGDRGS2XPQACYCW"
os.environ["AWS_SECRET_ACCESS_KEY"] = "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"
os.environ["BUCKET_NAME"] = "aakash-kumar-poc-batch-poc-testv2"
os.environ["HDFS_DEST_PATH"] = "hdfs://namenode:9000/poc/file-download/"

def list_files_in_bucket(bucket_name, prefix):
    s3 = boto3.client("s3", region_name='us-east-1')
    response = s3.list_objects_v2(Bucket=bucket_name, Prefix=prefix)
    files = [content["Key"] for content in response.get("Contents", []) if "Key" in content]
    return files

def create_spark_session():
    spark = SparkSession.builder \
        .appName(os.environ['APP_NAME']) \
        .config("spark.hadoop.fs.s3a.access.key", os.environ["AWS_ACCESS_KEY_ID"]) \
        .config("spark.hadoop.fs.s3a.secret.key", os.environ["AWS_SECRET_ACCESS_KEY"]) \
        .config("spark.hadoop.fs.s3a.endpoint", "s3.amazonaws.com") \
        .config("spark.speculation", "true") \
        .getOrCreate()
    sc = spark.sparkContext

    # dynamically fetch the "spark.executor.cores" configuration value
    executor_cores = int(sc.getConf().get("spark.executor.cores", "1"))
    # calculate threadsMax based on executor cores, assuming 2 threads per core
    threadsMax = executor_cores * 2
    # set the maximum connections to the same value as threadsMax for optimized S3 access
    connectionMaximum = threadsMax
    print(f"##### Setting S3A configurations: threadsMax={threadsMax}, connectionMaximum={connectionMaximum}")

    # Set essential S3 configurations
    spark.conf.set("spark.hadoop.fs.s3a.logging.level", "DEBUG")
    spark.conf.set("spark.hadoop.fs.s3a.connection.ssl.enabled", "true")
    spark.conf.set("spark.hadoop.fs.s3a.path.style.access", "true")

    spark.conf.set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
    spark.conf.set("spark.hadoop.fs.s3a.block.size", "268435456")  # Set to 256 MB
    spark.conf.set("spark.hadoop.fs.s3a.connection.maximum", str(connectionMaximum))
    spark.conf.set("spark.hadoop.fs.s3a.threads.max", str(threadsMax))
    
    spark.conf.set("spark.hadoop.fs.s3a.connection.timeout", "20000")
    spark.conf.set("spark.hadoop.fs.s3a.connection.establish.timeout", "5000")
    spark.conf.set("spark.hadoop.fs.s3a.socket.timeout", "20000")
    spark.conf.set("spark.hadoop.fs.s3a.max.retries", "10")
    spark.conf.set("spark.hadoop.fs.s3a.retry.limit", "10")
    spark.conf.set("spark.hadoop.fs.s3a.attempts.maximum", "10")

    # Configurations for downloads
    spark.conf.set("spark.hadoop.fs.s3a.read.ahead.range", "4194304")  # 4 MB
    spark.conf.set("spark.hadoop.fs.s3a.metadata.cache.enable", "true")
    spark.conf.set("spark.hadoop.fs.s3a.metadata.cache.ttl", "300000")  # 5 minutes

    # Configurations for uploads
    spark.conf.set("spark.hadoop.fs.s3a.fast.upload", "true")
    spark.conf.set("spark.hadoop.fs.s3a.fast.upload.buffer", "disk")
    spark.conf.set("spark.hadoop.fs.s3a.buffer.dir", "/mnt/tmp")

    spark.conf.set("spark.hadoop.fs.s3a.multipart.size", "104857600")  # 100 MB
    spark.conf.set("spark.hadoop.fs.s3a.multipart.threshold", "104857600")  # 100 MB

    spark.conf.set("spark.hadoop.fs.s3a.multipart.purge", "false")
    spark.conf.set("spark.hadoop.fs.s3a.multipart.purge.age", "86400")  # 1 day


    return spark


if __name__ == "__main__":
    print(f"##### Starting the application: {os.environ['APP_NAME']} #####")
    spark = create_spark_session()

    prefix = ""
    print(f"### Listing files in bucket: {os.environ['BUCKET_NAME']}, prefix: {prefix} ###")

    file_names = list_files_in_bucket(os.environ['BUCKET_NAME'], prefix)
    print(f"### Found {len(file_names)} files ###")
    for file_name in file_names:
        file_path = f"s3a://{os.environ['BUCKET_NAME']}/{file_name}"
        print(f"### Processing file: {file_path}")
        
        # adjust the read method according to your file format (e.g., read.text, read.csv)
        df = spark.read.csv(file_path)
        # example processing (here, just show the first few lines)
        df.show()

        print(f"### Writing file to HDFS: {os.environ['HDFS_DEST_PATH'] + file_path.split('/')[-1].replace('.csv', '.parquet')} ###")
        df.write.mode("overwrite").parquet(os.environ['HDFS_DEST_PATH'] + file_path.split('/')[-1].replace('.csv', '.parquet'))

    print("##### Application finished #####")
    spark.stop()    

