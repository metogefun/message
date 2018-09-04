package cn.finegames.message.gateway.infrastructure.messaging.rabbit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * rabbitmq配置
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "rabbit")
public class RabbitProperties {

    /**
     * mq 地址
     */
    private String address;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * virtual-host
     */
    private String vhost;

    /**
     * channel大小
     */
    private int channelSize;

    /**
     * 通道取出超时事件
     */
    private int channelCheckoutTimeout;

    /**
     * 连接超时
     */
    private int connectionTimeout;

    /**
     * 交换名称
     */
    private String exchangeName;

    /**
     * 发送 短信 routing key
     */
    private String sendSmsRoutingKey;

    /**
     * 发送钉钉routing key
     */
    private String sendDingRoutingKey;

    /**
     * 应答队列名称
     */
    private String replyQueueName;


    /**
     * reply routing key
     */
    private String replyRoutingKey;

    /**
     * 回调队列
     */
    private String callbackQueueName;


    /**
     * callback routing key
     */
    private String callbackRoutingKey;
}
