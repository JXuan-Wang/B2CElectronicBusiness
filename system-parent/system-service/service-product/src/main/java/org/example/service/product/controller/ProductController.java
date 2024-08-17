package org.example.service.product.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.service.product.service.ProductService;
import org.example.system.model.dto.h5.ProductSkuDto;
import org.example.system.model.dto.product.SkuSaleDto;
import org.example.system.model.entity.product.ProductSku;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.example.system.model.vo.h5.ProductItemVo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品列表管理")
@RestController
@RequestMapping(value="/api/product")
public class ProductController {
    @Resource
    private ProductService productService;

    @Operation(summary = "商品详情")
    @GetMapping("item/{skuId}")
    public Result<ProductItemVo> item(@Parameter(name = "skuId", description = "商品skuId", required = true) @PathVariable Long skuId) {
        ProductItemVo productItemVo = productService.item(skuId);
        return Result.build(productItemVo , ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "分页查询")
    @GetMapping(value = "/{page}/{limit}")
    public Result<PageInfo<ProductSku>> findByPage(@Parameter(name = "page", description = "当前页码", required = true) @PathVariable Integer page,
                                                   @Parameter(name = "limit", description = "每页记录数", required = true) @PathVariable Integer limit,
                                                   @Parameter(name = "productSkuDto", description = "搜索条件对象", required = false) ProductSkuDto productSkuDto) {
        PageInfo<ProductSku> pageInfo = productService.findByPage(page, limit, productSkuDto);
        return Result.build(pageInfo , ResultCodeEnum.SUCCESS) ;
    }

    // 远程调用：根据skuId返回sku信息
    @Operation(summary = "获取商品sku信息")
    @GetMapping("getBySkuId/{skuId}")
    public ProductSku getBySkuId(@Parameter(name = "skuId", description = "商品skuId", required = true) @PathVariable Long skuId) {
        ProductSku productSku = productService.getBySkuId(skuId);
        return productSku;
    }

    @Operation(summary = "更新商品sku销量")
    @PostMapping("updateSkuSaleNum")
    public Boolean updateSkuSaleNum(@RequestBody List<SkuSaleDto> skuSaleDtoList) {
        return productService.updateSkuSaleNum(skuSaleDtoList);
    }
}
