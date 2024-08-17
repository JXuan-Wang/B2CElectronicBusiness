package org.example.service.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.service.product.service.CategoryService;
import org.example.system.model.entity.product.Category;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "分类接口管理")
@RestController
@RequestMapping(value="/api/product/category")
//@CrossOrigin //解决跨域
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    //查询所有分类，树形封装
    @Operation(summary = "获取分类树形数据")
    @GetMapping("findCategoryTree")
    public Result<List<Category>> findCategoryTree() {
       List<Category> list=categoryService.findCategoryTree();
       return Result.build(list, ResultCodeEnum.SUCCESS);

    }
}
