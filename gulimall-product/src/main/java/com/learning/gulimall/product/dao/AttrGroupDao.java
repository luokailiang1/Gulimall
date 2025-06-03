package com.learning.gulimall.product.dao;

import com.learning.gulimall.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * 属性分组
 * 
 * @author lkl
 * @email lkl@gmail.com
 * @date 2025-05-25 23:55:02
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    @Select("SELECT * FROM pms_attr_group WHERE attr_group_id = #{attrGroupId}")
    AttrGroupEntity findByGroupId(Long attrGroupIdByAttrId);

    List<AttrGroupEntity> findByGroupIds(Set<Long> groupIds);
}
