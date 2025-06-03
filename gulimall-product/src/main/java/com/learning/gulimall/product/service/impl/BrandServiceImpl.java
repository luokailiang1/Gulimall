package com.learning.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.learning.gulimall.product.dao.CategoryBrandRelationDao;
import com.learning.gulimall.product.entity.CategoryBrandRelationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learning.gulimall.common.utils.PageUtils;
import com.learning.gulimall.common.utils.Query;

import com.learning.gulimall.product.dao.BrandDao;
import com.learning.gulimall.product.entity.BrandEntity;
import com.learning.gulimall.product.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    private CategoryBrandRelationDao categoryBrandRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BrandEntity> page = null;
        if (params.get("key") != null || ((String) params.get("key")).isEmpty()) {
            String key = (String) params.get("key");
            QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
            wrapper.and((obj) -> obj.eq("brand_id", key).or().like("name", key));
            page = this.page(
                    new Query<BrandEntity>().getPage(params),
                    wrapper
            );
        } else {
            page = this.page(
                    new Query<BrandEntity>().getPage(params),
                    new QueryWrapper<BrandEntity>()
            );
        }

        return new PageUtils(page);
    }

    @Override
    public void updateStatus(BrandEntity entity) {
        this.update(
                new UpdateWrapper<BrandEntity>()
                        .set("show_status", entity.getShowStatus())
                        .eq("brand_id", entity.getBrandId())
        );
    }

    @Override
    public void updateCascade(BrandEntity brand) {
        this.updateById(brand);
        // 更新关联表中的品牌名称
        if( brand.getName() != null && !brand.getName().isEmpty()) {
            categoryBrandRelationDao.update(
                    new UpdateWrapper<CategoryBrandRelationEntity>()
                            .set("brand_name", brand.getName())
                            .eq("brand_id", brand.getBrandId())
            );
        }
    }

}