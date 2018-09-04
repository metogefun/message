package cn.finegames.message.gateway.domain.permit;


import cn.finegames.commons.util.EnumCode;

/**
 * 许可类型
 *
 * @author wang zhaohui
 * @since 1.0
 */
public enum PermitCategoryEnum implements EnumCode {

    /**
     * ip
     */
    IP(0),

    /**
     * account
     */
    ACCOUNT(1);

    private int code;

    PermitCategoryEnum(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
