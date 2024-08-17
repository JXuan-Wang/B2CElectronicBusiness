package org.example.service.product.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.product.Brand;

import java.util.List;

@Mapper
public interface BrandMapper {
    List<Brand> findAll();
}
