package cn.finegames.message.gateway.application.impl.filter;

import cn.finegames.commons.util.Filter;
import cn.finegames.commons.util.FilterChain;
import cn.finegames.message.gateway.domain.message.Message;
import cn.finegames.message.gateway.domain.message.MessageCategoryEnum;
import cn.finegames.message.gateway.domain.message.MessageLocalStatusEnum;
import cn.finegames.message.gateway.domain.permit.TrafficPermit;
import cn.finegames.message.gateway.domain.permit.TrafficPermitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * ip blacklist filter
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Slf4j
@Component
@Qualifier("ipBlacklistFilter")
public class IpBlacklistFilter implements Filter<Message> {

    @Autowired
    private TrafficPermitRepository trafficPermitRepository;

    @Override
    public void doFilter(Message message, FilterChain<Message> filterChain) {
        // ip 黑名单
        String ip = message.getSourceIp();
        MessageCategoryEnum messageCategory = message.getMessageCategory();
        TrafficPermit trafficPermit = trafficPermitRepository.selectForbiddenByMessageCategoryAndIp(messageCategory, ip);
        if (Objects.nonNull(trafficPermit)) {
            message.setMessageLocalStatus(MessageLocalStatusEnum.BLACKLIST);
            message.setRemark("IP is on the blacklist");
            log.warn("消息{}的在IP黑名单中");
        }
        log.info("消息{}IP黑名单验证通过", message);
        filterChain.doFilter(message);
    }

}
