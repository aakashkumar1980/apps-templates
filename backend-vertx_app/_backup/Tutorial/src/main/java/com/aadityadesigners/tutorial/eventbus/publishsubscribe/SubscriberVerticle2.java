package com.aadityadesigners.tutorial.eventbus.publishsubscribe;

import com.aadityadesigners.tutorial._common.models.Customer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

public class SubscriberVerticle2 extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriberVerticle2.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        LOGGER.debug(String.format("%s::verticle started...", SubscriberVerticle2.class.getSimpleName()));
        startPromise.complete();

        vertx.eventBus().
            <JsonObject>consumer(MainVerticle.ADDRESS, handler -> {
                JsonObject body = handler.body();
                var customer = body.mapTo(Customer.class);
                LOGGER.info(String.format("Message (received): %s", customer.toString()));                
            }
        );
    }

}
