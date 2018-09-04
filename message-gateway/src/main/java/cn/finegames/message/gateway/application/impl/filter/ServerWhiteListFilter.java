package cn.finegames.message.gateway.application.impl.filter;

import cn.finegames.commons.util.Filter;
import cn.finegames.commons.util.FilterChain;
import cn.finegames.message.gateway.domain.message.Message;
import cn.finegames.message.gateway.domain.message.MessageCategoryEnum;
import cn.finegames.message.gateway.domain.permit.TrafficPermit;
import cn.finegames.message.gateway.domain.permit.TrafficPermitRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static cn.finegames.message.gateway.domain.message.MessageLocalStatusEnum.UNAUTHORIZED;


/**
 * 服务器白名单
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Slf4j
@Component
@Qualifier("serverWhiteListFilter")
public class ServerWhiteListFilter implements Filter<Message> {

    @Autowired
    private TrafficPermitRepository trafficPermitRepository;

    @Override
    public void doFilter(Message message, FilterChain<Message> filterChain) {
        MessageCategoryEnum messageCategory = message.getMessageCategory();

        List<TrafficPermit> list = trafficPermitRepository.selectAllowServersByMessageCategory(messageCategory);

        if (Objects.nonNull(list) && !list.isEmpty()) {
            String host = MDC.get("host");

            if (list.stream().anyMatch(ip -> ip.equals(host))) {
                message.setMessageLocalStatus(UNAUTHORIZED);
                message.setRemark(host + "未在许可ip中");
            }
        }

        filterChain.doFilter(message);
    }
}
