package cn.finegames.message.gateway.application;


import cn.finegames.message.gateway.application.impl.MessageSendEvent;
import cn.finegames.message.gateway.infrastructure.messaging.rabbit.event.MessageConfirmedEvent;
import cn.finegames.message.gateway.infrastructure.messaging.rabbit.event.MessageLocalStatusChangedEvent;
import cn.finegames.message.gateway.infrastructure.messaging.rabbit.event.MessageRetryEvent;

/**
 * message event service
 *
 * @author wang zhaohui
 * @since 1.0
 */
public interface MessageEventService {


    /**
     * 消息发送事件
     *
     * @param messageSendEvent
     */
    void onMessageSendEvent(MessageSendEvent messageSendEvent);


    /**
     * listener rabbit mq confirm event
     *
     * @param event
     */
    void onMessageConfirmEvent(MessageConfirmedEvent event);

    /**
     * listener {@linkplain MessageLocalStatusChangedEvent}
     *
     * @param event
     */
    void onLocalStatusChangeEvent(MessageLocalStatusChangedEvent event);


    /**
     * listener {@linkplain MessageRetryEvent}
     *
     * @param event
     */
    void onSendRetryEvent(MessageRetryEvent event);
}
