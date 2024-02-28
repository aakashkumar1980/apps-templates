import os
import sys
import boto3
from pyspark.sql import SparkSession

current_dir = os.path.dirname(os.path.abspath(__file__)) # directory of the current file
parent_dir = os.path.dirname(current_dir) # parent directory
sys.path.append(os.path.join(parent_dir, "utils")) # add the utils directory to the system path
from s3_optimization_configs import apply_configs


os.environ["APP_NAME"] = "DownloadFilesV2"
os.environ["AWS_ACCESS_KEY_ID"] = "AKIA4UGDRGS2XPQACYCW"
os.environ["AWS_SECRET_ACCESS_KEY"] = "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"
os.environ["AWS_REGION"] = "us-east-1"

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
    spark = apply_configs(spark, connectionMaximum, threadsMax)

    return spark


if __name__ == "__main__":
    os.environ["BUCKET_NAME"] = "aakash-kumar-poc-batch-poc-testv3"
    os.environ["FILE_SOURCE_PATH"] = "{current_dir}/_temp/"  

    print(f"##### Starting the application: {os.environ['APP_NAME']} #####")
    spark = create_spark_session()

    s3 = boto3.client("s3", region_name=os.environ["AWS_REGION"])
    for root, dirs, files in os.walk(os.environ["FILE_SOURCE_PATH"]):
        for file in files:
            print(f"##### Uploading file: {file} #####")
            file_path = os.path.join(root, file)
            file_name = file_path.split("/")[-1]
            s3.upload_file(file_path, os.environ["BUCKET_NAME"], file_name)
            print(f"##### File uploaded: {file_name} #####")



    print("##### Application finished #####")
    spark.stop()    

