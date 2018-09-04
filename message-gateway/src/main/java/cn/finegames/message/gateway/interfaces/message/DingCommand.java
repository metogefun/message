package cn.finegames.message.gateway.interfaces.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 钉钉common
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class DingCommand {
    /**
     * 应用ID
     */
    @NotBlank(message = "app id不能为空")
    private String appId;

    @NotBlank(message = "user list不能为空")
    private String users;

    @NotBlank(message = "消息类型不能为空")
    private String messageType = "text";

    @NotBlank(message = "消息内容不能为空")
    private String content;

    @Min(value = 1534928400000L, message = "时间戳未大于2018/08/22 17:00:00")
    private long timestamp;

    @NotBlank(message = "签名不能为空")
    private String sign;

    @NotBlank(message = "IP不能为空")
    private String ip;
}
