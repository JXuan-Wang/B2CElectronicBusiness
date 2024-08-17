package org.example.system.manager.controller;

import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.system.manager.service.ProductService;
import org.example.system.model.dto.product.ProductDto;
import org.example.system.model.entity.product.Product;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/product/product")
public class ProductController {
    @Resource
    private ProductService productService;

    //列表（条件分页查询）
    @GetMapping("/{page}/{limit}")
    public Result list(@PathVariable("page") int page,
                       @PathVariable("limit") int limit,
                       ProductDto productDto) {
        PageInfo<Product> pageInfo=productService.findByPage(page,limit,productDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    //添加
    @PostMapping("/save")
    public Result save(@RequestBody Product product) {
        productService.save(product);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //根据商品id查询商品信息
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable("id") Long id) {
        Product product=productService.getById(id);
        return Result.build(product, ResultCodeEnum.SUCCESS);
    }

    //保存修改数据
    @PutMapping("/updateById")
    public Result updateById(@RequestBody Product product) {
        productService.update(product);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //删除商品
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        productService.deleteById(id);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    //审核
    @GetMapping("/updateAuditStatus/{id}/{auditStatus}")
    public Result updateAuditStatus(@PathVariable Long id, @PathVariable Integer auditStatus) {
        productService.updateAuditStatus(id, auditStatus);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    //上下架
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        productService.updateStatus(id, status);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }
}
