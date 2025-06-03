package com.learning.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.learning.gulimall.product.dao.CategoryBrandRelationDao;
import com.learning.gulimall.product.entity.CategoryBrandRelationEntity;
import com.learning.gulimall.product.service.CategoryBrandRelationService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationDao categoryBrandRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = null;
        if (params.get("key") == null || ((String) params.get("key")).isEmpty()) {
            page = this.page(
                    new Query<CategoryEntity>().getPage(params),
                    new QueryWrapper<CategoryEntity>()
            );
        } else {
            // 构建查询条件
            QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
            // 添加模糊查询条件
            wrapper.and((obj) -> {
                obj.like("name", params.get("key"))
                        .or()
                        .eq("cat_id", params.get("key"));
            });

            page = this.page(
                    new Query<CategoryEntity>().getPage(params),
                    wrapper
            );
        }

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
            List<CategoryEntity> childTree = buildTree(child.getCatId(), parentMap);
            if (!childTree.isEmpty()) {
                child.setChildren(childTree);
            }
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
        if (entities != null && entities.size() > 0) {
        }
        List<Long> childIds = entities.stream()
                .flatMap(entity -> entity.getChildren() == null || entity.getChildren().isEmpty()
                        ? Stream.empty()
                        : entity.getChildren().stream())
                .map(CategoryEntity::getCatId)
                .collect(Collectors.toList());

        try {
            // 2. 删除子分类
            if (!childIds.isEmpty()) {
                baseMapper.deleteBatchIds(childIds);
            }
            log.info("删除子分类成功，删除的分类ID列表: {}", childIds);

            // 3. 删除父分类
            baseMapper.deleteBatchIds(idList);
            log.info("删除父分类成功，删除的分类ID列表: {}", idList);
        } catch (Exception e) {
            // 4. 如果删除失败，抛出异常
            log.error("删除分类失败，请检查是否有相关商品或其他依赖。");
        }
    }

    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        // 这里可以添加其他需要更新的逻辑，比如更新关联表等
        if(category.getName() != null && !category.getName().isEmpty()) {
            categoryBrandRelationDao.update(
                    new UpdateWrapper<CategoryBrandRelationEntity>()
                            .set("catelog_name", category.getName())
                            .eq("catelog_id", category.getCatId())
            );
        }

        log.info("更新分类详情成功: {}", category);
    }

}