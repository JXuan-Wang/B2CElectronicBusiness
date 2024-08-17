package org.example.service.product.service.impl;

import jakarta.annotation.Resource;
import org.example.service.product.mapper.BrandMapper;
import org.example.service.product.service.BrandService;
import org.example.system.model.entity.product.Brand;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Resource
    private BrandMapper brandMapper;

    @Override
    @Cacheable(value = "brandList", unless="#result.size() == 0")
    public List<Brand> findAll() {
        return brandMapper.findAll();
    }
}
