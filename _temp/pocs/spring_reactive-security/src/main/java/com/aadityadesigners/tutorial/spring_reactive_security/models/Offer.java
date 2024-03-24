package com.aadityadesigners.tutorial.spring_reactive_security.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Offer {
  private String id; // hashed value
  private String name;
  private BigDecimal budget;
  private String status;
  private LocalDate startDate;
  private LocalDate endDate;
  private List<String> targetAudience;

  private Promotion promotion;
  private OfferConstruct offerConstruct;
  private TermsAndConditions termsAndConditions;

}

