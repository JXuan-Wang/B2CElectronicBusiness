package org.example.service.product.service;

import com.github.pagehelper.PageInfo;
import org.example.system.model.dto.h5.ProductSkuDto;
import org.example.system.model.dto.product.SkuSaleDto;
import org.example.system.model.entity.product.ProductSku;
import org.example.system.model.vo.h5.ProductItemVo;

import java.util.List;

public interface ProductService {
    //根据销量进行排序，获取前10条记录
    List<ProductSku> selectProductSkuBySale();

    PageInfo<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto);

    ProductItemVo item(Long skuId);

    ProductSku getBySkuId(Long skuId);

    Boolean updateSkuSaleNum(List<SkuSaleDto> skuSaleDtoList);
}
