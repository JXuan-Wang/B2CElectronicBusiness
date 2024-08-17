package org.example.service.product.service;

import org.example.system.model.entity.product.Category;

import java.util.List;

public interface CategoryService {
    // 所有的一级分类
    List<Category> selectOneCategory();

    //查询所有分类，树形封装
    List<Category> findCategoryTree();
}
