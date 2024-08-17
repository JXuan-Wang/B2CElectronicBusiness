package org.example.system.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.product.ProductSpec;

import java.util.List;

@Mapper
public interface ProductSpecMapper {
    List<ProductSpec> findByPage();

    void save(ProductSpec productSpec);

    void update(ProductSpec productSpec);

    void delete(Long id);

    List<ProductSpec> findAll();
}
