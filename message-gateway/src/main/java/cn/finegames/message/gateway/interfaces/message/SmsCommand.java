package cn.finegames.message.gateway.interfaces.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 短信命令
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class SmsCommand {

    @NotBlank(message = "app id不能为空")
    private String appId;

    @NotEmpty(message = "手机号不能为空")
    private String mobiles;

    @NotBlank(message = "短信内容不能为空")
    private String content;

    @Min(value = 1, message = "短信类型在1-3之间")
    @Max(value = 3, message = "短信类型在1-3之间")
    private Integer smsType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    @Min(value = 1534928400000L, message = "时间戳未大于2018/08/22 17:00:00")
    private long timestamp;

    @NotBlank(message = "签名不能为空")
    private String sign;

    @NotBlank(message = "IP不能为空")
    private String ip;
}
