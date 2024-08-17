package org.example.service.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.product.ProductDetails;

@Mapper
public interface ProductDetailsMapper {
    ProductDetails getByProductId(Long productId);
}
