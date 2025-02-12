package io.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import static io.vertx.Constants.*;


public class ClusteredSender extends AbstractVerticle
{

    private final Logger logger = LoggerFactory.getLogger(ClusteredSender.class);

    /**
     *
     * @param promise
     */
    @Override
    public void start(Promise<Void> promise) {
        final Router router = RouterHelper.createRouter(vertx, "Hello from clustered messenger example!");
        router.post("/sendForAll/:" + PATH_PARAM_TO_RECEIVE_MESSAGE).handler(this::sendMessageForAllReceivers);
        router.post("/sendToNodeA/:" + PATH_PARAM_TO_RECEIVE_MESSAGE).handler(this::sendToNodeAOnly);
        router.post("/sendToNodeB/:" + PATH_PARAM_TO_RECEIVE_MESSAGE).handler(this::sendToNodeBOnly);
        HttpServerHelper.createAnHttpServer(vertx, router, config(), promise);
    }

    /**
     *
     * @param routingContext
     */
    private void sendMessageForAllReceivers(RoutingContext routingContext){
        final EventBus eventBus = vertx.eventBus();
        final String message = routingContext.request().getParam(PATH_PARAM_TO_RECEIVE_MESSAGE);
        logger.info("Clustered SEND : {}",message);
        eventBus.publish(ADDRESS, message);
        logger.info("Current Thread Id {} Is Clustered {} ",
                Thread.currentThread().getId(), vertx.isClustered());
        routingContext.response().end(message);
    }

    private void sendToNodeAOnly(RoutingContext routingContext){
        final EventBus eventBus = vertx.eventBus();
        final String message = routingContext.request().getParam(PATH_PARAM_TO_RECEIVE_MESSAGE);
        logger.info("Point to Point SEND to Node A : {}",message);
        eventBus.send(NODEA_ADDRESS, message);
        logger.info("Current Thread Id {} Is Clustered {} ",
                Thread.currentThread().getId(), vertx.isClustered());
        routingContext.response().end(message);
    }

    private void sendToNodeBOnly(RoutingContext routingContext){
        final EventBus eventBus = vertx.eventBus();
        final String message = routingContext.request().getParam(PATH_PARAM_TO_RECEIVE_MESSAGE);
        logger.info("Point to Point SEND to Node B : {}",message);
        eventBus.send(NODEB_ADDRESS, message);
        logger.info("Current Thread Id {} Is Clustered {} ",
                Thread.currentThread().getId(), vertx.isClustered());
        routingContext.response().end(message);
    }

}
