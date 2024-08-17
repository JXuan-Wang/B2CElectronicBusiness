package org.example.system.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.dto.product.ProductDto;
import org.example.system.model.entity.product.Product;

import java.util.List;

@Mapper
public interface ProductMapper {
    //列表（条件分页查询）
    List<Product> findByPage(ProductDto productDto);

    void save(Product product);

    //根据id查询商品基本信息
    Product findProductById(Long id);

    void updateById(Product product);

    void deleteById(Long id);
}
