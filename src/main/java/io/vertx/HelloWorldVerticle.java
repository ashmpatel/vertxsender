package io.vertx;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;



public class HelloWorldVerticle extends AbstractVerticle
{
    private final Logger logger = LoggerFactory.getLogger(HelloWorldVerticle.class);
    /**
     *
     * @param promise
     */
    @Override
    public void start(final Promise<Void> promise) {
        vertx.createHttpServer().requestHandler(request -> request.response()
                .putHeader("content-type", "text/html; charset=utf-8")
                .end("Hello from Vert.x application")).listen(config().getInteger("http.port", 8080), result -> {
            if (result.succeeded()) {
                logger.info("HTTP server running on port");
                promise.complete();
            } else {
                logger.error("Could not start a HTTP server. ", result.cause());
                promise.fail(result.cause());
            }
        });
    }
}
