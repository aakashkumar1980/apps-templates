package com.aadityadesigners.tutorial.basics;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class VerticleAx extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerticleAx.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        LOGGER.info(String.format("%s::verticle started...", VerticleAx.class.getSimpleName()));

        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        LOGGER.info(String.format("%s::verticle un-deployed.", VerticleAx.class.getSimpleName()));
        stopPromise.complete();
    }
}
