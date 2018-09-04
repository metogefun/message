package cn.finegames.message.gateway.infrastructure.messaging.rabbit.event;

import cn.finegames.message.gateway.infrastructure.messaging.rabbit.MessageCorrelationData;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * message retry event
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class MessageRetryEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public MessageRetryEvent(Object source) {
        super(source);
    }

    private String routingKey;

    private MessageCorrelationData correlationData;
}
