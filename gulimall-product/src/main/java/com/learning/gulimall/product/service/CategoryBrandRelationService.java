package com.learning.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learning.gulimall.common.utils.PageUtils;
import com.learning.gulimall.product.dto.CatelogInfoDto;
import com.learning.gulimall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author lkl
 * @email lkl@gmail.com
 * @date 2025-05-25 23:55:02
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<CategoryBrandRelationEntity> getCatelogInfoByBrandId(Long brandId);

    void saveDetail(CategoryBrandRelationEntity CategoryBrandRelationEntity);
}

