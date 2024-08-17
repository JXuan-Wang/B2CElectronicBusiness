package org.example.service.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.dto.h5.ProductSkuDto;
import org.example.system.model.entity.product.ProductSku;

import java.util.List;

@Mapper
public interface ProductSkuMapper {
    List<ProductSku> findByPage(ProductSkuDto productSkuDto);

    ProductSku getById(Long skuId);

    List<ProductSku> findByProductId(Long productId);

    void updateSale(Long skuId, Integer num);
}
