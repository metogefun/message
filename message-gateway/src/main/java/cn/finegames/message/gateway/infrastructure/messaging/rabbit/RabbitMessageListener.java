package cn.finegames.message.gateway.infrastructure.messaging.rabbit;

import cn.finegames.message.gateway.domain.message.MessageCategoryEnum;
import cn.finegames.message.gateway.infrastructure.messaging.rabbit.event.MessageLocalStatusChangedEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Date;

import static cn.finegames.commons.protocol.Message.*;

/**
 * message
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Slf4j
@Component
public class RabbitMessageListener implements MessageListener {

    /**
     * 短信消息类型
     */
    private static final String SMS_TYPE = "sms";

    /**
     * 钉钉消息类型
     */
    private static final String DING_TYPE = "ding";

    /**
     * 回调事件
     */
    private static final String CALLBACK = "callback";

    /**
     * 回复消息类型
     */
    private static final String REPLY = "reply";

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public void onMessage(Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();

        // 回調事件
        if (routingKey.contains(CALLBACK)) {
            try {

                MessageCallback messageCallback = MessageCallback.parseFrom(message.getBody());
                int count = messageCallback.getContentsCount();
                MessageLocalStatusChangedEvent messageLocalStatusChangedEvent;
                if (count > 0) {
                    for (MessageCallbackContent content : messageCallback.getContentsList()) {
                        messageLocalStatusChangedEvent = new MessageLocalStatusChangedEvent(this);
                        if (routingKey.contains(SMS_TYPE)) {
                            messageLocalStatusChangedEvent.setCategory(MessageCategoryEnum.SMS);
                        } else if (routingKey.contains(DING_TYPE)) {
                            messageLocalStatusChangedEvent.setCategory(MessageCategoryEnum.DING);
                        }
                        messageLocalStatusChangedEvent.setEventType(MessageLocalStatusChangedEvent.EventTypeEnum.CALLBACK);
                        messageLocalStatusChangedEvent.setThirdPartyUniqueId(content.getTaskId());
                        messageLocalStatusChangedEvent.setThirdPartyRespCode(content.getStatus());
                        messageLocalStatusChangedEvent.setThirdPartyRespMsg(content.getCode());
                        messageLocalStatusChangedEvent.setReceivedTime(new Date(content.getReceiveTime()));
                        publisher.publishEvent(messageLocalStatusChangedEvent);
                    }
                }
            } catch (InvalidProtocolBufferException e) {
                log.info("rabbit mq queue message listener, routing key is {}, convert to CallbackSms error",
                        routingKey, e);
            }
        } else if (routingKey.contains(DING_TYPE)) {
            try {
                MessageReply messageReply = MessageReply.parseFrom(message.getBody());
                MessageLocalStatusChangedEvent messageLocalStatusChangedEvent = new MessageLocalStatusChangedEvent(this);
                if (routingKey.contains(SMS_TYPE)) {
                    messageLocalStatusChangedEvent.setCategory(MessageCategoryEnum.SMS);
                } else if (routingKey.contains(DING_TYPE)) {
                    messageLocalStatusChangedEvent.setCategory(MessageCategoryEnum.DING);
                }
                messageLocalStatusChangedEvent.setMessageId(messageReply.getMessageId());
                messageLocalStatusChangedEvent.setThirdPartyUniqueId(messageReply.getTaskId());
                messageLocalStatusChangedEvent.setThirdPartyRespCode(messageReply.getCode());
                messageLocalStatusChangedEvent.setThirdPartyRespMsg(messageReply.getErrorMessage());
                publisher.publishEvent(messageLocalStatusChangedEvent);
            } catch (InvalidProtocolBufferException e) {
                log.info("rabbit mq queue message listener, routing key is {}, convert to ReplaySms error",
                        routingKey, e);
            }
        }
    }
}
