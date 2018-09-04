package cn.finegames.message.gateway.application.impl.filter;

import cn.finegames.commons.util.Filter;
import cn.finegames.commons.util.FilterChain;
import cn.finegames.message.gateway.application.MessageService;
import cn.finegames.message.gateway.domain.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * message
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Component
@Qualifier("messageServiceFilterProxy")
public class MessageServiceFilterProxy implements Filter<Message> {

    @Autowired
    private MessageService messageService;

    @Override
    public void doFilter(Message message, FilterChain<Message> filterChain) {
        messageService.save(message);
    }
}
