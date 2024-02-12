package com.aadityadesigners.poc.s3filesdownload;

import java.util.List;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;

public class AWSS3Test {
  public static void main(String[] args) {
    // Replace these with your actual access key and secret key
    String accessKey = "";
    String secretKey = "";

    // Create AWS credentials
    BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

    // Create an S3 client using the older SDK method
    AmazonS3Client s3Client = new AmazonS3Client(awsCreds);

    // Optionally, set the region if necessary
    s3Client.setRegion(Region.getRegion(Regions.US_WEST_1));

    // List the buckets
    System.out.println("Listing buckets:");
    List<Bucket> buckets = s3Client.listBuckets();
    for (Bucket bucket : buckets) {
      System.out.println("* " + bucket.getName());
    }
  }
}
