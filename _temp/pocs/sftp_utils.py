import pysftp

def list_sftp_files(partner):
    cnopts = pysftp.CnOpts()
    cnopts.hostkeys = None
    with pysftp.Connection(partner["host"], username=partner["username"], password=partner["password"], port=partner["port"], cnopts=cnopts) as sftp:
        sftp.cwd(partner["outbox_path"])
        return sftp.listdir()
