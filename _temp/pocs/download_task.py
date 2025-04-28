import pysftp
import subprocess
from src.db_utils import mark_completed, mark_in_progress, mark_failed

def run(job):
    partner = job["partner"]
    batch = job["batch"]
    cnopts = pysftp.CnOpts()
    cnopts.hostkeys = None

    with pysftp.Connection(partner["host"], username=partner["username"], password=partner["password"], port=partner["port"], cnopts=cnopts) as sftp:
        sftp.cwd(partner["outbox_path"])
        for f in batch:
            try:
                mark_in_progress(partner["name"], f)
                sftp.get(f, f"/tmp/{f}")
                subprocess.run(["hdfs", "dfs", "-put", f"/tmp/{f}", partner["hdfs_target"]])
                mark_completed(partner["name"], f)
            except Exception as e:
                mark_failed(partner["name"], f)
