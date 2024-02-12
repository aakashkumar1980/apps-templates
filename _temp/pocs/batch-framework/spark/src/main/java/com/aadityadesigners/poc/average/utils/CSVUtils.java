package com.aadityadesigners.poc.average.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.aadityadesigners.poc.average.model.SalesRecord;

import java.io.StringReader;

public class CSVUtils {

  public static SalesRecord parseCSVLine(String line) {
    // Assuming no internal commas affect the last two fields
    String[] parts = line.split(",");
    // Adjusted to reflect accurate field positions based on a 0-based index
    String category = parts[7].trim(); // 8th field for category
    Integer sales = Integer.parseInt(parts[8].trim()); // 9th field for sales
    return new SalesRecord(category, sales);
  }

  public static SalesRecord parseCSVLineV2(String line) {
    try {
      CSVParser parser = CSVFormat.DEFAULT.parse(new StringReader(line));
      CSVRecord record = parser.getRecords().get(0);
      String category = record.get(7).trim(); // Adjust index if necessary
      Integer sales = Integer.parseInt(record.get(8).trim()); // Adjust index if necessary
      return new SalesRecord(category, sales);
    } catch (Exception e) {
      e.printStackTrace();
      return null; // Or handle more gracefully
    }
  }
}
