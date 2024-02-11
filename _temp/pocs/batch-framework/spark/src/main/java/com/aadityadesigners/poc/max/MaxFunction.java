package com.aadityadesigners.poc.max;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.function.Function2;

public class MaxFunction implements Function2<Integer, Integer, Integer> {
  private static final Logger LOGGER = Logger.getLogger(MaxFunction.class);

  @Override
  public Integer call(Integer a, Integer b) throws Exception {
    LOGGER.info("##### Comparing: " + a + " and " + b);
    return Math.max(a, b);
  }
}
