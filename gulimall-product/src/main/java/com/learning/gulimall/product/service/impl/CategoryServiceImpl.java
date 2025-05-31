package com.learning.gulimall.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learning.gulimall.common.utils.PageUtils;
import com.learning.gulimall.common.utils.Query;

import com.learning.gulimall.product.dao.CategoryDao;
import com.learning.gulimall.product.entity.CategoryEntity;
import com.learning.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);
        // 1. 按parentCid分组
        Map<Long, List<CategoryEntity>> parentMap = entities.stream()
                .collect(Collectors.groupingBy(CategoryEntity::getParentCid));
        // 2. 递归组装树
        return buildTree(0L, parentMap);
    }

    private List<CategoryEntity> buildTree(Long parentId, Map<Long, List<CategoryEntity>> parentMap) {
        List<CategoryEntity> children = parentMap.getOrDefault(parentId, new ArrayList<>());
        for (CategoryEntity child : children) {
            child.setChildren(buildTree(child.getCatId(), parentMap));
        }
        // 排序
        children.sort(Comparator.comparing(CategoryEntity::getSort, Comparator.nullsLast(Integer::compareTo)));
        return children;
    }

    @Override
    public void deleteByIds(List<Long> idList) {
        //TODO: 后续是否需要enhance

        // 1. 检查是否有子分类
        List<CategoryEntity> entities = baseMapper.selectBatchIds(idList);
        if (entities != null && entities.size() > 0) {}
        List<Long> childIds = entities.stream()
                .flatMap(entity -> entity.getChildren() == null || entity.getChildren().isEmpty()
                        ? Stream.empty()
                        : entity.getChildren().stream())
                .map(CategoryEntity::getCatId)
                .collect(Collectors.toList());

        // 2. 删除子分类
        if (!childIds.isEmpty()) {
            baseMapper.deleteBatchIds(childIds);
        }

        // 3. 删除父分类
        baseMapper.deleteBatchIds(idList);
    }

}