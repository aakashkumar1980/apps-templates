package com.aadityadesigners.tutorial.basics;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class VerticleA extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerticleA.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        LOGGER.info(String.format("%s::verticle started...", VerticleA.class.getSimpleName()));

        vertx.deployVerticle(new VerticleAx(), postDeployment -> {
            LOGGER.info(String.format("%s::verticle deployed.", VerticleAx.class.getSimpleName()));
            vertx.undeploy(postDeployment.result());
        });

        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        LOGGER.info(String.format("%s::verticle un-deployed.", VerticleA.class.getSimpleName()));
        stopPromise.complete();
    }

}
