package io.renren.common.utils;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

public class HttpContextUtils {

	// 推荐：通过参数传递 exchange
	public static String getDomain(ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		String url = request.getURI().toString();
		String path = request.getPath().toString();
		return url.substring(0, url.length() - path.length());
	}

	public static String getOrigin(ServerWebExchange exchange) {
		return exchange.getRequest().getHeaders().getFirst("Origin");
	}
}