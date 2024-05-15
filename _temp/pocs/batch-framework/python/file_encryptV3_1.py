import time
import gnupg
import tempfile
import os

# Adjusted chunk size for reading the file (considering available memory)
chunk_size = 1024 * 1024 * 50  # 50 MB

def encrypt_file_with_gpg(input_file, output_file, public_key_file, temp_dir, chunk_size=chunk_size):
  # Initialize GPG
  gpg = gnupg.GPG(verbose=True)

  # Import the recipient's public key and extract fingerprints
  try:
    with open(public_key_file, 'rb') as key_file:
      recipients = gpg.import_keys(key_file.read())
    recipient_fingerprints = recipients.fingerprints
    if not recipient_fingerprints:
      raise ValueError("No valid fingerprints found in the public key file.")
  except Exception as e:
    print(f"Failed to import public key: {e}")
    return

  try:
    with open(input_file, 'rb') as input_stream:
      with tempfile.NamedTemporaryFile(delete=False, dir=temp_dir) as temp_file:
        while True:
          # Read a chunk of data from the input file
          chunk = input_stream.read(chunk_size)
          if not chunk:
            break
          # Write the chunk to the temporary file
          temp_file.write(chunk)

        # Ensure the temporary file is closed before re-opening for reading
        temp_file.close()
        print('Encrypting file...')
        with open(temp_file.name, 'rb') as temp_file_stream:
          encrypted_data = gpg.encrypt_file(temp_file_stream, recipients=recipient_fingerprints, output=output_file)
          if not encrypted_data.ok:
            print("Encryption failed:", encrypted_data.status)
            os.remove(temp_file.name)
            return
  except Exception as e:
    print(f"Failed to encrypt file: {e}")
  finally:
    # Cleanup the temporary file
    if os.path.exists(temp_file.name):
      os.remove(temp_file.name)

# USAGE:
# to generate a PGP key, run the following command:
# $ gpg --full-gen-key --output $keyname.gpg
# ~/.gnupg/pubring.kbx is the single file containing the private and public keys
# next, to export the public key (used in encryption), run the following command:
# $ gpg --export --armor "$RecipientName/ID/emailAddress" > _data/pgp_public_key.asc
# $ gpg --export-secret-keys --armor "$RecipientName/ID/emailAddress" > pgp_private_key.asc
# $ pip install python-gnupg
#################
### MAIN CODE ###
#################
temp_dir = '/mnt/ebs_volume/tmp/_data'
if __name__ == "__main__":
  input_file = '/mnt/ebs_volume/tmp/_data/customers-128000000.csv'
  output_file = input_file + '.gpg'
  public_key_file = '/mnt/ebs_volume/tmp/_data/pgp_public_key.asc'
  start_time = time.time()
  encrypt_file_with_gpg(input_file, output_file, public_key_file, temp_dir)
  end_time = time.time()

  print("Execution time:", time.strftime('%H:%M:%S', time.gmtime(end_time - start_time)))



# RESULTS #:
## File: customers-128000000.csv (22.4 GB size ->   GB encrypted)
#### CPU: 16 cores | 32 vCPU (% usage)
#### RAM:
#### Time: