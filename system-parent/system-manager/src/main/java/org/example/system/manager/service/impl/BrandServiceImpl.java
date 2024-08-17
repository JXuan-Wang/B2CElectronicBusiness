package org.example.system.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.system.manager.mapper.BrandMapper;
import org.example.system.manager.service.BrandService;
import org.example.system.model.entity.product.Brand;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Resource
    private BrandMapper brandMapper;

    @Override
    public PageInfo<Brand> findByPage(int page, int limit) {
        PageHelper.startPage(page,limit);
        List<Brand> list= brandMapper.findByPage();
        PageInfo<Brand> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    //添加
    @Override
    public void save(Brand brand) {
        brandMapper.save(brand);
    }

    @Override
    public List<Brand> findAll() {
        return brandMapper.findAll();
    }

    @Override
    public void updateById(Brand brand) {
        brandMapper.updateById(brand) ;
    }

    @Override
    public void deleteById(Long id) {
        brandMapper.deleteById(id) ;
    }
}
