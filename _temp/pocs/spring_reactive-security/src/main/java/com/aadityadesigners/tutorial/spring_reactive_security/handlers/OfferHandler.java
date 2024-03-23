package com.aadityadesigners.tutorial.spring_reactive_security.handlers;

import com.aadityadesigners.tutorial.spring_reactive_security.models.Offer;
import com.aadityadesigners.tutorial.spring_reactive_security.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class OfferHandler {

  @Autowired
  private OfferService offerService;

  public Mono<ServerResponse> createOffer(ServerRequest request) {
    Mono<Offer> offerMono = request.bodyToMono(Offer.class);
    return offerMono.flatMap(offer ->
        ServerResponse.status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(offerService.createOffer(offer)));
  }
}
