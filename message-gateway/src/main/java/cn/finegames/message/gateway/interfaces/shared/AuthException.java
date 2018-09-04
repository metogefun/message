package cn.finegames.message.gateway.interfaces.shared;


import cn.finegames.commons.util.exception.BaseException;

/**
 * 认证异常
 *
 * @author wang zhaohui
 * @since 1.0
 */
public class AuthException extends BaseException {

    public AuthException(String message) {
        super(500, message);
    }
}