package com.aadityadesigners.tutorial.basics;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class VerticleB extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerticleB.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        LOGGER.info(String.format("%s::verticle started...", VerticleB.class.getSimpleName()));

        vertx.deployVerticle(
            VerticleBx.class.getName(), 
            new DeploymentOptions().setInstances(Runtime.getRuntime().availableProcessors()+1),
            postDeployment -> {
                LOGGER.info(String.format("%s::verticle deployed.", VerticleBx.class.getSimpleName()));
        });

        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        LOGGER.info(String.format("%s::verticle un-deployed.", VerticleB.class.getSimpleName()));
        stopPromise.complete();
    }    
}
