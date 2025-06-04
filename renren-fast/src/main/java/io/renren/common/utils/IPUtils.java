package io.renren.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

public class IPUtils {
    private static final Logger logger = LoggerFactory.getLogger(IPUtils.class);

    /**
     * 获取IP地址（WebFlux版）
     */
    public static String getIpAddr(ServerWebExchange exchange) {
        String ip = null;
        try {
            ServerHttpRequest request = exchange.getRequest();
            ip = request.getHeaders().getFirst("x-forwarded-for");
            if (isEmptyOrUnknown(ip)) {
                ip = request.getHeaders().getFirst("Proxy-Client-IP");
            }
            if (isEmptyOrUnknown(ip)) {
                ip = request.getHeaders().getFirst("WL-Proxy-Client-IP");
            }
            if (isEmptyOrUnknown(ip)) {
                ip = request.getHeaders().getFirst("HTTP_CLIENT_IP");
            }
            if (isEmptyOrUnknown(ip)) {
                ip = request.getHeaders().getFirst("HTTP_X_FORWARDED_FOR");
            }
            if (isEmptyOrUnknown(ip) && request.getRemoteAddress() != null) {
                ip = request.getRemoteAddress().getAddress().getHostAddress();
            }
            // 多级代理时，取第一个非unknown的IP
            if (ip != null && ip.length() > 15 && ip.contains(",")) {
                ip = ip.split(",")[0].trim();
            }
        } catch (Exception e) {
            logger.error("IPUtils ERROR ", e);
        }
        return ip;
    }

    private static boolean isEmptyOrUnknown(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }
}