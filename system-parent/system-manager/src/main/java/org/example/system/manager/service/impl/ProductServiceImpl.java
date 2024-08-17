package org.example.system.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.system.manager.mapper.ProductDetailsMapper;
import org.example.system.manager.mapper.ProductMapper;
import org.example.system.manager.mapper.ProductSkuMapper;
import org.example.system.manager.service.ProductService;
import org.example.system.model.dto.product.ProductDto;
import org.example.system.model.entity.product.Product;
import org.example.system.model.entity.product.ProductDetails;
import org.example.system.model.entity.product.ProductSku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductMapper productMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    //列表（条件分页查询）
    @Override
    public PageInfo<Product> findByPage(int page, int limit, ProductDto productDto) {
        PageHelper.startPage(page,limit);
        List<Product> list=productMapper.findByPage(productDto);
        return new PageInfo<>(list);
    }

    //添加
    @Override
    public void save(Product product) {
        //保存商品基本信息
        product.setStatus(0);
        product.setAuditStatus(0);
        productMapper.save(product);

        //获取商品sku列表集合，保存sku信息
        List<ProductSku> productSkuList = product.getProductSkuList();
        for (int i = 0; i < productSkuList.size(); i++) {
            ProductSku productSku = productSkuList.get(i);
            //商品编号
            productSku.setSkuCode(product.getId()+"_"+i);
            //商品ID
            productSku.setProductId(product.getId());
            //skuName
            productSku.setSkuName(product.getName()+productSku.getSkuSpec());
            productSku.setSaleNum(0);
            productSku.setStatus(0);
            productSkuMapper.save(productSku);
        }

        //保存商品详情数据
        ProductDetails productDetails=new ProductDetails();
        productDetails.setProductId(product.getId());
        productDetails.setImageUrls(product.getDetailsImageUrls());
        productDetailsMapper.save(productDetails);
    }

    //根据商品id查询商品信息
    @Override
    public Product getById(Long id) {
        //根据id查询商品基本信息
        Product product=productMapper.findProductById(id);

        //根据id查询商品sku信息列表
        List<ProductSku> productSkuList=productSkuMapper.findProductSkuByProductId(id);
        product.setProductSkuList(productSkuList);

        //根据id查找商品详情数据
        ProductDetails productDetails=productDetailsMapper.findProductDetailsById(id);
        String imageUrls = productDetails.getImageUrls();
        product.setDetailsImageUrls(imageUrls);

        return product;
    }

    //保存修改数据
    @Override
    public void update(Product product) {
        //修改product
        productMapper.updateById(product);

        //修改product_sku
        List<ProductSku> productSkuList=product.getProductSkuList();
        productSkuList.forEach(productSku -> {
            productSkuMapper.updateById(productSku);
        });

        //修改product_details
        String detailsImageUrls = product.getDetailsImageUrls();
        ProductDetails productDetails = productDetailsMapper.findProductDetailsById(product.getId());
        productDetails.setImageUrls(detailsImageUrls);
        productDetailsMapper.updateById(productDetails);
    }

    @Override
    public void deleteById(Long id) {
        //根据商品id删除product表
        productMapper.deleteById(id);
        //根据商品id删除product_sku表
        productSkuMapper.deleteByProductId(id);
        //根据商品id删除product_details表
        productDetailsMapper.deleteByProductId(id);
    }

    @Override
    public void updateAuditStatus(Long id, Integer auditStatus) {
        Product product = new Product();
        product.setId(id);
        if(auditStatus == 1) {
            product.setAuditStatus(1);
            product.setAuditMessage("审批通过");
        } else {
            product.setAuditStatus(-1);
            product.setAuditMessage("审批不通过");
        }
        productMapper.updateById(product);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Product product = new Product();
        product.setId(id);
        if(status == 1) {
            product.setStatus(1);
        } else {
            product.setStatus(-1);
        }
        productMapper.updateById(product);
    }
}
