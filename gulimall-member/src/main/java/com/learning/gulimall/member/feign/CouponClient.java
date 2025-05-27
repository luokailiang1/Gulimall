package com.learning.gulimall.member.feign;

import com.learning.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "gulimall-coupon")
public interface CouponClient {
    @GetMapping("/coupon/coupon/list")
    R couponList();
}
