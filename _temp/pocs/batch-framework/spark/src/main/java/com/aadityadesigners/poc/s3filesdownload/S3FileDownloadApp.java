package com.aadityadesigners.poc.s3filesdownload;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.aadityadesigners.poc.Utils;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

import java.util.List;
import java.util.ArrayList;

public class S3FileDownloadApp {
  private static final Logger LOGGER = Logger.getLogger(S3FileDownloadApp.class);

  public static void main(String[] args) {
    LOGGER.info("##### Starting Application #####");

    SparkConf conf = new SparkConf()
        .setAppName("S3FileDownloadApp")
        .setMaster("spark://ip-172-31-7-170.us-west-1.compute.internal:7077");
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
    // Utils.setOptimizedS3AConfig(conf);

    JavaSparkContext sc = new JavaSparkContext(conf);
    sc.hadoopConfiguration().addResource(new Path(
        "/home/ubuntu/Desktop/apps-templates/_temp/pocs/batch-framework/spark/src/main/resources/core-site.xml"));

    long startTime = System.currentTimeMillis();
    String s3BucketPath = "aakash-kumar.apps-configs";
    String hdfsDestinationPath = "hdfs://ip-172-31-7-170.us-west-1.compute.internal:9000/poc/batch-applications/";
    for (String fileName : listFilesInS3Bucket(s3BucketPath, sc.hadoopConfiguration())) {
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

  private static List<String> listFilesInS3Bucket(String s3BucketPath, Configuration hadoopConfiguration) {
    List<String> s3Files = new ArrayList<>();

    BasicAWSCredentials awsCreds = new BasicAWSCredentials(
        hadoopConfiguration.get("fs.s3a.access.key"),
        hadoopConfiguration.get("fs.s3a.secret.key"));
    AmazonS3Client s3Client = new AmazonS3Client(awsCreds);
    s3Client.setRegion(Region.getRegion(Regions.US_WEST_1));

    s3Client.listObjects(s3BucketPath).getObjectSummaries().forEach(objectSummary -> {
      s3Files.add(objectSummary.getKey());
    });

    return s3Files;
  }

}
