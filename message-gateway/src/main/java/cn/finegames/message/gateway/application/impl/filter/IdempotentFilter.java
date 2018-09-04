package cn.finegames.message.gateway.application.impl.filter;

import cn.finegames.commons.util.Filter;
import cn.finegames.commons.util.FilterChain;
import cn.finegames.message.gateway.domain.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 幂等性过滤器
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Slf4j
@Component
@Qualifier("idempotentFilter")
public class IdempotentFilter implements Filter<Message> {

    private static final String VALUE = "1";

    private static final long TIME = 5L;

    @Autowired
    private RedisTemplate<String, String> template;

    @Override
    public void doFilter(Message message, FilterChain<Message> filterChain) {
        String flag = getIdempotentFlag(message);
        Boolean absent = template.opsForValue().setIfAbsent(flag, "");
        if (Objects.isNull(absent) || !absent) {
            log.warn("待发送消息 {} 重复", message);
            throw new RepeatedKeyException("消息重复发送");
        }

        try {
            template.opsForValue().set(flag, VALUE, TIME, TimeUnit.MINUTES);
            log.debug("消息{}保存幂等性验证通过", message);
            filterChain.doFilter(message);
        } catch (RuntimeException e) {
            log.warn("信息{}保存异常清除幂等性信息", message);
            template.delete(flag);
            throw new RuntimeException(e);
        }

    }


    /**
     * 加密key
     *
     * @param message
     * @return
     */
    private String getIdempotentFlag(Message message) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(",", message.getAccounts())).append(message.getContent()).append(message.getMessageCategory());

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sb.toString().getBytes());

            StringBuilder hexString = new StringBuilder();

            byte[] hash = md.digest();
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("内部算法转换异常");
        }
    }
}
