package com.aadityadesigners.poc.estimate;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

public class EstimateApp {
  private static final Logger LOGGER = Logger.getLogger(EstimateApp.class);

  public static void main(String[] args) {
    LOGGER.info("#########################");
    LOGGER.info("##### Starting Application");
    LOGGER.info("#########################");

    SparkConf conf = new SparkConf()
        .setAppName("EstimateApp")
        .setMaster("spark://ip-172-31-7-170.us-west-1.compute.internal:7077");
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
    JavaSparkContext sc = new JavaSparkContext(conf);

    // Start timing
    long startTime = System.currentTimeMillis();

    int numSamples = 1000000 * loadFactor(args); // Adjust this value based on your cluster's performance
    JavaRDD<Integer> sample = sc.parallelize(range(0, numSamples));
    long count = sample.filter(new Function<Integer, Boolean>() {
      public Boolean call(Integer i) {
        double x = Math.random() * 2 - 1;
        double y = Math.random() * 2 - 1;
        return x * x + y * y <= 1;
      }
    }).count();

    // End timing
    long endTime = System.currentTimeMillis();
    LOGGER.info("##### Pi is roughly " + 4.0 * count / numSamples);
    // Calculate and print the total time taken
    long duration = (endTime - startTime) / 1000; // Convert milliseconds to seconds
    LOGGER.info("##### Total time taken (loadFactor:" + loadFactor(args) + ") is " + duration + " seconds");

    // Stop the Spark context
    sc.stop();
    sc.close();
  }

  private static java.util.List<Integer> range(int start, int end) {
    java.util.List<Integer> range = new java.util.ArrayList<>(end - start + 1);
    for (int i = start; i <= end; i++) {
      range.add(i);
    }
    return range;
  }

  private static int loadFactor(String[] args) {
    int loadFactor = 1; // Default value if not provided
    if (args.length > 0) {
      String[] splitArg = args[0].split("=");
      if (splitArg[0].equalsIgnoreCase("LOAD_FACTOR") && splitArg.length > 1) {
        try {
          loadFactor = Integer.parseInt(splitArg[1]);
        } catch (NumberFormatException e) {
          System.err.println("Invalid format for LOAD_FACTOR, using default of 10.");
        }
      }
    }

    return loadFactor;
  }
}
