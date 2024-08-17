package org.example.system.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.system.manager.mapper.CategoryBrandMapper;
import org.example.system.manager.mapper.CategoryMapper;
import org.example.system.manager.service.CategoryBrandService;
import org.example.system.model.dto.product.CategoryBrandDto;
import org.example.system.model.entity.product.Brand;
import org.example.system.model.entity.product.CategoryBrand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryBrandServiceImpl implements CategoryBrandService {
    @Resource
    private CategoryBrandMapper categoryBrandMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    //分类品牌条件分页查询
    @Override
    public PageInfo<CategoryBrand> findByPage(int page, int limit, CategoryBrandDto categoryBrandDto) {
        PageHelper.startPage(page,limit);
        List<CategoryBrand> list=categoryBrandMapper.findByPage(categoryBrandDto);
        PageInfo<CategoryBrand> pageInfo=new PageInfo<>(list);

        return pageInfo;
    }

    //添加
    @Override
    public void save(CategoryBrandDto categoryBrandDto) {
        categoryBrandMapper.save(categoryBrandDto);
    }

    @Override
    public void updateById(CategoryBrand categoryBrand) {
        categoryBrandMapper.updateById(categoryBrand) ;
    }
    @Override
    public void deleteById(Long id) {
        categoryBrandMapper.deleteById(id) ;
    }

    //根据分类id查询对应品牌数据
    @Override
    public List<Brand> findBrandByCategoryId(Long categoryId) {
        return categoryBrandMapper.findBrandByCategoryId(categoryId);
    }
}
