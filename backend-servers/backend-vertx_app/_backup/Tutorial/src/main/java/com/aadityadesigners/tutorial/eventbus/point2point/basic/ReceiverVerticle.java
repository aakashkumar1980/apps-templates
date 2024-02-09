package com.aadityadesigners.tutorial.eventbus.point2point.basic;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

public class ReceiverVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        LOGGER.debug(String.format("%s::verticle started...", ReceiverVerticle.class.getSimpleName()));
        startPromise.complete();

        vertx.eventBus().
            <JsonObject>consumer(MainVerticle.ADDRESS, handler -> {
                LOGGER.info(String.format("Message (received): %s", handler.body()));
            }
        );
    }

}
