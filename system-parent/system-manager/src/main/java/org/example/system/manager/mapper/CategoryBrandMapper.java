package org.example.system.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.dto.product.CategoryBrandDto;
import org.example.system.model.entity.product.Brand;
import org.example.system.model.entity.product.CategoryBrand;

import java.util.List;

@Mapper
public interface CategoryBrandMapper {
    List<CategoryBrand> findByPage(CategoryBrandDto categoryBrandDto);

    void save(CategoryBrandDto categoryBrandDto);

    void updateById(CategoryBrand categoryBrand);

    void deleteById(Long id);

    //根据分类id查询对应品牌数据
    List<Brand> findBrandByCategoryId(Long categoryId);
}
