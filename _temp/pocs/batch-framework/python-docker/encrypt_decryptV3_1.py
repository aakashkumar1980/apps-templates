import time
import gnupg
import tempfile

# Chunk size for reading the file
chunk_size = 1024 * 1024 * 100  # 100MB
def encrypt_file_with_gpg(input_file, output_file, public_key_file, temp_dir, chunk_size=chunk_size):
  # Initialize GPG
  gpg = gnupg.GPG(verbose=True)

  # Import the recipient's public key and extract fingerprints
  with open(public_key_file, 'rb') as key_file:
    recipients = gpg.import_keys(key_file.read())
  recipient_fingerprints = recipients.fingerprints

  with open(input_file, 'rb') as input_stream:
    with tempfile.NamedTemporaryFile(delete=True, dir=temp_dir) as temp_file:
      while True:
        # Read a chunk of data from the input file
        chunk = input_stream.read(chunk_size)
        if not chunk:
          break

        # Write the chunk to the temporary file
        temp_file.write(chunk)

      # After writing the entire file, move the file pointer to the beginning
      temp_file.seek(0)

      # Encrypt the temporary file
      print('Encrypting file...')
      encrypted_data = gpg.encrypt_file(temp_file.name, recipients=recipient_fingerprints, output=output_file)
      if not encrypted_data.ok:
        print("Encryption failed:", encrypted_data.status)



# USAGE:
# to generate a PGP key, run the following command:
# $ gpg --full-gen-key --output $keyname.gpg
# ~/.gnupg/pubring.kbx is the single file containing the private and public keys
# next, to export the public key (used in encryption), run the following command:
# $ gpg --export --armor "$RecipientName/ID/emailAddress" > _data/pgp_public_key.asc
# $ pip install python-gnupg
#################
### MAIN CODE ###
#################
temp_dir = '/mnt/ebs_volume/tmp'
if __name__ == "__main__":
  input_file = '/mnt/ebs_volume/tmp/_data/customers-256000000.csv'
  output_file = input_file + '.gpg'
  public_key_file = '/mnt/ebs_volume/tmp/_data/pgp_public_key.asc'
  start_time = time.time()
  encrypt_file_with_gpg(input_file, output_file, public_key_file, temp_dir)
  end_time = time.time()

  print("Execution time:", time.strftime('%H:%M:%S', time.gmtime(end_time - start_time)))


# RESULTS #:
## File: customers-256000000.csv (44.7 GB size -> 28.5 GB encrypted)
### CPU: 4 cores | 8 vCPU (% usage)
#### RAM:
#### Execution time (MM:HH:SS): 00:43:57
#
# IO2 Volume: 6000 IOPS
# Test ($ sudo fio /mnt/ebs_volume/tmp/fio_test.fio) -> IOPS: 3049  | 12.5 MB/s
### CPU: 4 cores | 8 vCPU (% usage)
#### RAM:
#### Execution time (MM:HH:SS):
#
#
### CPU: 8 cores | 16 vCPU (% usage)
#### RAM:
#### Execution time (MM:HH:SS): 00:41:19

# VERIFICATION #
# $ gpg --decrypt ./_data/customers-256000000.csv.gpg > ./_data/decrypted_customers.csv
#
# $ sed -n '6666{p;q;}' ./_data/customers-256000000.csv
# $ sed -n '6666{p;q;}' ./_data/decrypted_customers.csv
#
# $ tail -n 6666 ./_data/customers-256000000.csv | head -n 1
# $ tail -n 6666 ./_data/decrypted_customers.csv | head -n 1
