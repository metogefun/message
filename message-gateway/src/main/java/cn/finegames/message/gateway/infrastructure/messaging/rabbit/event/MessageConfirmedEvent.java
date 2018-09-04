package cn.finegames.message.gateway.infrastructure.messaging.rabbit.event;

import cn.finegames.message.gateway.domain.message.MessageLocalStatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * message's confirmed event
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class MessageConfirmedEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public MessageConfirmedEvent(Object source) {
        super(source);
    }

    /**
     * message's id
     */
    private long messageId;

    /**
     * {@linkplain MessageLocalStatusEnum}
     */
    private MessageLocalStatusEnum messageLocalStatus;
}
