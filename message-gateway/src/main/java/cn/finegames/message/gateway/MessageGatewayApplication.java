package cn.finegames.message.gateway;

import cn.finegames.commons.util.FilterChain;
import cn.finegames.message.gateway.domain.message.Message;
import cn.finegames.message.gateway.infrastructure.messaging.rabbit.RabbitConfig;
import cn.finegames.message.gateway.interfaces.shared.AuthInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * message gateway application launcher
 *
 * @author wang zhaohui
 * @since 1.0
 */
@EnableTransactionManagement
@SpringBootApplication
@Import(RabbitConfig.class)
@MapperScan("cn.finegames.message.gateway.domain")
public class MessageGatewayApplication {


    @Autowired
    private AuthInterceptor authInterceptor;

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(authInterceptor).addPathPatterns("/**");
            }
        };
    }

    @Bean(initMethod = "init")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FilterChain<Message> filterChain() {
        return new FilterChain<>();
    }
}
