package com.aadityadesigners.tutorial.spring_reactive_validation;


import com.aadityadesigners.tutorial.spring_reactive_validation.models.Offer;
import com.aadityadesigners.tutorial.spring_reactive_validation.models.OfferConstruct;
import com.aadityadesigners.tutorial.spring_reactive_validation.models.TermsAndConditions;
import com.aadityadesigners.tutorial.spring_reactive_validation.service.OfferService;
import com.aadityadesigners.tutorial.spring_reactive_validation.utils.ApiPathConstants;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.aadityadesigners.tutorial.spring_reactive_validation.handlers.OfferHandler;

import java.math.BigDecimal;
import java.time.LocalDate;

@WebFluxTest
@Import({OfferHandler.class})
public class OfferSetupTest {

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private OfferService offerService;

  @Test
  public void testCreateOffer() {
    OfferConstruct offerConstruct = new OfferConstruct();
    offerConstruct.setType("Discount");
    offerConstruct.setDescription("Get 20% off on all products");
    offerConstruct.setKeywords(Arrays.asList("summer", "sale", "discount"));

    TermsAndConditions termsAndConditions = new TermsAndConditions();
    termsAndConditions.setSummary("Terms apply");
    termsAndConditions.setFullText("Full terms and conditions text");

    Offer offer = new Offer();
    offer.setId("offer1");
    offer.setName("Summer Sale");
    offer.setOfferConstruct(offerConstruct);
    offer.setBudget(new BigDecimal("5000"));
    offer.setStatus("Active");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusMonths(1));
    offer.setTargetAudience(Arrays.asList("Young Adults"));
    offer.setTermsAndConditions(termsAndConditions);

    Mockito.when(offerService.createOffer(Mockito.any(Offer.class))).thenReturn(offer);

    webTestClient.post().uri(ApiPathConstants.OFFERS_SETUP)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(offer)
        .exchange()
        .expectStatus().isCreated()
        .expectBody()
        .jsonPath("$.id").isEqualTo(offer.getId())
        .jsonPath("$.name").isEqualTo(offer.getName())
        .jsonPath("$.offerConstruct.type").isEqualTo(offer.getOfferConstruct().getType())
        .jsonPath("$.budget").isEqualTo(offer.getBudget())
        .jsonPath("$.status").isEqualTo(offer.getStatus())
        .jsonPath("$.startDate").isEqualTo(offer.getStartDate().toString())
        .jsonPath("$.endDate").isEqualTo(offer.getEndDate().toString())
        .jsonPath("$.targetAudience[0]").isEqualTo(offer.getTargetAudience().get(0))
        .jsonPath("$.termsAndConditions.summary").isEqualTo(offer.getTermsAndConditions().getSummary());
  }
}


