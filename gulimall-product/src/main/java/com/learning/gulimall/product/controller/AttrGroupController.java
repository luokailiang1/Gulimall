package com.learning.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.List;

import com.learning.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.learning.gulimall.product.entity.AttrEntity;
import com.learning.gulimall.product.service.AttrAttrgroupRelationService;
import com.learning.gulimall.product.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.learning.gulimall.product.entity.AttrGroupEntity;
import com.learning.gulimall.product.service.AttrGroupService;
import com.learning.gulimall.common.utils.PageUtils;
import com.learning.gulimall.common.utils.R;



/**
 * 属性分组
 *
 * @author lkl
 * @email lkl@gmail.com
 * @date 2025-05-25 23:55:02
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    private AttrService attrService;
    /**
     * 列表
     */
    @GetMapping("/list/{catelogId}")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId){
        PageUtils page = attrGroupService.queryPage(params, catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        List<Long> catelogPath = attrGroupService.getCatelogPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(catelogPath); // 设置三级分类路径
        return R.ok().put("attrGroup", attrGroup);
    }

    @PostMapping("/attr/relation")
    public R addRelationToAttr(@RequestBody List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntityList) {
        if (attrAttrgroupRelationEntityList == null || attrAttrgroupRelationEntityList.isEmpty()) {
            return R.error("关联数据不能为空");
        }
        attrAttrgroupRelationService.saveBatch(attrAttrgroupRelationEntityList);
        return R.ok();
    }


    @GetMapping("/{attrgroupId}/attr/relation")
    public R getRelationAttr(@PathVariable Long attrgroupId) {
        List<AttrEntity> attrEntities = attrGroupService.getRelationAttrsByAttrGroupId(attrgroupId);
        return R.ok().put("data", attrEntities);
    }

    //TODO: 获取属性分组没有关联的其他属性
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R getNoRelationAttr(@RequestParam Map<String, Object> params, @PathVariable Long attrgroupId) {
        PageUtils page = attrGroupService.queryNoRelationAttrPage(params, attrgroupId);
//        List<AttrEntity> attrEntities = attrGroupService.getNoRelationAttrsByAttrGroupId(attrgroupId);
        return R.ok().put("page", page);
    }
    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
