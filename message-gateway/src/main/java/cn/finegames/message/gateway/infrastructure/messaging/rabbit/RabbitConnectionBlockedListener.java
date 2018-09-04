package cn.finegames.message.gateway.infrastructure.messaging.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionBlockedEvent;
import org.springframework.amqp.rabbit.connection.ConnectionUnblockedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * rabbit mq connection blocked/unblocked event
 *
 * @author wang zhaohui
 * @since 1.0
 *
 * @see org.springframework.amqp.rabbit.connection.AbstractConnectionFactory.ConnectionBlockedListener
 */
@Slf4j
@Component
public class RabbitConnectionBlockedListener {

    @EventListener
    public void handleConnectionBlockedEvent(ConnectionBlockedEvent event) {
        log.error("RabbitMQ Blocked Connection {}, reason {}!", event.getConnection(), event.getReason());
    }


    @EventListener
    public void handleConnectionUnblockedEvent(ConnectionUnblockedEvent event) {
        log.warn("Rabbit MQ Unblocked {} !", event.getConnection());
    }
}
