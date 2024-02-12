package com.aadityadesigners.poc.average;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.PairFunction;
import com.aadityadesigners.poc.average.model.SalesRecord;
import com.aadityadesigners.poc.average.utils.CSVUtils;
import scala.Tuple2;

public class SalesRecordMapper {

  private static final Logger LOGGER = Logger.getLogger(SalesRecordMapper.class);

  public static JavaRDD<SalesRecord> mapLinesToSalesRecords(JavaRDD<String> lines) {
    // Skip the header
    String header = lines.first();
    JavaRDD<String> dataLines = lines.filter(line -> !line.equals(header));

    LOGGER.info("##### mapLinesToSalesRecords(" + Thread.currentThread().getName() + ")");
    return dataLines.map(CSVUtils::parseCSVLineV2);
  }

  public static JavaPairRDD<String, Tuple2<Integer, Integer>> mapToCategorySalesPairs(JavaRDD<SalesRecord> records) {
    LOGGER.info("##### mapToCategorySalesPairs(" + Thread.currentThread().getName() + ")");

    return records.mapToPair(
        (PairFunction<SalesRecord, String, Tuple2<Integer, Integer>>) record -> new Tuple2<>(record.getCategory(),
            new Tuple2<>(record.getSales(), 1)));
  }
}
