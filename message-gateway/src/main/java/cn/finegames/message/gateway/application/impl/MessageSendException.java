package cn.finegames.message.gateway.application.impl;


import cn.finegames.commons.util.exception.BaseException;

/**
 * 消息发送异常
 *
 * @author wang zhaohui
 * @since 1.0
 */
public class MessageSendException extends BaseException {

    public MessageSendException(String message) {
        super(500, message);
    }
}
