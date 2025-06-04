package io.renren.modules.app.interceptor;

import io.jsonwebtoken.Claims;
import io.renren.common.exception.RRException;
import io.renren.modules.app.utils.JwtUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * 权限(Token)验证 WebFlux 过滤器
 */

public class AuthorizationInterceptor implements WebFilter {
    @Autowired
    private JwtUtils jwtUtils;

    public AuthorizationInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public static final String USER_KEY = "userId";

    private static final List<String> WHITE_LIST = Arrays.asList(
            "/sys/login",
            "/sys/logout",
            "/sys/menu/nav",
            "/captcha.jpg"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        
        // 检查是否是白名单路径
        if (WHITE_LIST.stream().anyMatch(path::endsWith)) {
            return chain.filter(exchange);
        }

        // 获取token
        String token = exchange.getRequest().getHeaders().getFirst("token");
        if (StringUtils.isBlank(token)) {
            return Mono.error(new RRException("token不能为空", HttpStatus.UNAUTHORIZED.value()));
        }

        // 验证token
        Claims claims = jwtUtils.getClaimByToken(token);
        if (claims == null || jwtUtils.isTokenExpired(claims.getExpiration())) {
            return Mono.error(new RRException("token已失效，请重新登录", HttpStatus.UNAUTHORIZED.value()));
        }

        // 设置userId到request
        exchange.getAttributes().put(USER_KEY, claims.getSubject());
        
        return chain.filter(exchange);
    }
}