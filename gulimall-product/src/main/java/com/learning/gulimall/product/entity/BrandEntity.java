package com.learning.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.learning.gulimall.common.valid.AddGroup;
import com.learning.gulimall.common.valid.ListValue;
import com.learning.gulimall.common.valid.UpdateGroup;
import com.learning.gulimall.common.valid.UpdateStatusGroup;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

/**
 * 品牌
 * 
 * @author lkl
 * @email lkl@gmail.com
 * @date 2025-05-25 23:55:02
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@NotNull(message = "品牌ID不能为空", groups = {UpdateGroup.class, UpdateStatusGroup.class})
	@Null(message = "品牌ID必须为空", groups = {AddGroup.class})
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(message = "品牌logo不能为空", groups = {AddGroup.class})
	@URL(message = "品牌logo必须是一个合法的URL", groups = {AddGroup.class, UpdateGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@TableLogic(value = "1", delval = "0")
	@ListValue(values = {0, 1}, groups = {AddGroup.class, UpdateStatusGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotBlank(message = "品牌首字母不能为空", groups = {AddGroup.class})
	@Pattern(regexp = "^[a-zA-Z]$", message = "品牌首字母必须是一个字母", groups = {AddGroup.class, UpdateGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@Min(value = 0, message = "排序值不能小于0", groups = {AddGroup.class, UpdateGroup.class})
	@NotNull(message = "排序值不能为空", groups = {AddGroup.class})
	private Integer sort;

}
