package cn.finegames.message.gateway.domain.permit;

import cn.finegames.message.gateway.domain.message.MessageCategoryEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 开放账号
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class TrafficPermit {

    /**
     * id
     */
    private Integer id;

    /**
     * 许可类别
     */
    private PermitCategoryEnum permitCategory;

    /**
     * 许可内容
     */
    private String permitContent;

    /**
     * 许可描述
     */
    private String remark;

    /**
     * 消息类别
     */
    private MessageCategoryEnum messageCategory;

    /**
     * 是否禁止
     */
    private Boolean forbidden;

    /**
     * 是否删除
     */
    private Boolean delete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;
}