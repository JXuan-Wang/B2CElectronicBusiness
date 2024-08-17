package org.example.system.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.product.Brand;

import java.util.List;

@Mapper
public interface BrandMapper {
    List<Brand> findByPage();

    void save(Brand brand);

    List<Brand> findAll();

    void updateById(Brand brand);

    void deleteById(Long id);
}
