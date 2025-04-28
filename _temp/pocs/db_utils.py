import psycopg2

def insert_file_records(partner_name, files):
    conn = psycopg2.connect(database="sftp", user="postgres", password="pass", host="localhost", port="5432")
    cur = conn.cursor()
    for f in files:
        cur.execute("INSERT INTO file_download_status (partner_name, file_name, status) VALUES (%s, %s, %s) ON CONFLICT DO NOTHING", (partner_name, f, "PENDING"))
    conn.commit()
    conn.close()

def mark_in_progress(partner, file):
    conn = psycopg2.connect(database="sftp", user="postgres", password="pass", host="localhost", port="5432")
    cur = conn.cursor()
    cur.execute("UPDATE file_download_status SET status='IN_PROGRESS', attempt_count = attempt_count + 1 WHERE partner_name=%s AND file_name=%s", (partner, file))
    conn.commit()
    conn.close()

def mark_completed(partner, file):
    conn = psycopg2.connect(database="sftp", user="postgres", password="pass", host="localhost", port="5432")
    cur = conn.cursor()
    cur.execute("UPDATE file_download_status SET status='COMPLETED', last_updated=now() WHERE partner_name=%s AND file_name=%s", (partner, file))
    conn.commit()
    conn.close()

def mark_failed(partner, file):
    conn = psycopg2.connect(database="sftp", user="postgres", password="pass", host="localhost", port="5432")
    cur = conn.cursor()
    cur.execute("UPDATE file_download_status SET status='FAILED' WHERE partner_name=%s AND file_name=%s", (partner, file))
    conn.commit()
    conn.close()
