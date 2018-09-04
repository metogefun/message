package cn.finegames.message.gateway.application.impl;

import cn.finegames.commons.util.exception.DBException;
import cn.finegames.message.gateway.application.MessageEventService;
import cn.finegames.message.gateway.domain.message.Message;
import cn.finegames.message.gateway.domain.message.MessageCategoryEnum;
import cn.finegames.message.gateway.domain.message.MessageLocalStatusEnum;
import cn.finegames.message.gateway.domain.message.MessageRepository;
import cn.finegames.message.gateway.infrastructure.messaging.rabbit.MessageCorrelationData;
import cn.finegames.message.gateway.infrastructure.messaging.rabbit.RabbitProperties;
import cn.finegames.message.gateway.infrastructure.messaging.rabbit.event.MessageConfirmedEvent;
import cn.finegames.message.gateway.infrastructure.messaging.rabbit.event.MessageLocalStatusChangedEvent;
import cn.finegames.message.gateway.infrastructure.messaging.rabbit.event.MessageRetryEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Objects;

import static cn.finegames.message.gateway.domain.message.MessageCategoryEnum.SMS;

/**
 * message event service implement
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Slf4j
@Service
public class MessageEventServiceImpl implements MessageEventService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitProperties rabbitProperties;

    @Override
    @TransactionalEventListener
    public void onMessageSendEvent(MessageSendEvent messageSendEvent) {
        byte[] body = messageSendEvent.getBody();
        long messageId = messageSendEvent.getMessageId();
        MessageCategoryEnum messageCategory = messageSendEvent.getMessageCategoryEnum();

        MessageProperties messageProperties = new MessageProperties();
        org.springframework.amqp.core.Message msg = MessageBuilder.withBody(body).andProperties(messageProperties).build();
        MessageCorrelationData correlationData = new MessageCorrelationData(msg);
        correlationData.setMessageId(messageId);
        correlationData.setMessageCategory(messageCategory);

        // message.sms.send | message.ding.send
        rabbitTemplate.send(rabbitProperties.getExchangeName(),
                SMS.equals(messageCategory) ?
                        rabbitProperties.getSendSmsRoutingKey() : rabbitProperties.getSendDingRoutingKey(),
                msg, correlationData);
    }

    @Override
    @EventListener
    public void onMessageConfirmEvent(MessageConfirmedEvent event) {
        long messageId = event.getMessageId();
        Message message = messageRepository.selectByPrimaryKey(messageId);
        if (Objects.isNull(message)) {
            log.info("message confirm listener, message id [{}] nonexistent", messageId);
            return;
        }

        MessageLocalStatusEnum localStatus = message.getMessageLocalStatus();
        if (localStatus.getCode() >= event.getMessageLocalStatus().getCode()) {
            log.warn("message confirm listener, message {} local status is {}", messageId, localStatus.getCode());
            return;
        }

        int result = messageRepository.updateLocalStatus(event.getMessageLocalStatus(), messageId,
                message.getMessageLocalStatus(), message.getModifyTime());
        if (result <= 0) {
            log.warn("message {} confirm, change status {} to {} failure.", messageId, message.getMessageLocalStatus(),
                    event.getMessageLocalStatus().getCode());
        } else {
            log.info("message {} confirm, change status {} to {}.", messageId, message.getMessageLocalStatus(),
                    event.getMessageLocalStatus().getCode());
        }
    }

    @Override
    @EventListener
    public void onLocalStatusChangeEvent(MessageLocalStatusChangedEvent event) {
        MessageLocalStatusChangedEvent.EventTypeEnum eventTypeEnum = event.getEventType();

        // 回调
        if (MessageLocalStatusChangedEvent.EventTypeEnum.CALLBACK.equals(eventTypeEnum)) {
            Message message = messageRepository.selectByThirdPartyUniqueId(event.getThirdPartyUniqueId());
            if (Objects.isNull(message)) {
                log.error("third party id {} nonexistent", event.getThirdPartyUniqueId());
                return;
            }

            if (message.getMessageLocalStatus().getCode() >= MessageLocalStatusEnum.CALLBACK.getCode()) {
                log.warn("message id {} local status is {}, need no modification");
                return;
            }

            message.setMessageLocalStatus(MessageLocalStatusEnum.CALLBACK);
            message.setThirdPartyRespCode(event.getThirdPartyRespCode());
            message.setThirdPartyRespMsg(event.getThirdPartyRespMsg());
            message.setReceivedTime(event.getReceivedTime());

            int result = messageRepository.updateByPrimaryKeySelective(message);

            if (result <= 0) {
                log.error("message {} CALLBACK, change status success affect rows is 0");
                throw new DBException("修改消息" + event.getMessageId() + "CALLBACK异常");
            }

            log.info("message {} CALLBACK, change status success");
        } else if (MessageLocalStatusChangedEvent.EventTypeEnum.REPLY.equals(eventTypeEnum)) {
            Message message = messageRepository.selectByPrimaryKey(event.getMessageId());

            if (Objects.isNull(message)) {
                log.error("message id {} nonexistent", event.getMessageId());
                return;
            }

            if (message.getMessageLocalStatus().getCode() >= MessageLocalStatusEnum.REPLY.getCode()) {
                log.warn("message id {} local status is {}, need no modification");
                return;
            }

            message.setMessageLocalStatus(MessageLocalStatusEnum.REPLY);
            message.setThirdPartyUniqueId(event.getThirdPartyUniqueId());
            message.setThirdPartyRespCode(event.getThirdPartyRespCode());
            message.setThirdPartyRespMsg(event.getThirdPartyRespMsg());

            int result = messageRepository.updateByPrimaryKeySelective(message);
            if (result <= 0) {
                log.error("message {} REPLY, change status success affect rows is 0");
                throw new DBException("修改消息" + event.getMessageId() + "REPLY异常");
            }

            log.info("message {} REPLY, change status success", message);
        }
    }

    @Override
    @EventListener
    public void onSendRetryEvent(MessageRetryEvent event) {
        rabbitTemplate.send(rabbitProperties.getExchangeName(), event.getRoutingKey(),
                event.getCorrelationData().getMessage(), event.getCorrelationData());
        log.info("message {} retry", event);
    }
}
