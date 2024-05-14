import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;
import org.bouncycastle.openpgp.operator.bc.BcPGPDataEncryptorBuilder;

import java.io.*;
import java.security.Security;
import java.util.Iterator;

public class EncryptDecryptV2_3 {

  private static final int CHUNK_SIZE = 1024 * 1024 * 100; // 100 MB chunks

  public static void main(String[] args) throws Exception {
    Security.addProvider(new BouncyCastleProvider());

    String fileName = "customers-256000000.csv";
    String inputFile = "/mnt/ebs_volume/tmp/_data/" + fileName;
    String outputFile = "/mnt/ebs_volume/tmp/_data/" + fileName + ".pgp";
    String publicKeyFile = "/mnt/ebs_volume/tmp/_data/pgp_public_key.asc";
    String tempDir = "/mnt/ebs_volume/tmp/_data";
    long startTime = System.currentTimeMillis();

    encryptFileWithGPG(inputFile, outputFile, publicKeyFile, tempDir);

    long endTime = System.currentTimeMillis();
    System.out.println("Execution time: " + String.format("%02d:%02d:%02d",
        (endTime - startTime) / 3600000,
        ((endTime - startTime) / 60000) % 60,
        ((endTime - startTime) / 1000) % 60));
  }

  private static void encryptFileWithGPG(String inputFile, String outputFile, String publicKeyFile, String tempDir) throws IOException, PGPException {
    PGPPublicKey publicKey = readPublicKey(publicKeyFile);

    File tempFile = File.createTempFile("temp", ".tmp", new File(tempDir));
    try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
        BufferedOutputStream tempOutputStream = new BufferedOutputStream(new FileOutputStream(tempFile))) {

      byte[] buffer = new byte[CHUNK_SIZE];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        tempOutputStream.write(buffer, 0, bytesRead);
      }
    }

    try (BufferedInputStream tempInputStream = new BufferedInputStream(new FileInputStream(tempFile));
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile))) {

      PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(
          new BcPGPDataEncryptorBuilder(PGPEncryptedData.AES_256)
              .setWithIntegrityPacket(true)
              .setSecureRandom(new java.security.SecureRandom())
      );
      encryptedDataGenerator.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(publicKey).setProvider("BC"));

      try (OutputStream encryptedOut = encryptedDataGenerator.open(outputStream, new byte[1 << 16])) {
        PGPCompressedDataGenerator compressedDataGenerator = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);
        try (OutputStream compressedOut = compressedDataGenerator.open(encryptedOut)) {
          PGPLiteralDataGenerator literalDataGenerator = new PGPLiteralDataGenerator();
          try (OutputStream literalOut = literalDataGenerator.open(compressedOut, PGPLiteralData.BINARY, "tempfile", new File(tempDir).length(), new java.util.Date())) {
            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;
            while ((bytesRead = tempInputStream.read(buffer)) != -1) {
              literalOut.write(buffer, 0, bytesRead);
            }
          }
        }
        compressedDataGenerator.close();
      }
      encryptedDataGenerator.close();
    } finally {
      if (!tempFile.delete()) {
        System.err.println("Failed to delete temporary file: " + tempFile.getAbsolutePath());
      }
    }
  }

  private static PGPPublicKey readPublicKey(String publicKeyFile) throws IOException, PGPException {
    try (InputStream keyIn = new BufferedInputStream(new FileInputStream(publicKeyFile))) {
      PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(
          PGPUtil.getDecoderStream(keyIn), new JcaKeyFingerprintCalculator());
      Iterator<PGPPublicKeyRing> keyRingIter = pgpPub.getKeyRings();
      while (keyRingIter.hasNext()) {
        PGPPublicKeyRing keyRing = keyRingIter.next();
        Iterator<PGPPublicKey> keyIter = keyRing.getPublicKeys();
        while (keyIter.hasNext()) {
          PGPPublicKey key = keyIter.next();
          if (key.isEncryptionKey()) {
            return key;
          }
        }
      }
    }
    throw new IllegalArgumentException("Can't find encryption key in key ring.");
  }
}
