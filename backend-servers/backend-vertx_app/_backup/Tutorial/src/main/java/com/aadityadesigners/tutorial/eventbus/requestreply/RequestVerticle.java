package com.aadityadesigners.tutorial.eventbus.requestreply;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class RequestVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        LOGGER.debug(String.format("%s::verticle started...", RequestVerticle.class.getSimpleName()));
        startPromise.complete();

        final var message = new JsonObject()
            .put("customerId", "1");
        LOGGER.debug(String.format("Sending Request: %s", message.encode()));    
        vertx.eventBus().
            <JsonArray>request(MainVerticle.ADDRESS, message, handler -> {
                if(handler.failed()) {
                    LOGGER.error(String.format("Exception: %s", handler.cause().getMessage()));
                } else {
                    LOGGER.info(String.format("Response (received): %s", handler.result().body()));
                }
            }
        );
    }

}
