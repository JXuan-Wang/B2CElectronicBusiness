package org.example.system.manager.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.example.system.manager.service.CategoryService;
import org.example.system.model.entity.product.Category;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/product/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    //分类列表，每次查询一层数据
    @GetMapping("/findCategoryList/{id}")
    public Result findCategoryList(@PathVariable("id") Long id) {
        List<Category> list=categoryService.findCategoryList(id);
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }

    //导出
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response){
        categoryService.exportData(response);
    }

    //导入
    @PostMapping("/importData")
    public Result importData(MultipartFile file){
        //获取上传文件
        categoryService.importData(file);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
