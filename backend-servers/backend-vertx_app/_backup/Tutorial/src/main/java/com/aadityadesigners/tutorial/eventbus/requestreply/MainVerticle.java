package com.aadityadesigners.tutorial.eventbus.requestreply;

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
    
    vertx.deployVerticle(new RequestVerticle(), postDeployment -> {
      LOGGER.debug(String.format("%s::verticle deployed.", RequestVerticle.class.getSimpleName()));
    });
    vertx.deployVerticle(new ResponseVerticle(), postDeployment -> {
      LOGGER.debug(String.format("%s::verticle deployed.", ResponseVerticle.class.getSimpleName()));
    });
    startPromise.complete();
  }

}
