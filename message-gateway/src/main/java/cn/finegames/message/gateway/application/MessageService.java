package cn.finegames.message.gateway.application;


import cn.finegames.message.gateway.domain.message.Message;

/**
 * 短信服务
 *
 * @author wang zhaohui
 * @since 1.0
 */
public interface MessageService {


    /**
     * 保存短信
     *
     * @param message
     */
    void save(Message message);
}
