package com.aadityadesigners.tutorial.spring_reactive_security.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TermsAndConditions {
  private String summary;
  private String fullText;
}
