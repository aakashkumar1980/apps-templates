package com.aadityadesigners.poc.average.model;

public class SalesRecord {
  private String category;
  private Integer sales;

  public SalesRecord(String category, Integer sales) {
    this.category = category;
    this.sales = sales;
  }

  public String getCategory() {
    return category;
  }

  public Integer getSales() {
    return sales;
  }
}
