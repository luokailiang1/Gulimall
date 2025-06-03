package com.learning.gulimall.product.dao;

import com.learning.gulimall.product.dto.CatelogInfoDto;
import com.learning.gulimall.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jakarta.validation.constraints.AssertFalse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 品牌分类关联
 * 
 * @author lkl
 * @email lkl@gmail.com
 * @date 2025-05-25 23:55:02
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

    @Select("select * from pms_category_brand_relation where brand_id = #{brandId}")
    List<CategoryBrandRelationEntity> getCatelogInfoByBrandId(Long brandId);
}
