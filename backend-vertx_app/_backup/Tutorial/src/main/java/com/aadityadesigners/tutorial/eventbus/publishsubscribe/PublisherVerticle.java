package com.aadityadesigners.tutorial.eventbus.publishsubscribe;

import java.util.HashMap;

import com.aadityadesigners.tutorial._common.models.Customer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

public class PublisherVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        LOGGER.debug(String.format("%s::verticle started...", PublisherVerticle.class.getSimpleName()));
        startPromise.complete();

        var customer = Customer.builder()
            .customerId(1)
            .customerName("Aaditya Kumar")
        .build();
        var message = JsonObject.mapFrom(customer);
        LOGGER.debug(String.format("Publishing Message: %s", message.encode())); 
        vertx.eventBus().publish(MainVerticle.ADDRESS, JsonObject.mapFrom(customer));
    }

}
