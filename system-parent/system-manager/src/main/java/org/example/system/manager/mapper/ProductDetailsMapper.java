package org.example.system.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.product.ProductDetails;

@Mapper
public interface ProductDetailsMapper {
    void save(ProductDetails productDetails);

    //根据id删除商品详情数据
    ProductDetails findProductDetailsById(Long productId);

    void updateById(ProductDetails productDetails);

    void deleteByProductId(Long id);
}
