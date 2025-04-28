import yaml
from src.sftp_utils import list_sftp_files
from src.db_utils import insert_file_records
from pyspark.sql import SparkSession

# Load partner configuration
with open("config/app_config.yml", "r") as f:
    config = yaml.safe_load(f)

# Initialize Spark
spark = SparkSession.builder \
    .appName("SFTPFileDownloader") \
    .config("spark.executor.instances", "20") \
    .config("spark.executor.cores", "2") \
    .getOrCreate()

batch_jobs = []

# List files and prepare batches
for partner in config["partners"]:
    files = list_sftp_files(partner)
    insert_file_records(partner["name"], files)  # Couchbase insert

    # Split into 11 batches
    batch_size = max(1, len(files) // 11)
    for i in range(0, len(files), batch_size):
        batch_jobs.append({
            "partner": partner,
            "batch": files[i:i + batch_size]
        })

# Spark parallel processing
rdd = spark.sparkContext.parallelize(batch_jobs)
rdd.foreach(lambda job: __import__('src.download_task').download_task.run(job))