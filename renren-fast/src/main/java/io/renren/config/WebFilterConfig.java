package io.renren.config;

import io.renren.modules.app.interceptor.AuthorizationInterceptor;
import io.renren.modules.app.utils.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Configuration
public class WebFilterConfig {
//    @Bean
//    public WebFilter authorizationInterceptor(JwtUtils jwtUtils) {
//        return new AuthorizationInterceptor(jwtUtils);
//    }
}