# pip install pycryptodome
from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP


def encrypt_file_segment(segment, key):
  chunk_size = 4096
  with open(segment, 'rb') as f:
    data = f.read(chunk_size)
    cipher_text = b''
    while data:
      cipher_text += key.encrypt(data)
      data = f.read(chunk_size)
  with open(segment + '.enc', 'wb') as f:
    f.write(cipher_text)

def decrypt_file_segment(segment, key):
  chunk_size = 4096
  with open(segment, 'rb') as f:
    data = f.read(chunk_size)
    plain_text = b''
    while data:
      plain_text += key.decrypt(data)
      data = f.read(chunk_size)
  with open(segment[:-4], 'wb') as f:
    f.write(plain_text)

#################
### MAIN CODE ###
#################
if __name__ == "__main__":
  # Define file paths
  file_path = './_data/sample.csv'
  public_key_path = './_data/public.pem'
  private_key_path = './_data/private.pem'
  private_key_password = 'apple26j'

  # Load public and private keys
  with open(public_key_path, 'rb') as f:
    public_key = RSA.import_key(f.read())
  with open(private_key_path, 'rb') as f:
    private_key = RSA.import_key(f.read(), passphrase=private_key_password)

  ## ENCRYPTION AND DECRYPTION ##
  encrypt_file_segment(file_path, PKCS1_OAEP.new(public_key))
  #decrypt_file_segment(file_path + '.enc', PKCS1_OAEP.new(private_key))

