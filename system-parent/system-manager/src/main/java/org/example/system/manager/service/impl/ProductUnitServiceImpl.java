package org.example.system.manager.service.impl;

import jakarta.annotation.Resource;
import org.example.system.manager.mapper.ProductUnitMapper;
import org.example.system.manager.service.ProductUnitService;
import org.example.system.model.entity.base.ProductUnit;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductUnitServiceImpl implements ProductUnitService {
    @Resource
    private ProductUnitMapper productUnitMapper;

    @Override
    public List<ProductUnit> findAll() {
        return productUnitMapper.findAll();
    }
}
