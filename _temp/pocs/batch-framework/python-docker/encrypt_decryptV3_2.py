import time
import gnupg
from multiprocessing import Process, Queue

# Chunk size for reading the file
chunk_size = 1024 * 1024 * 100  # 100MB

def encrypt_chunk(gpg, input_chunk, recipient_fingerprints, output_file, thread_id):
  print(f'Encrypting chunk on thread {thread_id}...')
  encrypted_data = gpg.encrypt(input_chunk, recipients=recipient_fingerprints)
  print(f'Encrypted data for thread {thread_id}: {encrypted_data}')  # Debug line
  print(f'Encrypted data ok for thread {thread_id}: {encrypted_data.ok}')
  if encrypted_data.ok:
    try:
      print(f'Writing encrypted data to output file from thread {thread_id}...')
      with open(output_file, 'ab') as output_stream:
        output_stream.write(encrypted_data.data)
      print(f'Finished writing encrypted data to output file from thread {thread_id}.')
    except Exception as e:
      print(f'Error occurred during file writing from thread {thread_id}: {e}')

def encrypt_file_with_gpg(input_file, output_file, public_key_file, temp_dir, chunk_size=chunk_size):
  print('Starting encryption process...')
  # Initialize GPG
  gpg = gnupg.GPG(verbose=True)

  # Import the recipient's public key and extract fingerprints
  with open(public_key_file, 'rb') as key_file:
    recipients = gpg.import_keys(key_file.read())
  recipient_fingerprints = recipients.fingerprints

  with open(input_file, 'rb') as input_stream:
    process_id = 0
    while True:
      # Read a chunk of data from the input file
      chunk = input_stream.read(chunk_size)
      if not chunk:
        break

      # Encrypt each chunk in parallel
      process_id += 1
      encrypt_chunk(gpg, chunk, recipient_fingerprints, output_file, process_id)

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
