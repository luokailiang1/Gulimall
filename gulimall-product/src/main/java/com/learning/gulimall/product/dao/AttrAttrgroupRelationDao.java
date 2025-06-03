package com.learning.gulimall.product.dao;

import com.learning.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author lkl
 * @email lkl@gmail.com
 * @date 2025-05-25 23:55:02
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    @Select("SELECT attr_group_id FROM pms_attr_attrgroup_relation WHERE attr_id = #{attrId}")
    Long findAttrGroupIdByAttrId(Long attrId);

    @Select("SELECT attr_id FROM pms_attr_attrgroup_relation WHERE attr_group_id = #{attrgroupId}")
    List<Long> selectAttrIdsByAttrGroupId(Long attrgroupId);

    @Select("SELECT attr_id FROM pms_attr_attrgroup_relation WHERE attr_group_id != #{attrgroupId}")
    List<Long> selectAttrIdsNotEqualAttrGroupId(Long attrgroupId);
}
