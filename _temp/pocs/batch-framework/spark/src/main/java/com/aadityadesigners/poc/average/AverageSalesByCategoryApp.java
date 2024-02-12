package com.aadityadesigners.poc.average;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import com.aadityadesigners.poc.average.model.SalesRecord;
import scala.Tuple2;

import java.util.List;

public class AverageSalesByCategoryApp {
  private static final Logger LOGGER = Logger.getLogger(AverageSalesByCategoryApp.class);

  public static void main(String[] args) {
    LOGGER.info("##### Starting Application #####");
    SparkConf conf = new SparkConf()
        .setAppName("AverageSalesByCategoryApp")
        .setMaster("spark://ip-172-31-7-170.us-west-1.compute.internal:7077");
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
    JavaSparkContext sc = new JavaSparkContext(conf);

    String filePath = "/home/ubuntu/Downloads/data.csv";
    JavaRDD<String> lines = sc.textFile(filePath);

    // Mapping
    long startTimeMapping = System.currentTimeMillis();
    LOGGER.info("##### Mapping #####");
    JavaRDD<SalesRecord> records = SalesRecordMapper.mapLinesToSalesRecords(lines);
    JavaPairRDD<String, Tuple2<Integer, Integer>> categorySalesPairs = SalesRecordMapper
        .mapToCategorySalesPairs(records);
    long endTimeMapping = System.currentTimeMillis();
    LOGGER
        .info("##### Total time taken for Mapping is " + ((endTimeMapping - startTimeMapping) / 1000.0) + " seconds");

    // Reducing
    long startTimeReducing = System.currentTimeMillis();
    LOGGER.info("##### Reducing #####");
    JavaPairRDD<String, Tuple2<Integer, Integer>> reducedPairs = SalesReducer
        .reduceCategorySalesPairs(categorySalesPairs);
    JavaPairRDD<String, Double> averageSalesByCategory = SalesReducer.calculateAverageSalesByCategory(reducedPairs);
    long endTimeReducing = System.currentTimeMillis();
    LOGGER
        .info(
            "##### Total time taken for Reducing is " + ((endTimeReducing - startTimeReducing) / 1000.0) + " seconds");

    // Collect and print results
    long startTimeCollecting = System.currentTimeMillis();
    LOGGER.info("##### Output #####");
    List<Tuple2<String, Double>> output = averageSalesByCategory.collect();
    long endTimeCollecting = System.currentTimeMillis();
    // for (Tuple2<?, ?> tuple : output) {
    // System.out.println(tuple._1() + ": " + tuple._2());
    // }
    LOGGER
        .info("##### Total time taken for Collecting is " + ((endTimeCollecting - startTimeCollecting) / 1000.0)
            + " seconds");

    // Stop the Spark context
    sc.stop();
    sc.close();
  }
}
