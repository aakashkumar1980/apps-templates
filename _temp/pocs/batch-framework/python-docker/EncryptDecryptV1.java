import org.apache.commons.io.IOUtils;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;
import org.bouncycastle.openpgp.operator.bc.BcPGPDataEncryptorBuilder;

import java.io.*;
import java.security.Security;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EncryptDecryptV1 {

  private static final int CHUNK_SIZE = 10 * 1024 * 1024; // 10 MB chunk size

  public static void main(String[] args) throws Exception {
    Security.addProvider(new BouncyCastleProvider());

    String fileName = "customers-256000000.csv";
    File inputFile = new File("/mnt/ebs_volume/tmp/_data/" + fileName);
    File publicKeyFile = new File("/mnt/ebs_volume/tmp/_data/pgp_public_key.asc");
    File outputFile = new File("/mnt/ebs_volume/tmp/_data/" + fileName + ".pgp");
    long startTime = System.currentTimeMillis();

    PGPPublicKey publicKey = readPublicKey(publicKeyFile);
    int processors = Double.valueOf(Runtime.getRuntime().availableProcessors()+1).intValue();
    System.out.println("Number of processors: " + processors);
    ExecutorService executor = Executors.newFixedThreadPool(processors);

    try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
        ArmoredOutputStream armoredOutputStream = new ArmoredOutputStream(outputStream)) {

      PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(
          new BcPGPDataEncryptorBuilder(PGPEncryptedData.AES_256)
              .setWithIntegrityPacket(true)
              .setSecureRandom(new java.security.SecureRandom())
      );
      encryptedDataGenerator.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(publicKey).setProvider("BC"));

      try (OutputStream encryptedOut = encryptedDataGenerator.open(armoredOutputStream, new byte[1 << 16])) {
        byte[] buffer = new byte[CHUNK_SIZE];
        int bytesRead;
        int chunkIndex = 0;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
          final byte[] chunk = new byte[bytesRead];
          System.arraycopy(buffer, 0, chunk, 0, bytesRead);

          final int finalChunkIndex = chunkIndex;

          executor.submit(() -> {
            try {
              encryptChunk(chunk, encryptedOut, finalChunkIndex);
            } catch (IOException | PGPException e) {
              e.printStackTrace();
            }
          });

          chunkIndex++;
        }

        executor.shutdown();
        if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
          executor.shutdownNow();
        }
      }
    } finally {
      executor.shutdownNow();
    }

    long endTime = System.currentTimeMillis();
    System.out.println("Execution time: " + String.format("%02d:%02d:%02d",
        (endTime - startTime) / 3600000,
        ((endTime - startTime) / 60000) % 60,
        ((endTime - startTime) / 1000) % 60));
  }

  private static void encryptChunk(byte[] chunk, OutputStream encryptedOut, int chunkIndex) throws IOException, PGPException {
    PGPCompressedDataGenerator compressedDataGenerator = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    OutputStream compressedOut = compressedDataGenerator.open(byteArrayOutputStream);

    PGPLiteralDataGenerator literalDataGenerator = new PGPLiteralDataGenerator();
    OutputStream literalOut = literalDataGenerator.open(compressedOut, PGPLiteralData.BINARY, "chunk_" + chunkIndex, chunk.length, new java.util.Date());

    literalOut.write(chunk);
    literalOut.close();

    compressedOut.close();

    synchronized (encryptedOut) {
      encryptedOut.write(byteArrayOutputStream.toByteArray());
    }
  }

  private static PGPPublicKey readPublicKey(File publicKeyFile) throws IOException, PGPException {
    try (InputStream keyIn = new BufferedInputStream(new FileInputStream(publicKeyFile))) {
      PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(
          PGPUtil.getDecoderStream(keyIn), new JcaKeyFingerprintCalculator());
      for (PGPPublicKeyRing keyRing : pgpPub) {
        for (PGPPublicKey key : keyRing) {
          if (key.isEncryptionKey()) {
            return key;
          }
        }
      }
    }
    throw new IllegalArgumentException("Can't find encryption key in key ring.");
  }
}

/**
 * # RESULTS #:
 * # IO2 Volume: 6000 IOPS
 * # Test ($ sudo fio /mnt/ebs_volume/tmp/fio_test.fio) -> IOPS: 3049  | 12.5 MB/s
 *
 * # File: customers-256000000.csv (44.7 GB size -> 28.5 GB encrypted)
 * ### CPU: 16 cores | 32 vCPU (% usage)
 * #### Execution time (MM:HH:SS): 00:17:43
 */