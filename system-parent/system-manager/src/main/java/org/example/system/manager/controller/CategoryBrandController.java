package org.example.system.manager.controller;

import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.system.manager.service.CategoryBrandService;
import org.example.system.model.dto.product.CategoryBrandDto;
import org.example.system.model.entity.product.Brand;
import org.example.system.model.entity.product.CategoryBrand;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product/categoryBrand")
public class CategoryBrandController {
    @Resource
    private CategoryBrandService categoryBrandService;

    //添加
    @PostMapping("/save")
    public Result save(@RequestBody CategoryBrandDto categoryBrandDto) {
        categoryBrandService.save(categoryBrandDto);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //分类品牌条件分页查询
    @GetMapping("/{page}/{limit}")
    public Result findByPage(@PathVariable("page") int page,
                             @PathVariable("limit") int limit,
                             CategoryBrandDto categoryBrandDto) {
        PageInfo<CategoryBrand> pageInfo = categoryBrandService.findByPage(page, limit, categoryBrandDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    @PutMapping("updateById")
    public Result updateById(@RequestBody CategoryBrand categoryBrand) {
        categoryBrandService.updateById(categoryBrand);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Long id) {
        categoryBrandService.deleteById(id);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    //根据分类id查询对应品牌数据
    @GetMapping("/findBrandByCategoryId/{categoryId}")
    public Result findBrandByCategoryId(@PathVariable("categoryId") Long categoryId) {
        List<Brand> list = categoryBrandService.findBrandByCategoryId(categoryId);
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }
}
