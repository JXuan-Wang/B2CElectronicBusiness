package org.example.system.manager.service;

import com.github.pagehelper.PageInfo;
import org.example.system.model.dto.product.CategoryBrandDto;
import org.example.system.model.entity.product.Brand;
import org.example.system.model.entity.product.CategoryBrand;

import java.util.List;

public interface CategoryBrandService {
    PageInfo<CategoryBrand> findByPage(int page, int limit, CategoryBrandDto categoryBrandDto);

    void save(CategoryBrandDto categoryBrandDto);

    void updateById(CategoryBrand categoryBrand);

    void deleteById(Long id);

    //根据分类id查询对应品牌数据
    List<Brand> findBrandByCategoryId(Long categoryId);
}
