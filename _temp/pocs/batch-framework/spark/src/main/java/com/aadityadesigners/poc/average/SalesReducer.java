package com.aadityadesigners.poc.average;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function2;
import scala.Tuple2;

public class SalesReducer {
  private static final Logger LOGGER = Logger.getLogger(SalesReducer.class);

  public static JavaPairRDD<String, Tuple2<Integer, Integer>> reduceCategorySalesPairs(
      JavaPairRDD<String, Tuple2<Integer, Integer>> categorySalesPairs) {
    LOGGER.info("##### reduceCategorySalesPairs(" + Thread.currentThread().getName() + ")");

    return categorySalesPairs.reduceByKey(
        (Function2<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>) (tuple1,
            tuple2) -> new Tuple2<>(tuple1._1 + tuple2._1, tuple1._2 + tuple2._2));
  }

  public static JavaPairRDD<String, Double> calculateAverageSalesByCategory(
      JavaPairRDD<String, Tuple2<Integer, Integer>> reducedPairs) {
    LOGGER.info("##### calculateAverageSalesByCategory(" + Thread.currentThread().getName() + ")");

    return reducedPairs.mapValues(
        tuple -> tuple._1.doubleValue() / tuple._2);
  }
}
