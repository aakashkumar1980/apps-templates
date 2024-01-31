package com.aadityadesigners.tutorial.basics;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;


public class MainVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle(), postDeployment -> {
      LOGGER.info(String.format("%s::verticle deployed.", MainVerticle.class.getSimpleName()));
    });
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOGGER.info(String.format("%s::verticle started...", MainVerticle.class.getSimpleName()));
    
    vertx.deployVerticle(new VerticleA(), postDeployment -> {
      LOGGER.info(String.format("%s::verticle deployed.", VerticleA.class.getSimpleName()));
    });
    vertx.deployVerticle(new VerticleB(), postDeployment -> {
      LOGGER.info(String.format("%s::verticle deployed.", VerticleB.class.getSimpleName()));
    });
    startPromise.complete();
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    LOGGER.info(String.format("%s::verticle un-deployed.", MainVerticle.class.getSimpleName()));
    stopPromise.complete();
  }
}
