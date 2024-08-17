package org.example.system.manager.service;

import com.github.pagehelper.PageInfo;
import org.example.system.model.entity.product.Brand;

import java.util.List;

public interface BrandService {
    PageInfo<Brand> findByPage(int page, int limit);

    void save(Brand brand);

    List<Brand> findAll();

    void updateById(Brand brand);

    void deleteById(Long id);
}
