package com.aadityadesigners.poc.s3filesdownload;

import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.aadityadesigners.poc.Utils;

public class S3FileDownloadApp {
  private static final Logger LOGGER = Logger.getLogger(S3FileDownloadApp.class);

  public static void main(String[] args) {
    LOGGER.info("##### Starting Application #####");

    SparkConf conf = new SparkConf()
        .setAppName("S3FileDownloadApp")
        .setMaster("spark://ip-172-31-7-170.us-west-1.compute.internal:7077");
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
    JavaSparkContext sc = new JavaSparkContext(conf);
    Utils.setOptimizedS3AConfig(conf, sc);

    sc.hadoopConfiguration().addResource(new Path(
        "/home/ubuntu/Desktop/apps-templates/_temp/pocs/batch-framework/spark/src/main/resources/core-site.xml"));

    long startTime = System.currentTimeMillis();
    String s3BucketPath = "aakash-kumar.apps-configs";
    String hdfsDestinationPath = "hdfs://ip-172-31-7-170.us-west-1.compute.internal:9000/poc/batch-applications/";
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
