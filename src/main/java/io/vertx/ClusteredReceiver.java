package io.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.vertx.Constants.ADDRESS;
import static io.vertx.Constants.NODEA_ADDRESS;
import static io.vertx.Constants.NODEB_ADDRESS;

public class ClusteredReceiver extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(ClusteredReceiver.class);

    private String[] args;

    public ClusteredReceiver(String [] args) {
        this.args = args;
    }

    @Override
    public void start() {
        final EventBus eventBus = vertx.eventBus();
        logger.info("Current Thread Id {} Is Clustered {} ",
                Thread.currentThread().getId(), vertx.isClustered());
        eventBus.consumer(ADDRESS, receivedMessage ->
                logger.info("Received message: {} by thread : {}",
                        receivedMessage.body(),
                        Thread.currentThread().getId()));

        if (args[0].equals("A")) {
            eventBus.consumer(NODEA_ADDRESS, receivedMessage ->
                    logger.info("Received by NODE - A message: {} by thread : {}",
                            receivedMessage.body(),
                            Thread.currentThread().getId()));
        } else {
            eventBus.consumer(NODEB_ADDRESS, receivedMessage ->
                    logger.info("Received by NODE - B message: {} by thread : {}",
                            receivedMessage.body(),
                            Thread.currentThread().getId()));
        }

        logger.info("Receiver ready!");

    }
}
