package com.aadityadesigners.tutorial.eventbus.point2point.basic;

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
      LOGGER.debug(String.format("%s::verticle deployed.", MainVerticle.class.getSimpleName()));
    });
  }

  static final String ADDRESS = MainVerticle.class.getSimpleName()+"_ADDRESS";
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOGGER.debug(String.format("%s::verticle started...", MainVerticle.class.getSimpleName()));
    
    vertx.deployVerticle(new ReceiverVerticle(), postDeployment -> {
      LOGGER.debug(String.format("%s::verticle deployed.", ReceiverVerticle.class.getSimpleName()));
    });
    vertx.deployVerticle(new SenderVerticle(), postDeployment -> {
      LOGGER.debug(String.format("%s::verticle deployed.", SenderVerticle.class.getSimpleName()));
    });
    startPromise.complete();
  }

}
