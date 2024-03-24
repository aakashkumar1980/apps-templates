package com.aadityadesigners.tutorial.spring_reactive_security;


import com.aadityadesigners.tutorial.spring_reactive_security.configurations.OfferRouter;
import com.aadityadesigners.tutorial.spring_reactive_security.handlers.OfferHandler;
import com.aadityadesigners.tutorial.spring_reactive_security.models.Offer;
import com.aadityadesigners.tutorial.spring_reactive_security.models.OfferConstruct;
import com.aadityadesigners.tutorial.spring_reactive_security.models.Promotion;
import com.aadityadesigners.tutorial.spring_reactive_security.models.TermsAndConditions;
import com.aadityadesigners.tutorial.spring_reactive_security.service.OfferService;
import com.aadityadesigners.tutorial.spring_reactive_security.utils.ApiPathConstants;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import org.apache.commons.codec.digest.DigestUtils;
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

    Promotion promotion = new Promotion();
    promotion.setPromoCode("abcd-efgh-ijkl-mnop"); // tokenized value of "PROMO20"
    promotion.setDescription("20% off on all products");
    promotion.setRedemptionCode("U2FsdGVkX1+8Jv3FZg+8WpR/3b9aaF7zFgxQeAGtztc="); // encrypted value of "REDEEM20"

    TermsAndConditions termsAndConditions = new TermsAndConditions();
    termsAndConditions.setSummary("Terms apply");
    termsAndConditions.setFullText("Full terms and conditions text");

    Offer offer = new Offer();
    offer.setId(DigestUtils.sha256Hex("offer1")); // hashed value
    offer.setName("Summer Sale");
    offer.setBudget(new BigDecimal("5000"));
    offer.setStatus("Active");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusMonths(1));
    offer.setTargetAudience(Arrays.asList("Young Adults"));
    offer.setOfferConstruct(offerConstruct);
    offer.setPromotion(promotion);
    offer.setTermsAndConditions(termsAndConditions);

    Mockito.when(offerService.createOffer(Mockito.any(Offer.class))).thenReturn(offer);

    /**
     * curl -X POST -H "Content-Type: application/json" -d '{"id":"ab0c3a7cc79c2ad5ffaa2600c6de2fdff3409f07d50c06350d6cdef52bd3c4c9","name":"Summer Sale","budget":5000,"status":"Active","startDate":"2024-03-24","endDate":"2024-04-24","targetAudience":["Young Adults"],"promotion":{"promoCode":"abcd-efgh-ijkl-mnop","description":"20% off on all products","redemptionCode":"U2FsdGVkX1+8Jv3FZg+8WpR/3b9aaF7zFgxQeAGtztc="},"offerConstruct":{"type":"Discount","description":"Get 20% off on all products","keywords":["summer","sale","discount"]},"termsAndConditions":{"summary":"Terms apply","fullText":"Full terms and conditions text"}}' http://localhost:8080/api/v1/offers/setup
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


