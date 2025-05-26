package com.learning.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learning.gulimall.product.entity.BrandEntity;
import com.learning.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;


    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();

//        brandEntity.setName("华为");
//        brandEntity.setDescript("华为手机");
//        brandService.save(brandEntity);
//        System.out.println("保存成功");
//        brandEntity.setBrandId(1L);
//        brandEntity.setName("小米");
//        brandEntity.setDescript("小米手机");
//        brandService.updateById(brandEntity);
//        System.out.println("更新成功");
//        System.out.println(brandService.list());
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>()
                .eq("brand_id", 1L)
                .or()
                .eq("brand_id", 2L)
                .like("name", "华为")
        );
        list.forEach(item -> {
            System.out.println(item.getName());
        });
    }

}
