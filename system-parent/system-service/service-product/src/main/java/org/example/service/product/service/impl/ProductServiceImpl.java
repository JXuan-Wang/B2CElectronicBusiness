package org.example.service.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.service.product.mapper.ProductDetailsMapper;
import org.example.service.product.mapper.ProductMapper;
import org.example.service.product.mapper.ProductSkuMapper;
import org.example.service.product.service.ProductService;
import org.example.system.model.dto.h5.ProductSkuDto;
import org.example.system.model.dto.product.SkuSaleDto;
import org.example.system.model.entity.product.Product;
import org.example.system.model.entity.product.ProductDetails;
import org.example.system.model.entity.product.ProductSku;
import org.example.system.model.vo.h5.ProductItemVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductMapper productMapper;
    @Resource
    private ProductSkuMapper productSkuMapper;
    @Resource
    private ProductDetailsMapper productDetailsMapper;

    //根据销量进行排序，获取前10条记录
    @Override
    public List<ProductSku> selectProductSkuBySale() {
        return productMapper.selectProductSkuBySale();
    }

    @Override
    public PageInfo<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto) {
        PageHelper.startPage(page, limit);
        List<ProductSku> productSkuList = productSkuMapper.findByPage(productSkuDto);
        return new PageInfo<>(productSkuList);
    }

    @Override
    public ProductItemVo item(Long skuId) {
        // 创建vo对象，用于封装最终数据
        ProductItemVo productItemVo = new ProductItemVo();

        // 根据skuId获取sku信息
        ProductSku productSku=productSkuMapper.getById(skuId);

        // 根据第二部获取sku，从sku获取productId，获取商品信息
        Long productId = productSku.getProductId();
        Product product=productMapper.getById(productId);

        // productId,获取商品详情信息
        ProductDetails productDetails=productDetailsMapper.getByProductId(productId);

        // 封装map集合，商品规格对应商品skuId信息
        Map<String,Object> skuSpecValueMap=new HashMap<>();
        // 根据商品id获取商品所有sku列表
        List<ProductSku> productSkuList=productSkuMapper.findByProductId(productId);
        productSkuList.forEach(item->{
            skuSpecValueMap.put(item.getSkuSpec(),item.getId());
        });

        // 把需要数据封装到productItemVo里面
        productItemVo.setProduct(product);
        productItemVo.setSkuSpecValueMap(skuSpecValueMap);
        productItemVo.setProductSku(productSku);

        //封装详情图片，list集合
        String imageUrls = productDetails.getImageUrls();
        String[] split = imageUrls.split(",");
        List<String> list = Arrays.asList(split);
        productItemVo.setDetailsImageUrlList(list);

        //封装轮播图，list集合
        productItemVo.setSliderUrlList(Arrays
                .asList(product
                        .getSliderUrls()
                        .split(",")));

        productItemVo.setSpecValueList(JSON.parseArray(product.getSpecValue()));

        return productItemVo;
    }

    @Override
    public ProductSku getBySkuId(Long skuId) {
        return productSkuMapper.getById(skuId);
    }

    @Transactional
    @Override
    public Boolean updateSkuSaleNum(List<SkuSaleDto> skuSaleDtoList) {
        if(!CollectionUtils.isEmpty(skuSaleDtoList)) {
            for(SkuSaleDto skuSaleDto : skuSaleDtoList) {
                productSkuMapper.updateSale(skuSaleDto.getSkuId(), skuSaleDto.getNum());
            }
        }
        return true;
    }
}
