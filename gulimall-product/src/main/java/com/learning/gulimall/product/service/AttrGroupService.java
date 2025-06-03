package com.learning.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learning.gulimall.common.utils.PageUtils;
import com.learning.gulimall.product.entity.AttrEntity;
import com.learning.gulimall.product.entity.AttrGroupEntity;

import java.util.Map;
import java.util.List;

/**
 * 属性分组
 *
 * @author lkl
 * @email lkl@gmail.com
 * @date 2025-05-25 23:55:02
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    List<Long> getCatelogPath(Long attrGroupId);

    List<AttrEntity> getRelationAttrsByAttrGroupId(Long attrgroupId);

    PageUtils queryNoRelationAttrPage(Map<String, Object> params,Long attrGroupId);
}

