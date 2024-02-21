from pyspark.sql import SparkSession
import boto3
import os

os.environ["AWS_ACCESS_KEY_ID"] = "AKIA4UGDRGS2XPQACYCW"
os.environ["AWS_SECRET_ACCESS_KEY"] = "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"

def list_files_in_bucket(bucket_name, prefix):
    s3 = boto3.client("s3")
    response = s3.list_objects_v2(Bucket=bucket_name, Prefix=prefix)
    files = [content["Key"] for content in response.get("Contents", []) if "Key" in content]
    return files

def main():
    print("##### Starting the application #####")
    spark = SparkSession.builder \
        .appName("ReadFilesFromS3OneByOne") \
        .config("spark.hadoop.fs.s3a.access.key", os.environ["AWS_ACCESS_KEY_ID"]) \
        .config("spark.hadoop.fs.s3a.secret.key", os.environ["AWS_SECRET_ACCESS_KEY"]) \
        .config("spark.hadoop.fs.s3a.endpoint", "s3.amazonaws.com") \
        .getOrCreate()

    bucket_name = "aakash-kumar-poc-batch-poc-test"
    prefix = ""
    print(f"### Listing files in bucket: {bucket_name}, prefix: {prefix} ###")

    file_names = list_files_in_bucket(bucket_name, prefix)
    print(f"### Found {len(file_names)} files ###")
    for file_name in file_names:
        file_path = f"s3a://{bucket_name}/{file_name}"
        print(f"### Processing file: {file_path}")
        
        # Adjust the read method according to your file format (e.g., read.text, read.csv)
        df = spark.read.csv(file_path)
        # Example processing (here, just show the first few lines)
        df.show()

    print("##### Application finished #####")
    spark.stop()

if __name__ == "__main__":
    main()

