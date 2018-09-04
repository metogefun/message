package cn.finegames.message.gateway.application.impl.filter;


import cn.finegames.commons.util.exception.BaseException;

/**
 * 黑名单异常
 *
 * @author wang zhaohui
 * @since 1.0
 */
public class BlacklistException extends BaseException {

    public BlacklistException(String message) {
        super(500, message);
    }
}
