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

public class EncryptDecryptV2_2 {

  private static final int CHUNK_SIZE = 5 * 1024 * 1024; // 5 MB chunks

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
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile))) {

      byte[] buffer = new byte[CHUNK_SIZE];
      int bytesRead;

      while ((bytesRead = inputStream.read(buffer)) != -1) {
        byte[] chunk = new byte[bytesRead];
        System.arraycopy(buffer, 0, chunk, 0, bytesRead);

        byte[] finalChunk = chunk;
        executor.submit(() -> {
          try {
            encryptChunk(finalChunk, outputStream, publicKey);
          } catch (IOException | PGPException e) {
            e.printStackTrace();
          }
        });

        // Nullify buffer reference and suggest GC to release memory
        chunk = null;
        System.gc();
      }

      executor.shutdown();
      if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
        executor.shutdownNow();
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

  private static void encryptChunk(byte[] chunk, OutputStream outputStream, PGPPublicKey publicKey) throws IOException, PGPException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(
        new BcPGPDataEncryptorBuilder(PGPEncryptedData.AES_256)
            .setWithIntegrityPacket(true)
            .setSecureRandom(new java.security.SecureRandom())
    );
    encryptedDataGenerator.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(publicKey).setProvider("BC"));

    try (OutputStream encryptedOut = encryptedDataGenerator.open(byteArrayOutputStream, new byte[1 << 16])) {
      PGPCompressedDataGenerator compressedDataGenerator = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);
      try (OutputStream compressedOut = compressedDataGenerator.open(encryptedOut)) {
        PGPLiteralDataGenerator literalDataGenerator = new PGPLiteralDataGenerator();
        try (OutputStream literalOut = literalDataGenerator.open(compressedOut, PGPLiteralData.BINARY, "chunk", chunk.length, new java.util.Date())) {
          literalOut.write(chunk);
        }
      }
      compressedDataGenerator.close();
    }
    encryptedDataGenerator.close();

    synchronized (outputStream) {
      outputStream.write(byteArrayOutputStream.toByteArray());
    }

    // Clear byteArrayOutputStream for GC
    byteArrayOutputStream.reset();
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
