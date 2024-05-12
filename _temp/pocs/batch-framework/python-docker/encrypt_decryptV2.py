import os
import time
import subprocess
import tempfile

chunk_size=1024*1024 # 1MB
def encrypt_file_with_gpg(input_file, output_file, public_key_file, chunk_size=chunk_size):
  with open(input_file, 'rb') as input_stream:
    with tempfile.NamedTemporaryFile(delete=False) as temp_file:
      while True:
        # Read a chunk of data from the input file
        chunk = input_stream.read(chunk_size)
        if not chunk:
          break

        # Write the chunk to the temporary file
        temp_file.write(chunk)

    # Use subprocess to call gpg for encryption
    subprocess.run(['gpg', '--batch', '--recipient-file', public_key_file, '--output', output_file, '--encrypt', temp_file.name])

    # Remove the temporary file
    os.remove(temp_file.name)



# to generate a PGP key, run the following command:
# $ gpg --full-gen-key --output $keyname.gpg
# ~/.gnupg/pubring.kbx is the single file containing the private and public keys
# next, to export the public key (used in encryption), run the following command:
# $ gpg --export --armor "$RecipientName/ID/emailAddress" > _data/pgp_public_key.asc
#################
### MAIN CODE ###
#################
if __name__ == "__main__":
  input_file = './_data/sample.csv'
  output_file = input_file + '.gpg'
  public_key_file = './_data/pgp_public_key.asc'
  start_time = time.time()
  encrypt_file_with_gpg(input_file, output_file, public_key_file)
  end_time = time.time()

  print("Execution time:", time.strftime('%H:%M:%S', time.gmtime(end_time - start_time)))
