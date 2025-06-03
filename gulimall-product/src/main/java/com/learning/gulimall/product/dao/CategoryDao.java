package com.learning.gulimall.product.dao;

import com.learning.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * 商品三级分类
 * 
 * @author lkl
 * @email lkl@gmail.com
 * @date 2025-05-25 23:55:02
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
    @Select("select parent_cid from pms_category where cat_id = #{catId}")
    Long findParentIdByCatId(Long id);

    @Select("select * from pms_category where cat_id = #{catId}")
    CategoryEntity findByCatId(Long catId);

    List<CategoryEntity> findByCatIds(Set<Long> catelogIds);
}
