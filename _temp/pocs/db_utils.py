from couchbase.cluster import Cluster, ClusterOptions
from couchbase.auth import PasswordAuthenticator
from couchbase.collection import GetOptions
from datetime import datetime
import uuid

# Setup connection
cluster = Cluster("couchbase://localhost", ClusterOptions(
    PasswordAuthenticator("Administrator", "password")))
bucket = cluster.bucket("sftp")
collection = bucket.default_collection()

def insert_file_records(partner_name, files):
    for file_name in files:
        doc_id = f"{partner_name}_{file_name}"
        try:
            collection.insert(doc_id, {
                "type": "file_status",
                "partner": partner_name,
                "file_name": file_name,
                "status": "PENDING",
                "attempt_count": 0,
                "last_updated": datetime.utcnow().isoformat()
            })
        except Exception as e:
            pass  # Already exists or other error

def mark_status(partner, file_name, status):
    doc_id = f"{partner}_{file_name}"
    try:
        doc = collection.get(doc_id).content_as[dict]
        doc["status"] = status
        if status == "IN_PROGRESS" or status == "FAILED":
            doc["attempt_count"] += 1
        doc["last_updated"] = datetime.utcnow().isoformat()
        collection.replace(doc_id, doc)
    except Exception as e:
        print(f"Error updating file status: {e}")

def mark_completed(partner, file_name):
    mark_status(partner, file_name, "COMPLETED")

def mark_in_progress(partner, file_name):
    mark_status(partner, file_name, "IN_PROGRESS")

def mark_failed(partner, file_name):
    mark_status(partner, file_name, "FAILED")