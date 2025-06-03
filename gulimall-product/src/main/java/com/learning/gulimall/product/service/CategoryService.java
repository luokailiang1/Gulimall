package com.learning.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learning.gulimall.common.utils.PageUtils;
import com.learning.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author lkl
 * @email lkl@gmail.com
 * @date 2025-05-25 23:55:02
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void deleteByIds(List<Long> idList);

    void updateCascade(CategoryEntity category);
}

