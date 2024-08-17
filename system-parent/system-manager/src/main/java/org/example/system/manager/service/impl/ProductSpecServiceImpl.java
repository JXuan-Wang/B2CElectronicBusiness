package org.example.system.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.system.manager.mapper.ProductSpecMapper;
import org.example.system.manager.service.ProductSpecService;
import org.example.system.model.entity.product.ProductSpec;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSpecServiceImpl implements ProductSpecService {
    @Resource
    private ProductSpecMapper productSpecMapper;

    @Override
    public PageInfo<ProductSpec> findByPage(int page, int limit) {
        PageHelper.startPage(page,limit);
        List<ProductSpec> list= productSpecMapper.findByPage();
        return new PageInfo<>(list);
    }

    @Override
    public void save(ProductSpec productSpec) {
        productSpecMapper.save(productSpec);
    }

    @Override
    public void updateById(ProductSpec productSpec) {
        productSpecMapper.update(productSpec);
    }

    @Override
    public void deleteById(Long id) {
        productSpecMapper.delete(id);
    }

    @Override
    public List<ProductSpec> findAll() {
        return productSpecMapper.findAll();
    }
}
