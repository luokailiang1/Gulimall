package com.learning.gulimall.coupon.dao;

import com.learning.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author lkl
 * @email lkl@gmail.com
 * @date 2025-05-26 18:19:50
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
