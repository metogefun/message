package cn.finegames.message.gateway.application.impl.filter;


import cn.finegames.commons.util.exception.BaseException;

/**
 * 幂等性重复key
 *
 * @author wang zhaohui
 * @since 1.0
 */
public class RepeatedKeyException extends BaseException {

    public RepeatedKeyException(String message) {
        super(409, message);
    }
}
