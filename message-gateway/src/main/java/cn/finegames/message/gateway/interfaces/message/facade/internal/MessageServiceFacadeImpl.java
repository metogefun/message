package cn.finegames.message.gateway.interfaces.message.facade.internal;

import cn.finegames.commons.util.Filter;
import cn.finegames.commons.util.FilterChain;
import cn.finegames.message.gateway.domain.message.Message;
import cn.finegames.message.gateway.domain.message.MessageCategoryEnum;
import cn.finegames.message.gateway.domain.message.MessageLocalStatusEnum;
import cn.finegames.message.gateway.domain.message.MessageTypeEnum;
import cn.finegames.message.gateway.interfaces.message.DingCommand;
import cn.finegames.message.gateway.interfaces.message.SmsCommand;
import cn.finegames.message.gateway.interfaces.message.facade.MessageServiceFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 短信服务门面实现
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Slf4j
@Service
public class MessageServiceFacadeImpl implements MessageServiceFacade {

    @Autowired
    @Qualifier("idempotentFilter")
    private Filter<Message> idempotentFilter;

    @Autowired
    @Qualifier("ipBlacklistFilter")
    private Filter<Message> ipBlacklistFilter;

    @Autowired
    @Qualifier("accountBlacklistFilter")
    private Filter<Message> accountBlacklistFilter;

    @Autowired
    @Qualifier("serverWhiteListFilter")
    private Filter<Message> serverWhiteListFilter;


    @Autowired
    @Qualifier("messageServiceFilterProxy")
    private Filter<Message> messageServiceFilterProxy;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void save(SmsCommand smsCommand) {
        Message message = new Message();
        message.setContent(smsCommand.getContent());
        message.setAccounts(Arrays.stream(smsCommand.getMobiles().split(",")).collect(Collectors.toSet()).stream().toArray(String[]::new));
        Optional<MessageTypeEnum> smsTypeOptional = MessageTypeEnum.of(smsCommand.getSmsType());
        if (smsTypeOptional.isPresent()) {
            message.setMessageType(smsTypeOptional.get());
        } else {
            message.setMessageType(MessageTypeEnum.NOTIFICATION);
        }
        message.setMessageLocalStatus(MessageLocalStatusEnum.RECEIVED);
        message.setSourceAppId(smsCommand.getAppId());
        message.setSourceIp(smsCommand.getIp());
        message.setMessageCategory(MessageCategoryEnum.SMS);
        getFilterChain().doFilter(message);
        log.info("发送短信{}", smsCommand);
    }

    @Override
    public void save(DingCommand dingCommand) {
        Message message = new Message();
        message.setContent(dingCommand.getContent());
        message.setAccounts(Arrays.stream(dingCommand.getUsers().split(",")).collect(Collectors.toSet()).stream().toArray(String[]::new));
        message.setMessageType(MessageTypeEnum.NOTIFICATION);
        message.setMessageLocalStatus(MessageLocalStatusEnum.RECEIVED);
        message.setSourceIp(dingCommand.getIp());
        message.setSourceAppId(dingCommand.getAppId());
        message.setMessageCategory(MessageCategoryEnum.DING);
        getFilterChain().doFilter(message);
        log.info("发送钉钉{}", dingCommand);
    }


    private FilterChain<Message> getFilterChain() {
        FilterChain<Message> filterChain = applicationContext.getBean("filterChain", FilterChain.class);
        filterChain.filters(idempotentFilter, ipBlacklistFilter, accountBlacklistFilter, serverWhiteListFilter,
                messageServiceFilterProxy);
        return filterChain;
    }

}
