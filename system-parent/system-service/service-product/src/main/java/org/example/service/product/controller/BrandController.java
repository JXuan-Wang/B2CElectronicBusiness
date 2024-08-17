package org.example.service.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.service.product.service.BrandService;
import org.example.system.model.entity.product.Brand;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "品牌管理")
@RestController
@RequestMapping(value="/api/product/brand")
public class BrandController {
    @Resource
    private BrandService brandService;

    @Operation(summary = "获取全部品牌")
    @GetMapping("findAll")
    public Result<List<Brand>> findAll() {
        List<Brand> list = brandService.findAll();
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }
}
