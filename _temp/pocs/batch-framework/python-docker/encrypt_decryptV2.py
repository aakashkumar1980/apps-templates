import os
import time
import subprocess
import tempfile

def encrypt_file_with_gpg(input_file, output_file, public_key_file):
  with open(input_file, 'rb') as input_stream:
    # Write input file content to a temporary file
    with tempfile.NamedTemporaryFile(delete=False) as temp_file:
      temp_file.write(input_stream.read())

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
