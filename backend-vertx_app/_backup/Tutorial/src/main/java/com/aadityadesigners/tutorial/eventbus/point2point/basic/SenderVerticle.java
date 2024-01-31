package com.aadityadesigners.tutorial.eventbus.point2point.basic;

import java.util.HashMap;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

public class SenderVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(SenderVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        LOGGER.debug(String.format("%s::verticle started...", SenderVerticle.class.getSimpleName()));
        startPromise.complete();

        final var map = new HashMap<String, Object>();
        map.put("customerId", 1);
        map.put("customerName", "Aakash Kumar");
        var message = new JsonObject(map);
        LOGGER.debug(String.format("Sending Message: %s", message.encode()));
        vertx.eventBus().send(MainVerticle.ADDRESS, new JsonObject(map));
    }

}
