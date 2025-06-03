package com.learning.gulimall.product.service.impl;

import com.learning.gulimall.product.dao.BrandDao;
import com.learning.gulimall.product.dao.CategoryDao;
import com.learning.gulimall.product.dto.CatelogInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learning.gulimall.common.utils.PageUtils;
import com.learning.gulimall.common.utils.Query;

import com.learning.gulimall.product.dao.CategoryBrandRelationDao;
import com.learning.gulimall.product.entity.CategoryBrandRelationEntity;
import com.learning.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private CategoryBrandRelationDao categoryBrandRelationDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private BrandDao brandDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryBrandRelationEntity> getCatelogInfoByBrandId(Long brandId) {
        List<CategoryBrandRelationEntity> categoryBrandRelationEntityList = categoryBrandRelationDao.getCatelogInfoByBrandId(brandId);
        return categoryBrandRelationEntityList;
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity CategoryBrandRelationEntity) {
        enhanceCategoryBrandRelation(CategoryBrandRelationEntity);
        categoryBrandRelationDao.insert(CategoryBrandRelationEntity);
    }

    private void enhanceCategoryBrandRelation(CategoryBrandRelationEntity categoryBrandRelation) {
        // 获取品牌名称
        String brandName = brandDao.selectById(categoryBrandRelation.getBrandId()).getName();
        categoryBrandRelation.setBrandName(brandName);

        // 获取分类名称
        String catelogName = categoryDao.selectById(categoryBrandRelation.getCatelogId()).getName();
        categoryBrandRelation.setCatelogName(catelogName);
    }

}