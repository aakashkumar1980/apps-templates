package com.aadityadesigners.tutorial.eventbus.requestreply;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class ResponseVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        LOGGER.debug(String.format("%s::verticle started...", ResponseVerticle.class.getSimpleName()));
        startPromise.complete();

        vertx.eventBus().
            <JsonObject>consumer(MainVerticle.ADDRESS, handler -> {
                LOGGER.info(String.format("Request (received): %s", handler.body()));

                final var customer = new JsonObject()
                    .put("customerId", handler.body().getString("customerId"));
                final var taxDue = new JsonObject()
                    .put("taxDue", "$12,000");    
                final var message = new JsonArray().add(customer).add(taxDue);    
                LOGGER.debug(String.format("Sending Response: %s", message.encode()));
                handler.replyAndRequest(message, replyHandler -> {
                    if (replyHandler.failed()) {
                        LOGGER.error(String.format("Exception: %s", replyHandler.cause().getMessage()));
                    }
                });
            }
        );
    }

}
