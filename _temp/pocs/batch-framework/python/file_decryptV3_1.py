import time
import gnupg
import tempfile
import os

# Chunk size for reading the file
chunk_size = 1024 * 1024 * 50  # 50 MB

def decrypt_file_with_gpg(encrypted_file, output_file, private_key_file, passphrase, temp_dir, chunk_size=chunk_size):
  # Initialize GPG
  gpg = gnupg.GPG(verbose=True)

  # Import the private key
  try:
    with open(private_key_file, 'rb') as key_file:
      result = gpg.import_keys(key_file.read())
    if not result:
      raise ValueError("Failed to import private key.")
  except Exception as e:
    print(f"Failed to import private key: {e}")
    return

  try:
    with open(encrypted_file, 'rb') as encrypted_stream:
      with tempfile.NamedTemporaryFile(delete=False, dir=temp_dir) as temp_file:
        while True:
          # Read a chunk of data from the encrypted file
          chunk = encrypted_stream.read(chunk_size)
          if not chunk:
            break
          # Write the chunk to the temporary file
          temp_file.write(chunk)

        # Ensure the temporary file is closed before re-opening for reading
        temp_file.close()
        print('Decrypting file...')
        with open(temp_file.name, 'rb') as temp_file_stream:
          decrypted_data = gpg.decrypt_file(temp_file_stream, passphrase=passphrase, output=output_file)
          if not decrypted_data.ok:
            print("Decryption failed:", decrypted_data.status)
            os.remove(temp_file.name)
            return
  except Exception as e:
    print(f"Failed to decrypt file: {e}")
  finally:
    # Cleanup the temporary file
    if os.path.exists(temp_file.name):
      os.remove(temp_file.name)

# USAGE:
#################
### MAIN CODE ###
#################
temp_dir = '/mnt/ebs_volume/tmp/_data'
if __name__ == "__main__":
  encrypted_file = '/mnt/ebs_volume/tmp/_data/customers-128000000.csv.gpg'
  output_file = encrypted_file.replace('.csv.gpg', '_decrypted.csv')
  private_key_file = '/mnt/ebs_volume/tmp/_data/pgp_private_key.asc'
  passphrase = 'apple26j'
  start_time = time.time()
  decrypt_file_with_gpg(encrypted_file, output_file, private_key_file, passphrase, temp_dir)
  end_time = time.time()

  print("Execution time:", time.strftime('%H:%M:%S', time.gmtime(end_time - start_time)))



# RESULTS #:
## File: customers-128000000.csv.gpg ( GB size ->   GB decrypted)
#### CPU: 16 cores | 32 vCPU (% usage)
#### RAM:
#### Time: