package com.aadityadesigners.tutorial.spring_reactive_validation.configurations;

import com.aadityadesigners.tutorial.spring_reactive_validation.handlers.OfferHandler;
import com.aadityadesigners.tutorial.spring_reactive_validation.utils.ApiPathConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class OfferRouter {

  @Bean
  public RouterFunction<ServerResponse> route(OfferHandler offerHandler) {
    return (RouterFunction<ServerResponse>) RouterFunctions
        .route(
            POST(ApiPathConstants.OFFERS_SETUP).and(accept(MediaType.APPLICATION_JSON)),
            offerHandler::createOffer
        )
        .andOther(RouterFunctions
            .route(
                RequestPredicates.path("/api/**"),
                request -> ServerResponse.badRequest().build()
            )
        );
  }
}
