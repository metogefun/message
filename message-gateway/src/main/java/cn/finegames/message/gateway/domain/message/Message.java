package cn.finegames.message.gateway.domain.message;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信
 *
 * @author wang zhaohui
 * @since 1.0
 */
@ToString
public class Message {

    private Long id;

    /**
     * 待发送消息账号
     */
    private String[] accounts;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类别
     */
    private MessageCategoryEnum messageCategory;

    /**
     * 消息类型
     */
    private MessageTypeEnum messageType;

    /**
     * 消息本地状态
     */
    private MessageLocalStatusEnum messageLocalStatus;

    /**
     * 延迟发送时间
     * <p>
     * 目前畅卓短信支持
     */
    private Date delayedSendTime;

    /**
     * 扩展时间
     */
    private String extendedData;

    /**
     * 消息来源app id
     */
    private String sourceAppId;

    /**
     * 发送ip地址
     */
    private String sourceIp;

    /**
     * 第三方唯一标识
     */
    private String thirdPartyUniqueId;

    /**
     * 响应code
     */
    private String thirdPartyRespCode;

    /**
     * 响应消息
     */
    private String thirdPartyRespMsg;

    /**
     * 上行内容
     */
    private String replyContent;

    /**
     * 消息接收时间
     */
    private Date receivedTime;

    /**
     * 备注
     */
    private String remark;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        Preconditions.checkArgument(Objects.nonNull(id) && id.longValue() > 0L, "id must > 0");
        this.id = id;
    }

    public String[] getAccounts() {
        return accounts;
    }

    public void setAccounts(String[] accounts) {
        Preconditions.checkArgument(Objects.nonNull(accounts) && accounts.length > 0,
                "accounts can not be empty");
        this.accounts = accounts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        Preconditions.checkArgument(StringUtils.isNotBlank(content), "content can not be empty");
        this.content = content;
    }

    public MessageCategoryEnum getMessageCategory() {
        return messageCategory;
    }

    public void setMessageCategory(MessageCategoryEnum messageCategory) {
        Preconditions.checkArgument(Objects.nonNull(messageCategory), "message category can not be null");
        this.messageCategory = messageCategory;
    }

    public MessageTypeEnum getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageTypeEnum messageType) {
        Preconditions.checkArgument(Objects.nonNull(messageType), "message type can not be null");
        this.messageType = messageType;
    }

    public MessageLocalStatusEnum getMessageLocalStatus() {
        return messageLocalStatus;
    }

    public void setMessageLocalStatus(MessageLocalStatusEnum messageLocalStatus) {
        this.messageLocalStatus = messageLocalStatus;
    }

    public Date getDelayedSendTime() {
        return delayedSendTime;
    }

    public void setDelayedSendTime(Date delayedSendTime) {
        this.delayedSendTime = delayedSendTime;
    }

    public String getExtendedData() {
        return extendedData;
    }

    public void setExtendedData(String extendedData) {
        this.extendedData = extendedData;
    }

    public String getSourceAppId() {
        return sourceAppId;
    }

    public void setSourceAppId(String sourceAppId) {
        this.sourceAppId = sourceAppId;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(sourceIp);
        Preconditions.checkArgument(matcher.matches(), "IP address format error");
        this.sourceIp = sourceIp;
    }

    public String getThirdPartyUniqueId() {
        return thirdPartyUniqueId;
    }

    public void setThirdPartyUniqueId(String thirdPartyUniqueId) {
        this.thirdPartyUniqueId = thirdPartyUniqueId;
    }

    public String getThirdPartyRespCode() {
        return thirdPartyRespCode;
    }

    public void setThirdPartyRespCode(String thirdPartyRespCode) {
        this.thirdPartyRespCode = thirdPartyRespCode;
    }

    public String getThirdPartyRespMsg() {
        return thirdPartyRespMsg;
    }

    public void setThirdPartyRespMsg(String thirdPartyRespMsg) {
        this.thirdPartyRespMsg = thirdPartyRespMsg;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public Date getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
