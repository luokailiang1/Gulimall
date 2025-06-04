package io.renren.common.xss;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class XssFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest().mutate()
				// 可在此处对 header、query param 做 XSS 过滤
				// .header("xxx", xssClean(value))
				.build();
		ServerWebExchange xssExchange = exchange.mutate().request(request).build();
		return chain.filter(xssExchange);
	}

	// 示例：简单的 XSS 清理方法
	private String xssClean(String value) {
		if (value == null) return null;
		return value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
}