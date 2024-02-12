package com.aadityadesigners.poc.s3filesdownload;

import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class S3FileDownloadApp {
  private static final Logger LOGGER = Logger.getLogger(S3FileDownloadApp.class);

  public static void main(String[] args) {
    LOGGER.info("##### Starting Application #####");

    SparkConf conf = new SparkConf()
        .setAppName("S3FileDownloadApp")
        .setMaster("spark://ip-172-31-7-170.us-west-1.compute.internal:7077");
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
    conf.set("fs.s3a.connection.maximum", "1000");

    JavaSparkContext sc = new JavaSparkContext(conf);
    sc.hadoopConfiguration().addResource(new Path(
        "/home/ubuntu/Desktop/apps-templates/_temp/pocs/batch-framework/spark/src/main/resources/core-site.xml"));

    // Define your S3 bucket path
    String s3BucketPath = "s3a://aakash-kumar.apps-configs/";
    // Assuming you want to list files and directories at the root of the bucket
    JavaRDD<String> fileNames = sc.textFile(s3BucketPath);

    sc.stop();
    sc.close();

  }

}
