package cn.finegames.message.gateway.domain.message;

import cn.finegames.commons.util.EnumCode;

/**
 * 消息类型
 *
 * @author wang zhaohui
 * @since 1.0
 */
public enum MessageCategoryEnum implements EnumCode {

    /**
     * 短信
     */
    SMS(1),

    /**
     * 钉钉
     */
    DING(2);

    private int code;

    MessageCategoryEnum(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }


    public static MessageCategoryEnum of(int code) {
        MessageCategoryEnum[] messageCategoryEnums = MessageCategoryEnum.values();
        for (MessageCategoryEnum messageCategoryEnum : messageCategoryEnums) {
            if (messageCategoryEnum.code == code) {
                return messageCategoryEnum;
            }
        }

        throw new IllegalArgumentException("错误消息类型 [" + code + " ]");
    }

}
