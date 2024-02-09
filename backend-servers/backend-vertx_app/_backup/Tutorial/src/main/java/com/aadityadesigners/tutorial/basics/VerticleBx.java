package com.aadityadesigners.tutorial.basics;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class VerticleBx extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerticleBx.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        LOGGER.info(String.format("%s::verticle started...", VerticleBx.class.getSimpleName()));

        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        LOGGER.info(String.format("%s::verticle un-deployed.", VerticleBx.class.getSimpleName()));
        stopPromise.complete();
    }    
}
