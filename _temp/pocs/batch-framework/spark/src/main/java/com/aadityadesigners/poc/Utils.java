package com.aadityadesigners.poc;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class Utils {
  private static final Logger LOGGER = Logger.getLogger(Utils.class);

  /**
   * TODO: Implement and check the performance of this method
   * Set optimized S3A configuration for improved performance
   * 
   * @param conf
   */
  public static void setOptimizedS3AConfig(SparkConf conf, JavaSparkContext sc) {
    int executorCores = Integer.parseInt(sc.getConf().get("spark.executor.cores", "1"));
    int threadsMax = executorCores * 2;
    int connectionMaximum = threadsMax;
    LOGGER.info(String.format("##### Setting S3A configurations: threadsMax=%d, connectionMaximum=%d",
        threadsMax, connectionMaximum));

    /** Essential S3 configurations **/
    conf.set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
    conf.set("spark.hadoop.fs.s3a.logging.level", "DEBUG");

    conf.set("spark.speculation", "true");
    conf.set("spark.hadoop.fs.s3a.block.size", "268435456"); // Set to 256 MB
    conf.set("spark.hadoop.fs.s3a.connection.maximum", String.valueOf(connectionMaximum));
    conf.set("spark.hadoop.fs.s3a.threads.max", String.valueOf(threadsMax));
    conf.set("spark.hadoop.fs.s3a.connection.ssl.enabled", "true");
    conf.set("spark.hadoop.fs.s3a.path.style.access", "true");

    conf.set("spark.hadoop.fs.s3a.connection.timeout", "20000");
    conf.set("spark.hadoop.fs.s3a.connection.establish.timeout", "5000");
    conf.set("spark.hadoop.fs.s3a.socket.timeout", "20000");
    conf.set("spark.hadoop.fs.s3a.max.retries", "10");
    conf.set("spark.hadoop.fs.s3a.retry.limit", "10");
    conf.set("spark.hadoop.fs.s3a.attempts.maximum", "10");

    /** DOWNLOADS */
    conf.set("spark.hadoop.fs.s3a.read.ahead.range", "4194304"); // 4 MB
    conf.set("spark.hadoop.fs.s3a.metadata.cache.enable", "true");
    conf.set("spark.hadoop.fs.s3a.metadata.cache.ttl", "300000"); // 5 minutes

    /** UPLOADS */
    // Enable fast upload for improved performance
    conf.set("spark.hadoop.fs.s3a.fast.upload", "true");
    conf.set("spark.hadoop.fs.s3a.fast.upload.buffer", "disk");
    conf.set("spark.hadoop.fs.s3a.buffer.dir", "/mnt/tmp");

    // Multipart upload settings
    conf.set("spark.hadoop.fs.s3a.multipart.size", "104857600"); // Size of each part (100 MB)
    conf.set("spark.hadoop.fs.s3a.multipart.threshold", "104857600"); // Threshold for multipart uploads (100 MB)

    // Configure whether to purge multipart uploads that haven't been completed
    conf.set("spark.hadoop.fs.s3a.multipart.purge", "false");
    conf.set("spark.hadoop.fs.s3a.multipart.purge.age", "86400"); // Age in seconds before purging incomplete uploads

  }

  public static void printExecutionTime(final Logger logger, long startTime, long endTime) {
    logger.info(String.format("##### Total time taken (hh:mm:ss): %s", StringUtils.leftPad(
        String.valueOf((endTime - startTime) / 1000 / 60 / 60), 2, "0") + ":"
        + StringUtils.leftPad(
            String.valueOf((endTime - startTime) / 1000 / 60 % 60), 2, "0")
        + ":" + StringUtils.leftPad(
            String.valueOf((endTime - startTime) / 1000 % 60), 2, "0")));
  }
}
