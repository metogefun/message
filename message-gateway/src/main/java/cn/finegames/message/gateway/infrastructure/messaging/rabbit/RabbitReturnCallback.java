package cn.finegames.message.gateway.infrastructure.messaging.rabbit;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static cn.finegames.commons.protocol.Message.MessageStruct;


/**
 * rabbit mq return callback
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Slf4j
@Component
public class RabbitReturnCallback implements RabbitTemplate.ReturnCallback {

    private static Logger rabbitAckLog = LoggerFactory.getLogger("rabbit_ack");

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        try {
            MessageStruct messageStruct = MessageStruct.parseFrom(message.getBody());
            rabbitAckLog.warn(messageStruct.toString());
            log.error("message {} send error, reply code {}, reply text {}, exchange {}, routing key {}",
                    message, replyCode, replyText, exchange, routingKey);
        } catch (InvalidProtocolBufferException e) {
            log.error("message {} parse error, reply code {}, reply text {}, exchange {}, routing key {}",
                    message, replyCode, replyText, exchange, routingKey);
        }
    }
}
