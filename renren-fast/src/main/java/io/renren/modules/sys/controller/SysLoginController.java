/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.sys.controller;

import io.renren.common.utils.R;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.form.SysLoginForm;
import io.renren.modules.sys.service.SysCaptchaService;
import io.renren.modules.sys.service.SysUserService;
import io.renren.modules.sys.service.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * 登录相关
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
public class SysLoginController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private SysCaptchaService sysCaptchaService;

	/**
	 * 验证码
	 */
	@GetMapping("/captcha.jpg")
	public Mono<Void> captcha(ServerHttpResponse response, String uuid) {
		try {
			//获取图片验证码
			BufferedImage image = sysCaptchaService.getCaptcha(uuid);

			// 将图片转换为字节数组
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			byte[] imageBytes = baos.toByteArray();

			// 设置响应头
			response.getHeaders().setContentType(MediaType.IMAGE_JPEG);
			response.getHeaders().setCacheControl("no-store, no-cache");

			// 创建DataBuffer并写入响应
			DataBuffer buffer = response.bufferFactory().wrap(imageBytes);
			return response.writeWith(Mono.just(buffer));
		} catch (IOException e) {
			return Mono.error(e);
		}
	}

	/**
	 * 登录
	 */
	@PostMapping("/sys/login")
	public Mono<Map<String, Object>> login(@RequestBody SysLoginForm form) {
		return Mono.fromCallable(() -> {
			boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
			if(!captcha){
				return R.error("验证码不正确");
			}

			//用户信息
			SysUserEntity user = sysUserService.queryByUserName(form.getUsername());

			//账号不存在、密码错误
			if(user == null || !user.getPassword().equals(form.getPassword())) {
				return R.error("账号或密码不正确");
			}

			//账号锁定
			if(user.getStatus() == 0){
				return R.error("账号已被锁定,请联系管理员");
			}

			//生成token，并保存到数据库
			return sysUserTokenService.createToken(user.getUserId());
		});
	}

	/**
	 * 退出
	 */
	@PostMapping("/sys/logout")
	public Mono<R> logout() {
		return Mono.fromCallable(() -> {
			sysUserTokenService.logout(getUserId());
			return R.ok();
		});
	}

}
