package com.aadityadesigners.tutorial.spring_reactive_validation.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Promotion {

  private String promoCode; // tokenized value
  private String description;
  private String redemptionCode; // encrypted value
}
