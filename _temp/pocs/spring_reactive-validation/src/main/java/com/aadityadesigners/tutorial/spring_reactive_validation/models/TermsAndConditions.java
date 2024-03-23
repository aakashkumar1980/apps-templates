package com.aadityadesigners.tutorial.spring_reactive_validation.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TermsAndConditions {
  private String summary;
  private String fullText;
}
