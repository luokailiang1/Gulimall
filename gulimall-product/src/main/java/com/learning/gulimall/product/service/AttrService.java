package com.learning.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learning.gulimall.common.utils.PageUtils;
import com.learning.gulimall.product.entity.AttrEntity;
import com.learning.gulimall.product.vo.AttrRespVO;
import com.learning.gulimall.product.vo.AttrVO;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author lkl
 * @email lkl@gmail.com
 * @date 2025-05-25 23:55:02
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params, Long catelogId, String attrType);

    void saveAttr(AttrVO attr);

    void updateCascade(AttrVO attr);

    void deleteCascadeByIds(List<Long> attrIds);

    AttrRespVO getAttrInfoById(Long attrId);

}

