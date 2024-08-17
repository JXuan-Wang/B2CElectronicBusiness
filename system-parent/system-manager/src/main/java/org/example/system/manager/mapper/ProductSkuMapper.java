package org.example.system.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.product.ProductSku;

import java.util.List;

@Mapper
public interface ProductSkuMapper {
    void save(ProductSku productSku);

    //根据id查询商品sku信息列表
    List<ProductSku> findProductSkuByProductId(Long id);

    void updateById(ProductSku productSku);

    void deleteByProductId(Long id);
}
