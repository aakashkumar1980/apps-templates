package com.aadityadesigners.poc.max;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.log4j.Logger;

public class MaxApp {
  private static final Logger LOGGER = Logger.getLogger(MaxApp.class);

  public static void main(String[] args) {
    LOGGER.info("#########################");
    LOGGER.info("Starting MaxApp Application");
    LOGGER.info("#########################");

    SparkConf conf = new SparkConf()
        .setAppName("MaxApp")
        .setMaster("spark://ip-172-31-7-170.us-west-1.compute.internal:7077");
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
    conf.registerKryoClasses(new Class<?>[] { MaxApp.class });
    JavaSparkContext sc = new JavaSparkContext(conf);

    String filePath = "/home/ubuntu/Downloads/data.csv";

    // Load the data from a file
    JavaRDD<String> lines = sc.textFile(filePath);
    LOGGER.info("##### lines: " + lines.count());
    JavaRDD<Integer> numbers = lines.map(line -> {
      // Assuming the numerical word is always last, and entries are comma-separated
      String[] parts = line.split(",");
      try {
        return Integer.parseInt(parts[parts.length - 1]); // Parse the last part as an integer
      } catch (NumberFormatException e) {
        return 0; // Return a default value if parsing fails
      }
    });

    // Start timing
    long startTime = System.currentTimeMillis();
    // Use the reduce operation to find the maximum value
    Integer maxNumber = numbers.reduce(Math::max);
    LOGGER.info("##### maxNumber: " + maxNumber);

    // End timing and calculate the total time taken
    long endTime = System.currentTimeMillis();
    long duration = (endTime - startTime) / 1000; // Convert milliseconds to seconds
    LOGGER.info("##### Total time taken: " + duration + " seconds");

    // Stop the Spark context
    sc.stop();
    sc.close();
  }
}
