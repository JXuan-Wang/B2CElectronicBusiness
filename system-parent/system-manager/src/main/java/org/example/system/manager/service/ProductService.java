package org.example.system.manager.service;

import com.github.pagehelper.PageInfo;
import org.example.system.model.dto.product.ProductDto;
import org.example.system.model.entity.product.Product;

public interface ProductService {
    //列表（条件分页查询）
    PageInfo<Product> findByPage(int page, int limit, ProductDto productDto);

    //添加
    void save(Product product);

    //根据商品id查询商品信息
    Product getById(Long id);

    //保存修改数据
    void update(Product product);

    void deleteById(Long id);

    void updateAuditStatus(Long id, Integer auditStatus);

    void updateStatus(Long id, Integer status);
}
