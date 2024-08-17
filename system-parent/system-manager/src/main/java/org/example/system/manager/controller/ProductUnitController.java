package org.example.system.manager.controller;

import jakarta.annotation.Resource;
import org.example.system.manager.service.ProductUnitService;
import org.example.system.model.entity.base.ProductUnit;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/product/productUnit")
public class ProductUnitController {
    @Resource
    private ProductUnitService productUnitService;

    @GetMapping("/findAll")
    public Result findAll(){
        List<ProductUnit> list=productUnitService.findAll();
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }
}
