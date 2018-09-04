package cn.finegames.message.gateway.domain.message;


import cn.finegames.commons.util.EnumCode;

import java.util.Optional;

/**
 * 短信类型
 *
 * @author wang zhaohui
 * @since 1.0
 */
public enum MessageTypeEnum implements EnumCode {

    /**
     * 验证码
     */
    AUTH_CODE(1),

    /**
     * 通知
     */
    NOTIFICATION(2),

    /**
     * 运营
     */
    OPERATION(3);

    private Integer code;

    MessageTypeEnum(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    /**
     * 短信类型
     *
     * @param code
     * @return 短信类型
     */
    public static Optional<MessageTypeEnum> of(int code) {
        MessageTypeEnum[] types = MessageTypeEnum.values();
        for (MessageTypeEnum type : types) {
            if (type.code == code) {
                return Optional.of(type);
            }
        }

        return Optional.empty();
    }
}
