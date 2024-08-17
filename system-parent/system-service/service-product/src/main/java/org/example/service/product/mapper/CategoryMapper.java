package org.example.service.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.product.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {
    //所有的一级分类
    List<Category> selectOneCategory();

    //查询所有分类
    List<Category> findAll();
}
