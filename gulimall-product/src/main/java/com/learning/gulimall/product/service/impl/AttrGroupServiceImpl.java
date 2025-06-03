package com.learning.gulimall.product.service.impl;

import com.learning.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.learning.gulimall.product.dao.AttrDao;
import com.learning.gulimall.product.dao.CategoryDao;
import com.learning.gulimall.product.entity.AttrEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learning.gulimall.common.utils.PageUtils;
import com.learning.gulimall.common.utils.Query;

import com.learning.gulimall.product.dao.AttrGroupDao;
import com.learning.gulimall.product.entity.AttrGroupEntity;
import com.learning.gulimall.product.entity.CategoryEntity;
import com.learning.gulimall.product.service.AttrGroupService;

@Slf4j
@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private AttrAttrgroupRelationDao relationDao;
    @Autowired
    private AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {

        // 构建查询条件
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();

        IPage<AttrGroupEntity> page;
        // 1. 如果分类ID不为0，添加分类过滤条件
        if (catelogId != 0) {
            wrapper.eq("catelog_id", catelogId);
        }

        // 2. 统一处理关键字搜索
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(obj ->
                    obj.like("attr_group_name", key)
                            .or()
                            .eq("attr_group_id", key)
            );
        }
        page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                wrapper
        );
//        log.info("查询属性分组列表，参数：{}, 结果：{}", params, page.getRecords());
        // 设置分类路径
        page.getRecords().forEach(attrGroup -> {
            attrGroup.setCatelogPath(getCatelogPath(attrGroup.getCatelogId()));
        });

        return new PageUtils(page);
    }

    /**
     * 获取分类完整路径
     */
    @Override
    public List<Long> getCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        CategoryEntity category = categoryDao.selectById(catelogId);

        while (category != null) {
            paths.add(category.getCatId());
            if (category.getParentCid() == 0) {
                break;
            }
            category = categoryDao.selectById(category.getParentCid());
        }

        Collections.reverse(paths);
        return paths;
    }

    @Override
    public List<AttrEntity> getRelationAttrsByAttrGroupId(Long attrgroupId) {
        List<Long> attrIds = relationDao.selectAttrIdsByAttrGroupId(attrgroupId);
        if (attrIds != null && !attrIds.isEmpty()) {
            return attrDao.findByAttrIds(attrIds);
        }
        return List.of();
    }

    @Override
    public PageUtils queryNoRelationAttrPage(Map<String, Object> params, Long attrgroupId) {
        // 构建查询条件
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>();
        IPage<AttrEntity> page = new Query<AttrEntity>().getPage(params);

        List<Long> attrIds = relationDao.selectAttrIdsNotEqualAttrGroupId(attrgroupId);
        if (attrIds != null && !attrIds.isEmpty()) {
            wrapper.in("attr_id", attrIds);
        }

        // 2. 统一处理关键字搜索
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(obj ->
                    obj.like("attr_name", key)
                            .or()
                            .eq("attr_id", key)
            );
        }

        page = attrDao.selectPage(
                page,
                wrapper
        );
        return new PageUtils(page);
    }

}