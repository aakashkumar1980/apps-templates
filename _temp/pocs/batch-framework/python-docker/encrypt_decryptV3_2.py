import time
import gnupg
import tempfile
from multiprocessing import Process, Queue

# Chunk size for reading the file
chunk_size = 1024 * 1024 * 100  # 100MB
# Modify the encrypt_chunk function
def encrypt_chunk(gpg, input_chunk, recipient_fingerprints, output_list):
  print('Encrypting chunk...')
  encrypted_data = gpg.encrypt(input_chunk, recipients=recipient_fingerprints)
  print('Encrypted data:', encrypted_data)  # Debug line
  print('Encrypted data ok:', encrypted_data.ok)
  if encrypted_data.ok:
    output_list.append(encrypted_data.data)

# Modify the encrypt_file_with_gpg function
def encrypt_file_with_gpg(input_file, output_file, public_key_file, temp_dir, chunk_size=chunk_size):
  print('Starting encryption process...')
  # Initialize GPG
  gpg = gnupg.GPG(verbose=True)

  # Import the recipient's public key and extract fingerprints
  with open(public_key_file, 'rb') as key_file:
    recipients = gpg.import_keys(key_file.read())
  recipient_fingerprints = recipients.fingerprints

  with open(input_file, 'rb') as input_stream:
    with open(output_file, 'wb') as output_stream:
      while True:
        # Read a chunk of data from the input file
        chunk = input_stream.read(chunk_size)
        if not chunk:
          break

        # Encrypt the chunk
        encrypted_data = gpg.encrypt(chunk, recipients=recipient_fingerprints)
        print('Encrypted data ok:', encrypted_data.ok)
        if encrypted_data.ok:
          # Write the encrypted chunk to the output file
          output_stream.write(encrypted_data.data)

  print('Encryption process completed.')




# Main code
temp_dir = '/mnt/ebs_volume/tmp'
if __name__ == "__main__":
  input_file = './_data/sample.csv'
  output_file = input_file + '.gpg'
  public_key_file = './_data/pgp_public_key.asc'
  start_time = time.time()
  encrypt_file_with_gpg(input_file, output_file, public_key_file, temp_dir)
  end_time = time.time()

  print("Execution time:", time.strftime('%H:%M:%S', time.gmtime(end_time - start_time)))


# RESULTS #:
## File: customers-256000000.csv (44.7 GB size ->  GB encrypted)
### CPU: 8 cores | 16 vCPU (% usage)
#### RAM:
#### Execution time (MM:HH:SS):