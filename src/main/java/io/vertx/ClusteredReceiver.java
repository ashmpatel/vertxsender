package io.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.vertx.Constants.ADDRESS;

public class ClusteredReceiver extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(ClusteredReceiver.class);

    @Override
    public void start() {
        final EventBus eventBus = vertx.eventBus();
        logger.info("Current Thread Id {} Is Clustered {} ",
                Thread.currentThread().getId(), vertx.isClustered());
        eventBus.consumer(ADDRESS, receivedMessage -> logger.info("Received message: {} by thread : {}", receivedMessage.body(), Thread.currentThread().getId()));
        logger.info("Receiver ready!");

    }
}
