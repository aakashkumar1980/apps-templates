package com.aadityadesigners.poc;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import java.util.Iterator;

public class App {
  private static final Logger LOGGER = Logger.getLogger(App.class);

  public static void main(String[] args) {
    System.out.println("#########################");
    System.out.println("Starting Spark Application");
    System.out.println("#########################");

    SparkConf conf = new SparkConf()
        .setAppName("Simple Test")
        .setMaster("spark://ip-172-31-7-170.us-west-1.compute.internal:7077");
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
    conf.registerKryoClasses(new Class<?>[] { App.class, MaxFunction.class });
    JavaSparkContext sc = new JavaSparkContext(conf);

    String filePath = "/home/ubuntu/Desktop/apps-templates/_temp/pocs/batch-framework/spark/src/main/resources/temperature.txt";
    // Load the temperature data from a file
    JavaRDD<String> lines = sc.textFile(filePath);
    JavaRDD<Integer> temperatures = lines.map(line -> Integer.parseInt(line));

    // Logging within mapPartitions to capture processing on workers
    JavaRDD<Integer> loggedTemperatures = temperatures.mapPartitions(new FlatMapFunction<Iterator<Integer>, Integer>() {
      @Override
      public Iterator<Integer> call(Iterator<Integer> iterator) throws Exception {
        List<Integer> tempList = new ArrayList<>();
        while (iterator.hasNext()) {
          Integer temperature = iterator.next();
          System.out.println("##### Processing temperature: " + temperature);
          tempList.add(temperature);
        }
        return tempList.iterator();
      }
    });

    // Use the reduce operation to find the maximum value
    Integer maxTemperature = loggedTemperatures.reduce(new MaxFunction());
    LOGGER.info("##### maxTemperature: " + maxTemperature);

    // Stop the Spark context
    sc.stop();
    sc.close();
  }
}
