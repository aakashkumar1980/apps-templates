from pyspark.sql import SparkSession
import boto3
import os

os.environ["APP_NAME"] = "DownloadFilesV1"
os.environ["AWS_ACCESS_KEY_ID"] = "AKIA4UGDRGS2XPQACYCW"
os.environ["AWS_SECRET_ACCESS_KEY"] = "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"
os.environ["BUCKET_NAME"] = "aakash-kumar-poc-batch-poc-testv2"
os.environ["HDFS_DEST_PATH"] = "hdfs://namenode:9000/poc/file-download/"

def list_files_in_bucket(bucket_name, prefix):
    s3 = boto3.client("s3", region_name='us-east-1')
    response = s3.list_objects_v2(Bucket=bucket_name, Prefix=prefix)
    files = [content["Key"] for content in response.get("Contents", []) if "Key" in content]
    return files

def main():
    print(f"##### Starting the application: {os.environ['APP_NAME']} #####")
    spark = SparkSession.builder \
        .appName(os.environ['APP_NAME']) \
        .config("spark.hadoop.fs.s3a.access.key", os.environ["AWS_ACCESS_KEY_ID"]) \
        .config("spark.hadoop.fs.s3a.secret.key", os.environ["AWS_SECRET_ACCESS_KEY"]) \
        .config("spark.hadoop.fs.s3a.endpoint", "s3.amazonaws.com") \
        .getOrCreate()

    prefix = ""
    print(f"### Listing files in bucket: {os.environ['BUCKET_NAME']}, prefix: {prefix} ###")

    file_names = list_files_in_bucket(os.environ['BUCKET_NAME'], prefix)
    print(f"### Found {len(file_names)} files ###")
    for file_name in file_names:
        file_path = f"s3a://{os.environ['BUCKET_NAME']}/{file_name}"
        print(f"### Processing file: {file_path}")
        
        # Adjust the read method according to your file format (e.g., read.text, read.csv)
        df = spark.read.csv(file_path)
        # Example processing (here, just show the first few lines)
        df.show()

        print(f"### Writing file to HDFS: {os.environ['HDFS_DEST_PATH'] + file_path.split('/')[-1].replace('.csv', '.parquet')} ###")
        df.write.mode("overwrite").parquet(os.environ['HDFS_DEST_PATH'] + file_path.split('/')[-1].replace('.csv', '.parquet'))

    print("##### Application finished #####")
    spark.stop()

if __name__ == "__main__":
    main()

