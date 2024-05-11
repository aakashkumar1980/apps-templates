import subprocess

def encrypt_file_with_gpg(input_file, output_file, public_key_file):
  command = [
    'gpg',
    '--batch',
    '--recipient-file', public_key_file,
    '--output', output_file,
    '--encrypt', input_file
  ]
  subprocess.run(command, check=True)


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
  output_file = input_file+'.gpg'
  public_key_file = './_data/pgp_public_key.asc'
  encrypt_file_with_gpg(input_file, output_file, public_key_file)
