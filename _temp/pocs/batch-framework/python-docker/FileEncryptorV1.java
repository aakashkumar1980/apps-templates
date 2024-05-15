import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Objects;
import java.nio.file.Paths;


@Getter
@Builder
@AllArgsConstructor
public class FileEncryptorV1 {

  static {
    // Add Bouncy castle to JVM
    if (Objects.isNull(Security.getProvider(BouncyCastleProvider.PROVIDER_NAME))) {
      Security.addProvider(new BouncyCastleProvider());
    }
  }

  @Builder.Default
  private int compressionAlgorithm = CompressionAlgorithmTags.ZIP;
  @Builder.Default
  private int symmetricKeyAlgorithm = SymmetricKeyAlgorithmTags.AES_128;
  @Builder.Default
  private boolean armor = true;
  @Builder.Default
  private boolean withIntegrityCheck = true;
  @Builder.Default
  private int bufferSize = 100 * 1024 * 1024; // 100 MB buffer size
  //private int bufferSize = 1 << 16;



  public void encrypt(OutputStream encryptOut, InputStream clearIn, long length, InputStream publicKeyIn)
      throws IOException, PGPException {
    PGPCompressedDataGenerator compressedDataGenerator =
        new PGPCompressedDataGenerator(compressionAlgorithm);
    PGPEncryptedDataGenerator pgpEncryptedDataGenerator = new PGPEncryptedDataGenerator(
        // This bit here configures the encrypted data generator
        new JcePGPDataEncryptorBuilder(symmetricKeyAlgorithm)
            .setWithIntegrityPacket(withIntegrityCheck)
            .setSecureRandom(new SecureRandom())
            .setProvider(BouncyCastleProvider.PROVIDER_NAME)
    );
    // Adding public key
    pgpEncryptedDataGenerator.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(
        FileEncryptionDecryptionCommonUtilsV1.getPublicKey(publicKeyIn)));
    if (armor) {
      encryptOut = new ArmoredOutputStream(encryptOut);
    }
    OutputStream cipherOutStream = pgpEncryptedDataGenerator.open(encryptOut, new byte[bufferSize]);
    FileEncryptionDecryptionCommonUtilsV1.copyAsLiteralData(compressedDataGenerator.open(cipherOutStream), clearIn, length, bufferSize);
    // Closing all output streams in sequence
    compressedDataGenerator.close();
    cipherOutStream.close();
    encryptOut.close();
  }


  public static void main(String[] args) {
    String inputFilePath = "/mnt/ebs_volume/tmp/_data/customers-2000000.csv";
    //String inputFilePath = "/mnt/ebs_volume/PrivateLearningV2.1/apps/apps-templates/_temp/pocs/batch-framework/python-docker/_data/sample.csv";
    String publicKeyFilePath = "/mnt/ebs_volume/tmp/_data/pgp_public_key.asc";
    String outputFilePath = inputFilePath+".pgp";

    long startTime = System.currentTimeMillis();
    FileEncryptorV1 fileEncryptorV1 = FileEncryptorV1.builder()
        .armor(true)
        .compressionAlgorithm(CompressionAlgorithmTags.ZIP)
        .symmetricKeyAlgorithm(SymmetricKeyAlgorithmTags.AES_128)
        .withIntegrityCheck(true)
        .build();

    // start file encryption
    try (InputStream clearIn = Files.newInputStream(Paths.get(inputFilePath));
         InputStream publicKeyIn = Files.newInputStream(Paths.get(publicKeyFilePath))) {
      try (OutputStream encryptOut = Files.newOutputStream(Paths.get(outputFilePath))) {
        fileEncryptorV1.encrypt(encryptOut, clearIn, Files.size(Paths.get(inputFilePath)), publicKeyIn);
      }
    } catch (IOException | PGPException e) {
      e.printStackTrace();
    }
    long endTime = System.currentTimeMillis();
    // total time in HH:MM:SS format
    long totalTime = endTime - startTime;
    System.out.println("Total time taken: " + ((totalTime / (1000 * 60 * 60)) % 24) + " hours " + ((totalTime / (1000 * 60)) % 60) + " minutes " + ((totalTime / 1000) % 60) + " seconds");

  }

}

/**
 * # RESULTS #:
 * ## File: customers-2000000.csv (350 MB size ->  223 MB encrypted)
 * ### CPU: 4 cores | 8 vCPU (10% usage)
 * #### RAM: 0.5 GB
 * #### Execution time (MM:HH:SS): 0 hours 11 minutes 16 seconds
 *
 *
 */