package cn.finegames.message.gateway.interfaces.shared;

import cn.finegames.commons.util.NetUtils;
import cn.finegames.message.gateway.domain.account.OpenAccount;
import cn.finegames.message.gateway.domain.account.OpenAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Objects;

/**
 * 认证拦截器
 *
 * @author wang zhaohui
 * @since 1.0
 */
@Slf4j
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    /**
     * 消息1分钟过期
     */
    private static final long EXPIRE_TIME = 60000;

    @Autowired
    private OpenAccountRepository openAccountRepository;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 时间戳验证
        String timestamp = request.getParameter("timestamp");
        if (StringUtils.isBlank(timestamp)) {
            throw new AuthException("消息已过期");
        }
        Date timestampDate = new Date(Long.valueOf(timestamp));
        if (System.currentTimeMillis() - timestampDate.getTime() > EXPIRE_TIME) {
            throw new AuthException("消息已超时");
        }

        // 签名认证
        String sign = request.getParameter("sign");
        if (StringUtils.isBlank(sign)) {
            throw new AuthException("未包含签名");
        }

        // ip 认证
        String ip = request.getParameter("ip");
        if (StringUtils.isBlank(ip) || NetUtils.isInvalidLocalHost(ip)) {
            throw new AuthException("请使用内网或外网地址");
        }

        // app 认证
        String appId = request.getParameter("appId");
        if (StringUtils.isBlank(appId)) {
            throw new AuthException("未知app id");
        }
        OpenAccount openAccount = openAccountRepository.selectByAppId(appId);
        if (Objects.isNull(openAccount)) {
            throw new AuthException("app id不存在");
        }

        String secretKey = openAccount.getSecretKey();

        String host = request.getRemoteHost();
        MDC.put("host", host);

        StringBuilder sb;
        String content = request.getParameter("content");
        switch (request.getServletPath()) {
            case "/message/sms":
                sb = new StringBuilder();
                String mobiles = request.getParameter("mobiles");
                String smsType = request.getParameter("smsType");
                String sendTime = request.getParameter("sendTime");
                sb.append(request.getServletPath()).append("?")
                        .append("appId=").append(appId)
                        .append("&mobiles=").append(mobiles)
                        .append("&content=").append(content)
                        .append("&smsType=").append(smsType);
                if (StringUtils.isNotBlank(sendTime)) {
                    sb.append("&sendTime=").append(sendTime);
                }
                sb.append("&timestamp=").append(timestamp)
                        .append("&ip=").append(ip)
                        .append("&secretKey=").append(secretKey);
                String secretSign = toMD5(sb.toString());

                log.debug("{} 参数列表 {} MD5加密后结果{}，url中sign {}", request.getServletPath(), sb.toString(), secretSign, sign);
                if (!secretSign.equals(sign)) {
                    throw new AuthException("签名异常");
                }
                break;
            case "/message/ding":
                sb = new StringBuilder();
                String users = request.getParameter("users");
                sb.append(request.getServletPath()).append("?")
                        .append("appId=").append(appId)
                        .append("&users=").append(users)
                        .append("&content=").append(content)
                        .append("&timestamp=").append(timestamp)
                        .append("&ip=").append(ip)
                        .append("&secretKey=").append(secretKey);

                secretSign = toMD5(sb.toString());
                if (!sign.equals(secretSign)) {
                    throw new AuthException("签名异常");
                }
                log.debug("{} 参数列表 {} MD5加密后结果{}，url中sign {}", request.getServletPath(), sb.toString(), secretSign, sign);
                break;
            default:
                break;
        }

        return super.preHandle(request, response, handler);
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        MDC.clear();
        super.postHandle(request, response, handler, modelAndView);
    }

    private String toMD5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes(Charset.forName("UTF-8")));
            return new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new AuthException("MD5算法支持异常");
        }
    }
}