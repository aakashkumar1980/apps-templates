package com.aadityadesigners.tutorial.spring_reactive_validation.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Offer {
  private String id;
  private String name;
  private OfferConstruct offerConstruct;
  private BigDecimal budget;
  private String status;
  private LocalDate startDate;
  private LocalDate endDate;
  private List<String> targetAudience;
  private TermsAndConditions termsAndConditions;

}

