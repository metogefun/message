package cn.finegames.message.gateway.application.impl;

import cn.finegames.message.gateway.domain.message.MessageCategoryEnum;
import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

/**
 * 消息发送事件
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Getter
@ToString
public class MessageSendEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public MessageSendEvent(Object source) {
        super(source);
    }


    private long messageId;

    private MessageCategoryEnum messageCategoryEnum;

    /**
     * 消息体
     */
    private byte[] body;


    public void setMessageId(long messageId) {
        Preconditions.checkArgument(messageId > 0L, "message's id must > 0");
        this.messageId = messageId;
    }

    public void setMessageCategoryEnum(MessageCategoryEnum messageCategoryEnum) {
        Preconditions.checkArgument(Objects.nonNull(messageCategoryEnum), "message's category can't be null");
        this.messageCategoryEnum = messageCategoryEnum;
    }

    public void setBody(byte[] body) {
        Preconditions.checkArgument(Objects.nonNull(body) && body.length > 0, "message body can't be null");
        this.body = body;
    }
}
