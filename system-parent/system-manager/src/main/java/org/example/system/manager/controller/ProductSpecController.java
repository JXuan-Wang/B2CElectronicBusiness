package org.example.system.manager.controller;

import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.system.manager.service.ProductSpecService;
import org.example.system.model.entity.product.ProductSpec;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product/productSpec")
public class ProductSpecController {
    @Resource
    private ProductSpecService productSpecService;

    //列表
    @GetMapping("/{page}/{limit}")
    public Result list(@PathVariable("page") int page,
                       @PathVariable("limit") int limit) {
        PageInfo<ProductSpec> pageInfo = productSpecService.findByPage(page, limit);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    //添加
    @PostMapping("/save")
    public Result save(@RequestBody ProductSpec productSpec) {
        productSpecService.save(productSpec);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //修改
    @PostMapping("/updateById")
    public Result updateById(@RequestBody ProductSpec productSpec) {
        productSpecService.updateById(productSpec);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //删除
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        productSpecService.deleteById(id);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @GetMapping("findAll")
    public Result findAll() {
        List<ProductSpec> list = productSpecService.findAll();
        return Result.build(list , ResultCodeEnum.SUCCESS) ;
    }
}
