package com.learning.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learning.gulimall.member.feign.CouponClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.learning.gulimall.member.entity.MemberEntity;
import com.learning.gulimall.member.service.MemberService;
import com.learning.gulimall.common.utils.PageUtils;
import com.learning.gulimall.common.utils.R;



/**
 * 会员
 *
 * @author lkl
 * @email lkl@gmail.com
 * @date 2025-05-26 18:33:16
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private CouponClient couponClient;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 获取会员信息
     */
    @GetMapping("/info/coupon")
    public R getMemberInfoWithCoupon() {
        R page = couponClient.couponList();
        return R.ok().put("member", page);
    }

}
