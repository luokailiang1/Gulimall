package com.learning.gulimall.product.vo;

import lombok.Data;

import java.util.List;

@Data
public class AttrRespVO extends AttrVO{

    private String catelogName;

    private String groupName;

    private List<Long> catelogPath;
}
