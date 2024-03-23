package com.aadityadesigners.tutorial.spring_reactive_validation;


import com.aadityadesigners.tutorial.spring_reactive_validation.configurations.OfferRouter;
import com.aadityadesigners.tutorial.spring_reactive_validation.handlers.OfferHandler;
import com.aadityadesigners.tutorial.spring_reactive_validation.models.Offer;
import com.aadityadesigners.tutorial.spring_reactive_validation.models.OfferConstruct;
import com.aadityadesigners.tutorial.spring_reactive_validation.models.TermsAndConditions;
import com.aadityadesigners.tutorial.spring_reactive_validation.service.OfferService;
import com.aadityadesigners.tutorial.spring_reactive_validation.utils.ApiPathConstants;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
@Import({OfferRouter.class, OfferHandler.class})
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

    /**
     * curl -X POST -H "Content-Type: application/json" -d '{"id":"offer1","name":"Summer Sale","offerConstruct":{"type":"Discount","description":"Get 20% off on all products","keywords":["summer","sale","discount"]},"budget":5000,"status":"Active","startDate":"2024-03-23","endDate":"2024-04-23","targetAudience":["Young Adults"],"termsAndConditions":{"summary":"Terms apply","fullText":"Full terms and conditions text"}}
     * ' http://localhost:8080/api/v1/offers/setup
     */
    webTestClient.post().uri(ApiPathConstants.OFFERS_SETUP_V1)
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


