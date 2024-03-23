package com.aadityadesigners.tutorial.spring_reactive_validation.models;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OfferConstruct {
  private String type;
  private String description;
  private List<String> keywords;
}