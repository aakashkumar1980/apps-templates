package com.aadityadesigners.poc;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;

public class Utils {

  /**
   * TODO: Implement and check the performance of this method
   * Set optimized S3A configuration for improved performance
   * 
   * @param conf
   */
  public static void setOptimizedS3AConfig(SparkConf conf) {
    // Essential S3 configurations
    conf.set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
    conf.set("spark.hadoop.fs.s3a.block.size", "268435456"); // Set to 256 MB

    // Enable fast upload for improved performance
    conf.set("spark.hadoop.fs.s3a.fast.upload", "true");
    conf.set("spark.hadoop.fs.s3a.fast.upload.buffer", "disk");

    // Set a directory for buffering data during uploads
    conf.set("spark.hadoop.fs.s3a.buffer.dir", "/mnt/tmp");

    // Increase the number of threads for parallel uploads and downloads
    conf.set("spark.hadoop.fs.s3a.threads.max", String.valueOf(Runtime.getRuntime().availableProcessors()));

    // Adjust the maximum number of connections to S3
    conf.set("spark.hadoop.fs.s3a.connection.maximum", "1000");

    // Set connection timeout and socket timeout (in milliseconds)
    conf.set("spark.hadoop.fs.s3a.connection.timeout", "20000");
    conf.set("spark.hadoop.fs.s3a.socket.timeout", "20000");

    // Multipart upload settings
    conf.set("spark.hadoop.fs.s3a.multipart.size", "104857600"); // Size of each part for multipart upload (100 MB)
    conf.set("spark.hadoop.fs.s3a.multipart.threshold", "104857600"); // Threshold for starting multipart uploads (100
                                                                      // MB)

    // Configure whether to automatically purge multipart uploads that haven't been
    // completed
    conf.set("spark.hadoop.fs.s3a.multipart.purge", "false");
    conf.set("spark.hadoop.fs.s3a.multipart.purge.age", "86400"); // Age in seconds before purging incomplete multipart
                                                                  // uploads

    // Optionally, enable SSL for secure data transfer (recommended for production)
    conf.set("spark.hadoop.fs.s3a.connection.ssl.enabled", "true");

    // If dealing with S3 bucket naming conventions requiring path-style access
    conf.set("spark.hadoop.fs.s3a.path.style.access", "true");

  }

  public static void printExecutionTime(final Logger LOGGER, long startTime, long endTime) {
    LOGGER.info(String.format("Total time taken (hh:mm:ss): %s", StringUtils.leftPad(
        String.valueOf((endTime - startTime) / 1000 / 60 / 60), 2, "0") + ":"
        + StringUtils.leftPad(
            String.valueOf((endTime - startTime) / 1000 / 60 % 60), 2, "0")
        + ":" + StringUtils.leftPad(
            String.valueOf((endTime - startTime) / 1000 % 60), 2, "0")));
  }
}
