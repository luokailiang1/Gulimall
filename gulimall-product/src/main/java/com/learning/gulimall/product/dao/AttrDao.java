package com.learning.gulimall.product.dao;

import com.learning.gulimall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品属性
 * 
 * @author lkl
 * @email lkl@gmail.com
 * @date 2025-05-25 23:55:02
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    List<AttrEntity> findByAttrIds(List<Long> attrIds);
}
