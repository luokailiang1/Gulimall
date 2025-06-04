package io.renren.common.aspect;

import com.google.gson.Gson;
import io.renren.common.annotation.SysLog;
import io.renren.modules.sys.entity.SysLogEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class SysLogAspect {
	@Autowired
	private SysLogService sysLogService;

	@Pointcut("@annotation(io.renren.common.annotation.SysLog)")
	public void logPointCut() { }

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		Object result = point.proceed();
		long time = System.currentTimeMillis() - beginTime;
		saveSysLog(point, time);
		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		SysLogEntity sysLog = new SysLogEntity();
		SysLog syslog = method.getAnnotation(SysLog.class);
		if (syslog != null) {
			sysLog.setOperation(syslog.value());
		}

		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");

		Object[] args = joinPoint.getArgs();
		try {
			String params = new Gson().toJson(args);
			sysLog.setParams(params);
		} catch (Exception e) {
			// ignore
		}

		// 查找 ServerWebExchange 参数获取 IP
		String ip = null;
		for (Object arg : args) {
			if (arg instanceof ServerWebExchange exchange) {
                if (exchange.getRequest().getRemoteAddress() != null) {
					ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
				}
				break;
			}
		}
		sysLog.setIp(ip);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = null;
		if (authentication != null && authentication.getPrincipal() instanceof SysUserEntity) {
			username = ((SysUserEntity) authentication.getPrincipal()).getUsername();
		}
		sysLog.setUsername(username);

		sysLog.setTime(time);
		sysLog.setCreateDate(new Date());
		sysLogService.save(sysLog);
	}
}