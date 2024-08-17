package org.example.service.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.product.Product;
import org.example.system.model.entity.product.ProductSku;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<ProductSku> selectProductSkuBySale();

    Product getById(Long productId);
}
