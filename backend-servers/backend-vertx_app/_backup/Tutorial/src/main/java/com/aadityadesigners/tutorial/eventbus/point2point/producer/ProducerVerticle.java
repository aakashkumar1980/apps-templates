package com.aadityadesigners.tutorial.eventbus.point2point.producer;

import com.aadityadesigners.tutorial._common.models.Customer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

public class ProducerVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerVerticle.class);

    MessageProducer<JsonObject> messageProducer;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        LOGGER.debug(String.format("%s::verticle started...", ProducerVerticle.class.getSimpleName()));
        startPromise.complete();

        var customer = Customer.builder()
            .customerId(1)
            .customerName("Aaditya Kumar")
        .build();
        var message = JsonObject.mapFrom(customer);
        LOGGER.debug(String.format("Sending Message: %s", message.encode())); 
        messageProducer = vertx.eventBus().sender(MainVerticle.ADDRESS);
        messageProducer.write(message, handler -> {
            if (handler.failed()) {
                LOGGER.error(String.format("Exception: %s", handler.cause().getMessage()));
            }
        });
    }

    @Override
    public void stop(final Promise<Void> stopPromise) throws Exception {
        messageProducer.close(whenDone -> stopPromise.complete());
    }
}
