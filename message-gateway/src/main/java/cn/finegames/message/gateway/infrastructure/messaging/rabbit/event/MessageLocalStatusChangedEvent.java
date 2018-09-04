package cn.finegames.message.gateway.infrastructure.messaging.rabbit.event;

import cn.finegames.message.gateway.domain.message.MessageCategoryEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * message local status changed event
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class MessageLocalStatusChangedEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public MessageLocalStatusChangedEvent(Object source) {
        super(source);
    }

    /**
     * {@linkplain MessageCategoryEnum}
     */
    private MessageCategoryEnum category;

    /**
     * {@linkplain EventTypeEnum}
     */
    private EventTypeEnum eventType;

    /**
     * Message's id
     */
    private long messageId;

    /**
     * third party reply id
     */
    private String thirdPartyUniqueId;

    private String thirdPartyRespCode;

    private String thirdPartyRespMsg;

    private Date receivedTime;


    /**
     * event type
     */
    public enum EventTypeEnum {
        REPLY,
        CALLBACK
    }
}
