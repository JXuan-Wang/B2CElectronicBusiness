package org.example.service.product.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.example.service.product.mapper.CategoryMapper;
import org.example.service.product.service.CategoryService;
import org.example.system.model.entity.product.Category;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    //所有的一级分类
    @Override
    @Cacheable(value="category",key="'one'")
    public List<Category> selectOneCategory() {
        // 查询redis，是否有所有一级分类
//        String categoryOneJson=redisTemplate.opsForValue().get("category:one");

        // 如果redis包含所有一级分类，直接返回
//        if(!StringUtils.isEmpty(categoryOneJson)){
//            //categoryOneJson字符串转换List<Category>
//            return JSON.parseArray(categoryOneJson, Category.class);
//        }

        // 如果redis没有所有一级分类，查询数据库，吧数据库查询内容返回，并且查询内容放到redis里面
        //        redisTemplate.opsForValue().set("category:one"
//                ,JSON.toJSONString(categoryList)
//                ,7, TimeUnit.DAYS);

        return categoryMapper.selectOneCategory();
    }

    //查询所有分类，树形封装
    // category::all
    @Override
    @Cacheable(value="category",key="'all'")
    public List<Category> findCategoryTree() {
        List<Category> categoryList = categoryMapper.findAll();
        //全部一级分类
        List<Category> oneCategoryList = categoryList.stream().filter(item -> item.getParentId().longValue() == 0).collect(Collectors.toList());

        if(!CollectionUtils.isEmpty(oneCategoryList)) {
            oneCategoryList.forEach(oneCategory -> {
                List<Category> twoCategoryList = categoryList
                        .stream()
                        .filter(item -> item.getParentId().longValue() == oneCategory.getId().longValue())
                        .collect(Collectors.toList());
                oneCategory.setChildren(twoCategoryList);

                if(!CollectionUtils.isEmpty(twoCategoryList)) {
                    twoCategoryList.forEach(twoCategory -> {
                        List<Category> threeCategoryList = categoryList
                                .stream()
                                .filter(item -> item.getParentId().longValue() == twoCategory.getId().longValue())
                                .collect(Collectors.toList());
                        twoCategory.setChildren(threeCategoryList);
                    });
                }
            });
        }
        return oneCategoryList;
    }
}
