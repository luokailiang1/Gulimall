package com.learning.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.learning.gulimall.product.entity.CategoryEntity;
import com.learning.gulimall.product.service.CategoryService;
import com.learning.gulimall.common.utils.PageUtils;
import com.learning.gulimall.common.utils.R;



/**
 * 商品三级分类
 *
 * @author lkl
 * @email lkl@gmail.com
 * @date 2025-05-25 23:55:02
 */
@Slf4j
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @GetMapping("/list/tree")
    public R list(){
        List<CategoryEntity> categoryEntities = categoryService.listWithTree();

        return R.ok().put("data", categoryEntities);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody CategoryEntity category){
        try {
            // 这里可以添加一些业务逻辑，比如验证数据等
            categoryService.save(category);
            log.info("保存分类成功: {}", category);
        } catch (Exception e) {
            return R.error("保存分类失败: " + e.getMessage());
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 批量修改
     */
    @PostMapping("/update/sort")
    public R updateBatch(@RequestBody List<CategoryEntity> categories) {
        if (categories == null || categories.isEmpty()) {
            return R.error("参数不能为空");
        }
        try {
            categoryService.updateBatchById(categories, 50);
            log.info("批量更新分类成功, 数量: {}", categories.size());
        } catch (Exception e) {
            log.error("批量更新分类失败", e);
            return R.error("批量更新分类失败: " + e.getMessage());
        }
        return R.ok();
    }
    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] catIds){
        categoryService.deleteByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
