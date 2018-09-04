package cn.finegames.message.gateway.infrastructure.messaging.rabbit;

import static cn.finegames.commons.protocol.Message.*;

import cn.finegames.message.gateway.domain.message.MessageLocalStatusEnum;
import cn.finegames.message.gateway.infrastructure.messaging.rabbit.event.MessageConfirmedEvent;
import cn.finegames.message.gateway.infrastructure.messaging.rabbit.event.MessageRetryEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import static cn.finegames.message.gateway.domain.message.MessageCategoryEnum.SMS;


/**
 * rabbit mq confirm callback
 * <p>
 * Use optimistic locking to change database records when successful.
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Slf4j
@Component
public class RabbitConfirmCallback implements RabbitTemplate.ConfirmCallback {

    private static Logger rabbitAckLog = LoggerFactory.getLogger("rabbit_ack");

    private static final int MAX_RETRY_TIME = 3;

    @Autowired
    private RabbitProperties rabbitProperties;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!(correlationData instanceof MessageCorrelationData)) {
            return;
        }

        MessageCorrelationData messageCorrelationData = (MessageCorrelationData) correlationData;
        if (!ack) {
            // 重试
            int retryTime = messageCorrelationData.incrementRetryCount();

            // 超过最大重试次数存储在文件中
            if (retryTime > MAX_RETRY_TIME) {
                try {
                    MessageStruct messageStruct = MessageStruct.parseFrom(messageCorrelationData.getMessage().getBody());
                    rabbitAckLog.warn(messageStruct.toString());
                    log.info("message {} retry timeout!", messageStruct);
                } catch (InvalidProtocolBufferException e) {
                    log.error("message {} convert to proto error", messageCorrelationData.getMessage(), e);
                }
            } else {
                // 未超过次数，发送重试事件
                MessageRetryEvent event = new MessageRetryEvent(this);
                event.setCorrelationData(messageCorrelationData);
                event.setRoutingKey(SMS.equals(messageCorrelationData.getMessageCategory()) ?
                        rabbitProperties.getSendSmsRoutingKey() : rabbitProperties.getSendDingRoutingKey());
                publisher.publishEvent(event);
                log.info("event {} retry time {}", event, retryTime);
            }
        } else {
            // 修改记录状态
            MessageConfirmedEvent event = new MessageConfirmedEvent(this);
            event.setMessageId(messageCorrelationData.getMessageId());
            event.setMessageLocalStatus(MessageLocalStatusEnum.SENT);
            publisher.publishEvent(event);
        }
    }
}
