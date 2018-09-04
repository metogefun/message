package cn.finegames.message.gateway.infrastructure.messaging.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ThreadFactory;

/**
 * rabbit mq 配置
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Configuration
public class RabbitConfig {

    @Autowired
    private RabbitProperties rabbitProperties;

    @Autowired
    private RabbitReturnCallback returnCallback;

    @Autowired
    private RabbitConfirmCallback rabbitConfirmCallback;

    @Autowired
    private RabbitMessageListener rabbitMessageListener;


    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(500);
        backOffPolicy.setMultiplier(10.0);
        backOffPolicy.setMaxInterval(10000);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        template.setRetryTemplate(retryTemplate);
        template.setMandatory(true);
        template.setReturnCallback(returnCallback);
        template.setConfirmCallback(rabbitConfirmCallback);
        return template;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(rabbitProperties.getAddress());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        connectionFactory.setVirtualHost(rabbitProperties.getVhost());
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        connectionFactory.setChannelCacheSize(rabbitProperties.getChannelSize());
        connectionFactory.setChannelCheckoutTimeout(rabbitProperties.getChannelCheckoutTimeout());
        connectionFactory.setConnectionTimeout(rabbitProperties.getConnectionTimeout());

        connectionFactory.setConnectionNameStrategy(cf -> "PIS_SMS");
        connectionFactory.setConnectionThreadFactory(threadFactory());
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);

        return connectionFactory;
    }


    @Bean
    public SimpleMessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueues(replyQueue(), callbackQueue());
        container.setMessageListener(rabbitMessageListener);
        return container;
    }

    @Bean
    public ThreadFactory threadFactory() {
        return new CustomizableThreadFactory("rabbitmq-");
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }


    @Bean
    public Exchange exchange() {
        TopicExchange topicExchange = new TopicExchange(rabbitProperties.getExchangeName(), true, false);
        rabbitAdmin().declareExchange(topicExchange);
        return topicExchange;
    }


    @Bean
    public Queue replyQueue() {
        Queue queue = new Queue(rabbitProperties.getReplyQueueName(), true);
        rabbitAdmin().declareQueue(queue);
        return queue;
    }

    @Bean
    public Binding replyBinding() {
        Binding binding = new Binding(replyQueue().getName(), Binding.DestinationType.QUEUE, exchange().getName(),
                rabbitProperties.getReplyRoutingKey(), null);
        rabbitAdmin().declareBinding(binding);
        return binding;
    }


    @Bean
    public Queue callbackQueue() {
        Queue queue = new Queue(rabbitProperties.getCallbackQueueName(), true);
        rabbitAdmin().declareQueue(queue);
        return queue;
    }

    @Bean
    public Binding callbackBinding() {
        Binding binding = new Binding(callbackQueue().getName(), Binding.DestinationType.QUEUE, exchange().getName(),
                rabbitProperties.getCallbackRoutingKey(), null);
        rabbitAdmin().declareBinding(binding);
        return binding;
    }
}
