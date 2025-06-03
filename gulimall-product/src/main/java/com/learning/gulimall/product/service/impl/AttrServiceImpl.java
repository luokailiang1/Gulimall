package com.learning.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.learning.gulimall.common.constant.ProductConstant;
import com.learning.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.learning.gulimall.product.dao.AttrGroupDao;
import com.learning.gulimall.product.dao.CategoryDao;
import com.learning.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.learning.gulimall.product.entity.AttrGroupEntity;
import com.learning.gulimall.product.entity.CategoryEntity;
import com.learning.gulimall.product.vo.AttrRespVO;
import com.learning.gulimall.product.vo.AttrVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learning.gulimall.common.utils.PageUtils;
import com.learning.gulimall.common.utils.Query;

import com.learning.gulimall.product.dao.AttrDao;
import com.learning.gulimall.product.entity.AttrEntity;
import com.learning.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId, String attrType) {
        // 构建查询条件
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>();
        IPage<AttrEntity> page;
        // 1. 根据属性类型过滤
        if ("base".equalsIgnoreCase(attrType)) {
            wrapper.eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()); // 基础属性
        } else if ("sale".equalsIgnoreCase(attrType)) {
            wrapper.eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()); // 销售属性
        } else {
            throw new IllegalArgumentException("Invalid attrType: " + attrType);
        }
        // 1. 如果分类ID不为0，添加分类过滤条件
        if (catelogId != 0) {
            wrapper.and(obj ->
                    obj.eq("catelog_id", catelogId)
                    );
        }
        // 2. 统一处理关键字搜索
        String key = (String) params.get("key");
        if (key != null && !key.isEmpty()) {
            wrapper.and(obj ->
                    obj.like("attr_name", key)
                            .or()
                            .eq("attr_id", key)
            );
        }
        page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);

        // 3. 设置分类和分组名称
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVO> attrRespVOS = new ArrayList<>();
        if (records != null && !records.isEmpty()) {
            // 批量提取catelogId和attrId
            Set<Long> catelogIds = records.stream().map(AttrEntity::getCatelogId).collect(Collectors.toSet());
            Set<Long> attrIds = records.stream().map(AttrEntity::getAttrId).collect(Collectors.toSet());

            // 批量查询分类
            List<CategoryEntity> categories = categoryDao.findByCatIds(catelogIds);
            Map<Long, String> catelogNameMap = new HashMap<>();
            if (categories != null) {
                for (CategoryEntity category : categories) {
                    catelogNameMap.put(category.getCatId(), category.getName());
                }
            }

            // 批量查询属性-分组关系
            List<AttrAttrgroupRelationEntity> relations = attrAttrgroupRelationDao.selectList(
                    new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_id", attrIds)
            );
            Map<Long, Long> attrIdToGroupId = new HashMap<>();
            for (AttrAttrgroupRelationEntity rel : relations) {
                attrIdToGroupId.put(rel.getAttrId(), rel.getAttrGroupId());
            }
            Set<Long> groupIds = new HashSet<>(attrIdToGroupId.values());

            // 批量查询分组
            List<AttrGroupEntity> groups = attrGroupDao.findByGroupIds(groupIds);
            Map<Long, String> groupNameMap = new HashMap<>();
            if (groups != null) {
                for (AttrGroupEntity group : groups) {
                    groupNameMap.put(group.getAttrGroupId(), group.getAttrGroupName());
                }
            }

            // 组装VO
            for (AttrEntity attr : records) {
                AttrRespVO attrRespVO = new AttrRespVO();
                BeanUtils.copyProperties(attr, attrRespVO);
                // 分类名
                attrRespVO.setCatelogName(catelogNameMap.getOrDefault(attr.getCatelogId(), ""));
                // 分组名
                Long groupId = attrIdToGroupId.get(attr.getAttrId());
                if (groupId != null) {
                    attrRespVO.setGroupName(groupNameMap.getOrDefault(groupId, ""));
                } else {
                    attrRespVO.setGroupName("");
                }
                attrRespVOS.add(attrRespVO);
            }
        }
        pageUtils.setList(attrRespVOS);
        return pageUtils;
    }

    @Override
    @Transactional
    public void saveAttr(AttrVO attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        // 直接调用父类的save方法
        this.save(attrEntity);
        // 保存关联关系
        if( attr.getAttrType().equals(ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) ) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }
    }

    @Override
    @Transactional
    public void updateCascade(AttrVO attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        // 直接调用父类的save方法
        this.updateById(attrEntity);

        // 如果是销售属性，则不需要更新关联关系
        if (attr.getAttrType().equals(ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode())) {
            return;
        }

        // 检查是否存在关联关系
        Long count = attrAttrgroupRelationDao.selectCount(new UpdateWrapper<AttrAttrgroupRelationEntity>()
                .set("attr_group_id", attr.getAttrGroupId())
                .eq("attr_id", attr.getAttrId()));

        // 更新关联关系
        if (count > 0) {
            attrAttrgroupRelationDao.update(
                    new UpdateWrapper<AttrAttrgroupRelationEntity>()
                            .set("attr_group_id", attr.getAttrGroupId())
                            .eq("attr_id", attr.getAttrId())
            );
        }
        else {
            // 如果没有关联关系，则插入新的关联关系
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attr.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }
    }

    @Override
    @Transactional
    public void deleteCascadeByIds(List<Long> attrIds) {
        // 删除属性
        this.removeByIds(attrIds);
        // 删除关联关系
        attrAttrgroupRelationDao.delete(
                new UpdateWrapper<AttrAttrgroupRelationEntity>()
                        .in("attr_id", attrIds)
        );
    }

    @Override
    public AttrRespVO getAttrInfoById(Long attrId) {
        AttrRespVO attrRespVO = new AttrRespVO();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity, attrRespVO);

        //设置分组信息
        if( attrEntity.getAttrType().equals(ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) ) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(
                    new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId)
            );
            if (attrAttrgroupRelationEntity != null) {
                attrRespVO.setAttrGroupId(attrAttrgroupRelationEntity.getAttrGroupId());
            }
        }

        return attrRespVO;
    }

}
