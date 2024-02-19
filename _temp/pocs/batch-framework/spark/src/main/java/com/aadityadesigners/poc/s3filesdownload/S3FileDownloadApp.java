package com.aadityadesigners.poc.s3filesdownload;

import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.aadityadesigners.poc.Utils;

public class S3FileDownloadApp {
  private static final Logger LOGGER = Logger.getLogger(S3FileDownloadApp.class);

  static String s3BucketPath = "aakash-kumar-poc-batch-poc-test";
  static String hdfsDestinationPath = String.format("hdfs://%s:9000/poc/batch-applications/", Utils.HOSTNAME);
  static String RESOURCE = "/home/ubuntu/Desktop/apps-templates/_temp/pocs/batch-framework/spark/src/main/resources";

  public static void main(String[] args) {
    LOGGER.info("##### Starting Application #####");

    SparkConf conf = new SparkConf()
        .setAppName("S3FileDownloadApp")
        .setMaster(String.format("spark://%s:7077", Utils.HOSTNAME));
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
    JavaSparkContext sc = new JavaSparkContext(conf);
    Utils.setOptimizedS3AConfig(conf, sc);
    sc.hadoopConfiguration().addResource(new Path(RESOURCE + "/core-site.xml"));

    long startTime = System.currentTimeMillis();
    for (String fileName : Utils.listFilesInS3Bucket(s3BucketPath, sc.hadoopConfiguration())) {
      LOGGER.info(String.format("Downloading file: %s", fileName));

      JavaRDD<String> fileData = sc.textFile(String.format("s3a://%s/%s", s3BucketPath, fileName));
      fileData.saveAsTextFile(hdfsDestinationPath + fileName);
    }
    long endTime = System.currentTimeMillis();
    Utils.printExecutionTime(LOGGER, startTime, endTime);

    // Close the Spark context
    sc.stop();
    sc.close();
  }

}
