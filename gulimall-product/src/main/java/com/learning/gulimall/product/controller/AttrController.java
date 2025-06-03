package com.learning.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.learning.gulimall.product.service.AttrGroupService;
import com.learning.gulimall.product.vo.AttrRespVO;
import com.learning.gulimall.product.vo.AttrVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learning.gulimall.product.entity.AttrEntity;
import com.learning.gulimall.product.service.AttrService;
import com.learning.gulimall.common.utils.PageUtils;
import com.learning.gulimall.common.utils.R;



/**
 * 商品属性
 *
 * @author lkl
 * @email lkl@gmail.com
 * @date 2025-05-25 23:55:02
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrGroupService attrGroupService;

    /**
     * 列表
     */
    @RequestMapping("/{attrType}/list/{catelogId}")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId, @PathVariable("attrType") String attrType){
        PageUtils page = attrService.queryPage(params, catelogId, attrType);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
		AttrRespVO attrRespVO = attrService.getAttrInfoById(attrId);
        attrRespVO.setCatelogPath(attrGroupService.getCatelogPath(attrRespVO.getCatelogId()));
        return R.ok().put("attr", attrRespVO);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVO attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVO attr){
		attrService.updateCascade(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.deleteCascadeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
