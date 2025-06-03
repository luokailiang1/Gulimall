package com.learning.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learning.gulimall.common.utils.PageUtils;
import com.learning.gulimall.product.entity.AttrAttrgroupRelationEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author lkl
 * @email lkl@gmail.com
 * @date 2025-05-25 23:55:02
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

