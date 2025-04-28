import pysftp
import subprocess
from src.db_utils import mark_completed, mark_in_progress, mark_failed

def run(job):
    partner = job["partner"]
    batch = job["batch"]
    cnopts = pysftp.CnOpts()
    cnopts.hostkeys = None

    try:
        with pysftp.Connection(partner["host"], username=partner["username"],
                               password=partner["password"], port=partner["port"], cnopts=cnopts) as sftp:
            sftp.cwd(partner["outbox_path"])

            for file_name in batch:
                try:
                    mark_in_progress(partner["name"], file_name)
                    local_path = f"/tmp/{partner['name']}_{file_name}"
                    sftp.get(file_name, local_path)

                    # Put to HDFS
                    subprocess.run(["hdfs", "dfs", "-put", local_path, partner["hdfs_target"]], check=True)

                    mark_completed(partner["name"], file_name)
                except Exception as e:
                    print(f"[ERROR] File: {file_name} - {str(e)}")
                    mark_failed(partner["name"], file_name)
    except Exception as e:
        print(f"[FATAL] Connection failed for {partner['name']}: {e}")