import time
import gnupg
import tempfile
from multiprocessing import Process, Queue

# Chunk size for reading the file
chunk_size = 1024 * 1024 * 100  # 100MB
def encrypt_chunk(gpg, input_chunk, recipient_fingerprints, output_queue):
  print('Encrypting chunk...')
  encrypted_data = gpg.encrypt(input_chunk, recipients=recipient_fingerprints)
  print('Encrypted data ok:', encrypted_data.ok)
  if encrypted_data.ok:
    output_queue.put(encrypted_data.data)

def encrypt_file_with_gpg(input_file, output_file, public_key_file, temp_dir, chunk_size=chunk_size):
  print('Starting encryption process...')
  # Initialize GPG
  gpg = gnupg.GPG(verbose=True)

  # Import the recipient's public key and extract fingerprints
  with open(public_key_file, 'rb') as key_file:
    recipients = gpg.import_keys(key_file.read())
  recipient_fingerprints = recipients.fingerprints

  output_queue = Queue()

  with open(input_file, 'rb') as input_stream:
    with tempfile.NamedTemporaryFile(delete=False, dir=temp_dir) as temp_file:
      processes = []
      while True:
        # Read a chunk of data from the input file
        chunk = input_stream.read(chunk_size)
        if not chunk:
          break

        # Encrypt each chunk in parallel
        process = Process(target=encrypt_chunk, args=(gpg, chunk, recipient_fingerprints, output_queue))
        print('Starting encryption process for chunk...')
        process.start()
        processes.append(process)

        # Write encrypted chunks to temporary file
        while not output_queue.empty():
          temp_file.write(output_queue.get())

      # Wait for all processes to finish
      for process in processes:
        print('Waiting for encryption process to finish...')
        process.join()

      # Move the file pointer to the beginning of the temporary file
      temp_file.seek(0)

      # Inside the encrypt_file_with_gpg function
      try:
        # Write encrypted data to the final output file
        print('Writing encrypted data to output file...')
        with open(output_file, 'wb') as output_stream:
          encrypted_data = temp_file.read()
          print('Encrypted data size:', len(encrypted_data))  # Debug line
          output_stream.write(encrypted_data)
        print('Finished writing encrypted data to output file.')  # Debug line
      except Exception as e:
        print('Error occurred during file writing:', e)

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