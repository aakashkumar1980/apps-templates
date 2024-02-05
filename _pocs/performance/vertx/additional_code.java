import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.vertx.core.Vertx;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        Vertx vertx = context.getBean(Vertx.class);
        OfferServiceVerticle offerServiceVerticle = context.getBean(OfferServiceVerticle.class);

        vertx.deployVerticle(offerServiceVerticle, res -> {
            if (res.succeeded()) {
                System.out.println("OfferServiceVerticle deployed successfully!");
            } else {
                System.out.println("Failed to deploy OfferServiceVerticle: " + res.cause());
            }
        });
    }

    @Bean
    public Vertx vertx() {
        return Vertx.vertx();
    }
}


@Component
public class OfferServiceVerticle extends AbstractVerticle {

    private final OfferRouter offerRouter;
    private final MainRouter mainRouter;

    @Autowired
    public OfferServiceVerticle(MainRouter mainRouter, OfferRouter offerRouter) {
        this.mainRouter = mainRouter;
        this.offerRouter = offerRouter;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        // Initialize the main Router
        Router router = mainRouter.getRouter(vertx);
        
        // Bind the OfferRouter routes under a specific path, e.g., "/api"
        router.mountSubRouter("/api", offerRouter.getRouter(vertx));

        // Create the HTTP server and listen on port 8080
        vertx.createHttpServer().requestHandler(router).listen(8080, http -> {
            if (http.succeeded()) {
                startPromise.complete();
                System.out.println("HTTP server started on port 8080");
            } else {
                startPromise.fail(http.cause());
            }
        });
    }
}


import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.springframework.stereotype.Component;

@Component
public class MainRouter {

    // Method to initialize the main Router
    public Router getRouter(Vertx vertx) {
        return Router.router(vertx);
        // Here you can setup global routes or middleware if needed
    }
}


import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OfferRouter {

    private final OfferService offerService;

    @Autowired
    public OfferRouter(OfferService offerService) {
        this.offerService = offerService;
    }

    // Method to initialize the Offer-specific Router
    public Router getRouter(Vertx vertx) {
        Router router = Router.router(vertx);
        
        // Setup offer-specific routes
        router.get("/offers").handler(routingContext -> {
            // Example handler that uses offerService to fetch offers and respond
        });
        
        return router;
    }
}

