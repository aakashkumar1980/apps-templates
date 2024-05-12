import time
import gnupg

def encrypt_file_with_gpg(input_file, output_file, public_key_file):
  gpg = gnupg.GPG()
  with open(public_key_file, 'rb') as key_file:
    recipients = gpg.import_keys(key_file.read())
  with open(input_file, 'rb') as input_stream:
    encrypted_data = gpg.encrypt_file(input_stream, recipients=recipients.fingerprints, armor=False, output=output_file)
  if not encrypted_data.ok:
    raise Exception(f"Encryption failed: {encrypted_data.status}")

if __name__ == "__main__":
  input_file = './_data/sample.csv'
  output_file = input_file + '.gpg'
  public_key_file = './_data/pgp_public_key.asc'
  start_time = time.time()
  encrypt_file_with_gpg(input_file, output_file, public_key_file)
  end_time = time.time()

  print("Execution time:", time.strftime('%H:%M:%S', time.gmtime(end_time - start_time)))
