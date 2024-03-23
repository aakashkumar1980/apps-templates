package com.aadityadesigners.tutorial.spring_reactive_security.configurations;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import com.aadityadesigners.tutorial.spring_reactive_security.handlers.OfferHandler;
import com.aadityadesigners.tutorial.spring_reactive_security.utils.ApiPathConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class OfferRouter {

  private static final Logger logger = LogManager.getLogger(OfferRouter.class);

  @Bean
  public RouterFunction<ServerResponse> route(OfferHandler offerHandler) {
    logger.info("Setting up the Routes...");
    logger.info("Registered Route: Method = {}, Path = {}", "POST", ApiPathConstants.OFFERS_SETUP_V1);

    return (RouterFunction<ServerResponse>) RouterFunctions
        .route(
            POST(ApiPathConstants.OFFERS_SETUP_V1).and(accept(MediaType.APPLICATION_JSON)),
            offerHandler::createOffer
        )
        .andOther(RouterFunctions
            .route(
                RequestPredicates.path(ApiPathConstants.API_ALL),
                request -> ServerResponse.badRequest().build()
            )
        );


  }

}
