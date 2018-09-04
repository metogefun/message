package cn.finegames.message.gateway.domain.message;


import cn.finegames.commons.util.EnumCode;

/**
 * 短信状态枚举
 *
 * @author wang zhaohui
 * @since 1.0
 */
public enum MessageLocalStatusEnum implements EnumCode {

    /**
     * 已接收，默认状态
     */
    RECEIVED(0),

    /**
     * 已发送
     */
    SENT(1),

    /**
     * 响应
     */
    REPLY(2),

    /**
     * 回调
     */
    CALLBACK(3),

    /**
     * 黑名单
     */
    BLACKLIST(98),

    /**
     * 未许可
     */
    UNAUTHORIZED(99);

    private int code;

    MessageLocalStatusEnum(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
