package cn.finegames.message.gateway.infrastructure.messaging.rabbit;

import cn.finegames.message.gateway.domain.message.MessageCategoryEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * message correlation data
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Getter
@ToString
public class MessageCorrelationData extends CorrelationData {

    private Message message;

    /**
     * retry count
     */
    private int retryCount;

    @Setter
    private long messageId;

    @Setter
    private MessageCategoryEnum messageCategory;

    public MessageCorrelationData(Message message) {
        this.message = message;
    }


    /**
     * 增加次数
     *
     * @return
     */
    public int incrementRetryCount() {
        return ++retryCount;
    }
}
