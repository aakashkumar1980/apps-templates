import os
import time
import gnupg
from multiprocessing import Pool

def encrypt_chunk(chunk, recipients):
  gpg = gnupg.GPG()
  encrypted_data = gpg.encrypt(chunk, recipients=recipients.fingerprints, armor=False)
  if not encrypted_data.ok:
    raise Exception(f"Encryption failed: {encrypted_data.status}")
  return encrypted_data.data

chunk_size = 1024*1024*500 # 500MB
pool_size = os.cpu_count()
def encrypt_file_with_gpg(input_file, output_file, public_key_file, chunk_size=chunk_size, pool_size=pool_size):
  print("pool_size:", pool_size)
  gpg = gnupg.GPG()
  with open(public_key_file, 'rb') as key_file:
    recipients = gpg.import_keys(key_file.read())
  with open(input_file, 'rb') as input_stream:
    with open(output_file, 'wb') as output_stream:
      pool = Pool(pool_size)
      while True:
        chunk = input_stream.read(chunk_size)
        if not chunk:
          break
        encrypted_chunk = pool.apply_async(encrypt_chunk, args=(chunk, recipients))
        output_stream.write(encrypted_chunk.get())
      pool.close()
      pool.join()



# to generate a PGP key, run the following command:
# $ gpg --full-gen-key --output $keyname.gpg
# ~/.gnupg/pubring.kbx is the single file containing the private and public keys
# next, to export the public key (used in encryption), run the following command:
# $ gpg --export --armor "$RecipientName/ID/emailAddress" > _data/pgp_public_key.asc
#################
### MAIN CODE ###
#################
if __name__ == "__main__":
  input_file = './_data/customers-256000000.csv'
  output_file = input_file + '.gpg'
  public_key_file = './_data/pgp_public_key.asc'
  start_time = time.time()
  encrypt_file_with_gpg(input_file, output_file, public_key_file)
  end_time = time.time()

  print("Execution time:", time.strftime('%H:%M:%S', time.gmtime(end_time - start_time)))


# RESULTS #:
## File: customers-256000000.csv (44.7 GB size)
### CPU: 8 cores | 16 vCPU (50% usage)
#### RAM: 7.9 GB -
#### Execution time (MM:HH:SS):
