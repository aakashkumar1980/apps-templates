import time
import subprocess

def encrypt_file_with_gpg(input_file, output_file, public_key_file):
  with open(input_file, 'rb') as input_stream:
    with open(output_file, 'wb') as output_stream:
      command = [
        'gpg',
        '--batch',
        '--recipient-file', public_key_file,
        '--output', '-',
        '--encrypt'
      ]
      subprocess.run(command, stdin=input_stream, stdout=output_stream, check=True)

if __name__ == "__main__":
  input_file = './_data/customers-256000000.csv'
  output_file = input_file + '.gpg'
  public_key_file = './_data/pgp_public_key.asc'
  start_time = time.time()
  encrypt_file_with_gpg(input_file, output_file, public_key_file)
  end_time = time.time()

  print("Execution time:", time.strftime('%H:%M:%S', time.gmtime(end_time - start_time)))
