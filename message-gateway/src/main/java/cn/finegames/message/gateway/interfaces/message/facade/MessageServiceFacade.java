package cn.finegames.message.gateway.interfaces.message.facade;

import cn.finegames.message.gateway.interfaces.message.DingCommand;
import cn.finegames.message.gateway.interfaces.message.SmsCommand;

/**
 * 消息服务
 *
 * @author wang zhaohui
 * @since 1.0
 */
public interface MessageServiceFacade {

    /**
     * 保存短信
     *
     * @param smsCommand
     */
    void save(SmsCommand smsCommand);

    /**
     * 保存钉钉
     *
     * @param dingCommand
     */
    void save(DingCommand dingCommand);
}
