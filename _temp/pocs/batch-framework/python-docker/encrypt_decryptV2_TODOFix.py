import time
import subprocess
import os

def encrypt_file_with_gpg(input_file, output_file, public_key_file):
  chunk_size = 1024 * 1024  # 1 MB chunk size (adjust as needed)
  num_processes = os.cpu_count()

  with open(input_file, 'rb') as input_stream:
    with open(output_file, 'wb') as output_stream:
      while True:
        # Read a chunk of data
        chunk = input_stream.read(chunk_size)
        if not chunk:
          break

        # Spawn subprocesses for parallel encryption
        processes = []
        for _ in range(min(num_processes, len(chunk) // chunk_size + 1)):
          batch_chunk = input_stream.read(chunk_size)
          if not batch_chunk:
            break
          process = subprocess.Popen(['gpg', '--batch', '--recipient-file', public_key_file, '--output', '-', '--encrypt'], stdin=subprocess.PIPE, stdout=subprocess.PIPE)
          processes.append((process, batch_chunk))

        # Collect output from subprocesses
        for process, batch_chunk in processes:
          output, _ = process.communicate(input=batch_chunk)
          output_stream.write(output)


if __name__ == "__main__":
  input_file = './_data/sample.csv'
  output_file = input_file + '.gpg'
  public_key_file = './_data/pgp_public_key.asc'
  start_time = time.time()
  encrypt_file_with_gpg(input_file, output_file, public_key_file)
  end_time = time.time()

  print("Execution time:", time.strftime('%H:%M:%S', time.gmtime(end_time - start_time)))
