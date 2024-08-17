package org.example.service.product.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.service.product.service.CategoryService;
import org.example.service.product.service.ProductService;
import org.example.system.model.entity.product.Category;
import org.example.system.model.entity.product.ProductSku;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.example.system.model.vo.h5.IndexVo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name="首页接口管理")
@RestController
@RequestMapping(value="/api/product/index")
//@CrossOrigin  //解决跨域问题
public class IndexController {
    @Resource
    private ProductService productService;
    @Resource
    private CategoryService categoryService;

    @GetMapping
    public Result index(){
        // 所有的一级分类
        List<Category> categoryList=categoryService.selectOneCategory();

        //根据销量进行排序，获取前10条记录
        List<ProductSku> productSkuList=productService.selectProductSkuBySale();

        // 封装数据到vo对象里面
        IndexVo indexVo = new IndexVo();
        indexVo.setCategoryList(categoryList);
        indexVo.setProductSkuList(productSkuList);
        return Result.build(indexVo, ResultCodeEnum.SUCCESS);
    }
}
