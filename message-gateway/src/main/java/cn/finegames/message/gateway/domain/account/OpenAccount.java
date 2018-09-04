package cn.finegames.message.gateway.domain.account;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.Objects;

/**
 * 开放账号
 *
 * @author wang zhaohui
 * @since 1.0
 */
@ToString
public class OpenAccount {

    /**
     * unique id
     */
    private Integer id;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 秘钥
     */
    private String secretKey;

    /**
     * 有效期
     */
    @Getter
    @Setter
    private Date expireTime;

    /**
     * 创建时间
     */
    @Getter
    @Setter
    private Date createTime;

    /**
     * 修改时间
     */
    @Getter
    @Setter
    private Date modifyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Preconditions.checkArgument(Objects.nonNull(id) && id.intValue() > 0, "id must > 0");
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(appId), "app id can not be blank");
        this.appId = appId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        Preconditions.checkArgument(StringUtils.isNotBlank(secretKey), "secretKey can not be blank");
        this.secretKey = secretKey;
    }
}
