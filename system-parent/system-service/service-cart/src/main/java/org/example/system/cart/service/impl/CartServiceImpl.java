package org.example.system.cart.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.plugin.auth.constant.Constants;
import jakarta.annotation.Resource;
import org.example.system.cart.service.CartService;
import org.example.system.feign.product.ProductFeignClient;
import org.example.system.model.entity.h5.CartInfo;
import org.example.system.model.entity.product.ProductSku;
import org.example.system.utils.AuthContextUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Resource
    private ProductFeignClient productFeignClient;

    private String getCartKey(Long userId) {
        //定义key user:cart:userId
        return "user:cart:" + userId;
    }

    @Override
    public void addToCart(Long skuId, Integer skuNum) {
        // 必须登录状态，获取当前登录用户id（作为redis的hash类型的key值）
        //从ThreadLocal获取用户信息就可以了
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = getCartKey(userId);

        // 因为购物车放到redis里面

        // hash类型 key：userId field：skuId value：sku信息
        // 从redis里面获取购物车数据，根据key值+skuId获取（hash类型key+field）
        Object cartInfoObj = redisTemplate.opsForHash()
                .get(cartKey, String.valueOf(skuId));

        // 如果购物车存在添加商品，把商品数量相加
        CartInfo cartInfo=null;
        if(cartInfoObj!=null) { //添加到购物车的商品已经存在，把商品数量相加
            //cartInfoObj-->cartInfo
            cartInfo = JSON.parseObject(cartInfoObj.toString(), CartInfo.class);
            //数量相加
            cartInfo.setSkuNum(cartInfo.getSkuNum()+skuNum);
            //设置属性,表示购物车商品选中状态
            cartInfo.setIsChecked(1);
            cartInfo.setUpdateTime(new Date());
        }else{
            // 如果购物车没有添加商品，直接把商品添加购物车（添加到redis里面）
            // 远程调用实现：根据skuId获取商品sku信息
            cartInfo=new CartInfo();

            // 远程调用实现:根据skuId获取商品sku信息
            ProductSku productSku=productFeignClient.getBySkuId(skuId);
            // 设置相关数据到cartInfo中
            cartInfo.setCartPrice(productSku.getSalePrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setSkuId(skuId);
            cartInfo.setUserId(userId);
            cartInfo.setImgUrl(productSku.getThumbImg());
            cartInfo.setSkuName(productSku.getSkuName());
            cartInfo.setIsChecked(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());
        }
        //添加到redis里面
        redisTemplate.opsForHash()
                .put(cartKey,String.valueOf(skuId)
                        ,JSON.toJSONString(cartInfo));
    }

    @Override
    public List<CartInfo> getCartList() {
        // 构建查询的redis里面key值，根据userId
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = getCartKey(userId);

        // 根据key从redis里面hash类型获取所有value值cartInfo
        List<Object> valueList = redisTemplate.opsForHash().values(cartKey);

        // List<Object> -- List<CartInfo>
        if(!CollectionUtil.isEmpty(valueList)){
            List<CartInfo> cartInfoList = valueList.stream().map(cartInfoObj
                            -> JSON.parseObject(cartInfoObj.toString(), CartInfo.class))
                    .collect(Collectors.toList());
            return cartInfoList;
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteCart(Long skuId) {
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = getCartKey(userId);

        redisTemplate.opsForHash().delete(cartKey,String.valueOf(skuId));
    }

    @Override
    public void checkCart(Long skuId, Integer isChecked) {
        // 构建查询的redis里面key值，根据userId
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = getCartKey(userId);

        // 判断key是否包含filed
        Boolean hasKey = redisTemplate.opsForHash().hasKey(cartKey
                , String.valueOf(skuId));
        if(hasKey){
            // 根据key+field把value获取出来
            String cartInfoString = redisTemplate.opsForHash()
                    .get(cartKey, String.valueOf(skuId))
                    .toString();

            // 更新value里面选中状态
            CartInfo cartInfo = JSON.parseObject(cartInfoString, CartInfo.class);
            cartInfo.setIsChecked(isChecked);

            //放回redis的hash类型里面
            redisTemplate.opsForHash().put(cartKey
                    ,String.valueOf(skuId),JSON.toJSONString(cartInfo));
        }
    }

    @Override
    public void allCheckCart(Integer isChecked) {
        // 构建查询的redis里面key值，根据userId
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = getCartKey(userId);

        // 根据key获取购物车所有value值
        List<Object> objectList = redisTemplate.opsForHash().values(cartKey);

        // List<Object> --> List<CartInfo>
        // 判断不为空
        if(!CollectionUtils.isEmpty(objectList)){
            List<CartInfo> cartInfoList = objectList.stream().map(object ->
                            JSON.parseObject(object.toString(), CartInfo.class))
                    .collect(Collectors.toList());

            // 把每个商品的isChecked进行更新
            cartInfoList.forEach(cartInfo -> {
                cartInfo.setIsChecked(isChecked);
                redisTemplate.opsForHash().put(cartKey
                        ,String.valueOf(cartInfo.getSkuId())
                        ,JSON.toJSONString(cartInfo));
            });
        }
    }

    @Override
    public void clearCart() {
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = getCartKey(userId);
        redisTemplate.delete(cartKey);
    }

    @Override
    public List<CartInfo> getAllCkecked() {
        // 获取userId,构建key
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = getCartKey(userId);

        // 根据key获取购物车所有商品
        List<Object> objectList = redisTemplate.opsForHash().values(cartKey);

        if(!CollectionUtils.isEmpty(objectList)){
            List<CartInfo> cartInfoList = objectList.stream().map(object
                            -> JSON.parseObject(object.toString(), CartInfo.class))
                    .filter(cartInfo -> cartInfo.getIsChecked()==1)
                    .collect(Collectors.toList());
            return cartInfoList;
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteChecked() {
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = getCartKey(userId);
        List<Object> objectList = redisTemplate.opsForHash().values(cartKey);       // 删除选中的购物项数据
        if(!CollectionUtils.isEmpty(objectList)) {
            objectList.stream().map(cartInfoJSON -> JSON.parseObject(cartInfoJSON.toString(), CartInfo.class))
                    .filter(cartInfo -> cartInfo.getIsChecked() == 1)
                    .forEach(cartInfo -> redisTemplate.opsForHash().delete(cartKey , String.valueOf(cartInfo.getSkuId())));
        }
    }
}
