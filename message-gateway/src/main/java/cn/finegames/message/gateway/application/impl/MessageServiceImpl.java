package cn.finegames.message.gateway.application.impl;

import cn.finegames.commons.util.exception.BaseException;
import cn.finegames.commons.util.exception.DBException;
import cn.finegames.message.gateway.application.MessageService;
import cn.finegames.message.gateway.domain.message.Message;
import cn.finegames.message.gateway.domain.message.MessageCategoryEnum;
import cn.finegames.message.gateway.domain.message.MessageLocalStatusEnum;
import cn.finegames.message.gateway.domain.message.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cn.finegames.commons.protocol.Message.*;

import java.util.Arrays;
import java.util.Objects;


/**
 * 短信服务
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    @Transactional(rollbackFor = BaseException.class)
    public void save(Message message) {
        int result = messageRepository.insertSelective(message);
        if (result <= 0) {
            throw new DBException("消息保存异常");
        }

        // 如果消息状态非RECEIVED状态不会推送到MQ
        if (!MessageLocalStatusEnum.RECEIVED.equals(message.getMessageLocalStatus())) {
            return;
        }

        // 发送 rabbitmq
        MessageStruct.Builder builder = MessageStruct.newBuilder()
                .setMessageId(message.getId())
                .addAllAccounts(Arrays.asList(message.getAccounts()))
                .setContent(message.getContent())
                .setMessageType(message.getMessageType().getCode());
        if (Objects.nonNull(message.getDelayedSendTime())) {
            builder.setSendTime(message.getDelayedSendTime().getTime());
        }

        byte[] body = builder.build().toByteArray();


        if (Objects.isNull(body)) {
            throw new MessageSendException("消息类型" + message.getMessageCategory() + "组装异常");
        }

        MessageSendEvent messageSendEvent = new MessageSendEvent(this);
        messageSendEvent.setBody(body);
        messageSendEvent.setMessageCategoryEnum(message.getMessageCategory());
        messageSendEvent.setMessageId(message.getId());
        publisher.publishEvent(messageSendEvent);
    }
}
