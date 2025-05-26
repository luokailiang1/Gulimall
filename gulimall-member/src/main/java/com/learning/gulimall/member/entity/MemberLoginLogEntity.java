package com.learning.gulimall.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 会员登录记录
 * 
 * @author lkl
 * @email lkl@gmail.com
 * @date 2025-05-26 18:33:16
 */
@Data
@TableName("ums_member_login_log")
public class MemberLoginLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * member_id
	 */
	private Long memberId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * ip
	 */
	private String ip;
	/**
	 * city
	 */
	private String city;
	/**
	 * 登录类型[1-web，2-app]
	 */
	private Integer loginType;

}
