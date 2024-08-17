package org.example.system.manager.service;

import com.github.pagehelper.PageInfo;
import org.example.system.model.entity.product.ProductSpec;

import java.util.List;

public interface ProductSpecService {
    PageInfo<ProductSpec> findByPage(int page, int limit);

    void save(ProductSpec productSpec);

    void updateById(ProductSpec productSpec);

    void deleteById(Long id);

    List<ProductSpec> findAll();

}
