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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 黑名单过滤
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Slf4j
@Component
@Qualifier("accountBlacklistFilter")
public class AccountBlacklistFilter implements Filter<Message> {

    @Autowired
    private TrafficPermitRepository trafficPermitRepository;


    @Override
    public void doFilter(Message message, FilterChain<Message> filterChain) {
        // 账号黑名单
        String[] accounts = message.getAccounts();
        MessageCategoryEnum messageCategory = message.getMessageCategory();
        List<TrafficPermit> trafficPermitList = trafficPermitRepository.selectForbiddenByMessageCategoryAndAccount(messageCategory, accounts);
        if (!trafficPermitList.isEmpty()) {
            // 移除黑名单中的账号
            List<String> sendAccounts = Arrays.stream(accounts).collect(Collectors.toList());
            List<String> forbiddenAccounts = trafficPermitList.stream().map(TrafficPermit::getPermitContent).collect(Collectors.toList());
            sendAccounts.removeAll(forbiddenAccounts);
            // 如果所有账号都在黑名单中
            if (sendAccounts.isEmpty()) {
                message.setMessageLocalStatus(MessageLocalStatusEnum.BLACKLIST);
                message.setRemark("All accounts are on the blacklist");
                log.warn("消息{}所有账号都在黑名单中", message);
            } else {
                accounts = new String[sendAccounts.size()];
                sendAccounts.toArray(accounts);
                message.setAccounts(accounts);
                message.setRemark(String.join(",", forbiddenAccounts.stream().toArray(String[]::new)) + " on the blacklist");
            }
        }

        log.info("消息{}账号黑名单验证通过", message);
        filterChain.doFilter(message);
    }
}
