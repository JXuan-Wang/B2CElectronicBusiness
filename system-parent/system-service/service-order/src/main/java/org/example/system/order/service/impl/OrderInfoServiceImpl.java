package org.example.system.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.system.common.exception.MyException;
import org.example.system.feign.cart.CartFeignClient;
import org.example.system.feign.product.ProductFeignClient;
import org.example.system.feign.user.UserFeignClient;
import org.example.system.model.dto.h5.OrderInfoDto;
import org.example.system.model.entity.h5.CartInfo;
import org.example.system.model.entity.order.OrderInfo;
import org.example.system.model.entity.order.OrderItem;
import org.example.system.model.entity.order.OrderLog;
import org.example.system.model.entity.product.ProductSku;
import org.example.system.model.entity.user.UserAddress;
import org.example.system.model.entity.user.UserInfo;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.example.system.model.vo.h5.TradeVo;
import org.example.system.order.mapper.OrderInfoMapper;
import org.example.system.order.mapper.OrderItemMapper;
import org.example.system.order.mapper.OrderLogMapper;
import org.example.system.order.service.OrderInfoService;
import org.example.system.utils.AuthContextUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {
    @Resource
    private CartFeignClient cartFeignClient;
    @Resource
    private ProductFeignClient productFeignClient;
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    private OrderLogMapper orderLogMapper;
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private OrderItemMapper orderItemMapper;


    @Override
    public TradeVo getTrade() {
        // 远程调用获取购物车选中商品列表
        List<CartInfo> cartInfoList = cartFeignClient.getAllCkecked();

        //创建list集合用于封装订单项
        List<OrderItem> orderItemList=new ArrayList<>();
        for (CartInfo cartInfo : cartInfoList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setSkuId(cartInfo.getSkuId());
            orderItem.setSkuName(cartInfo.getSkuName());
            orderItem.setSkuNum(cartInfo.getSkuNum());
            orderItem.setSkuPrice(cartInfo.getCartPrice());
            orderItem.setThumbImg(cartInfo.getImgUrl());
            orderItemList.add(orderItem);
        }

        // 获取订单支付总金额
        BigDecimal totalAmount=new BigDecimal(0);
        for (OrderItem orderItem : orderItemList) {
            totalAmount=totalAmount.add(orderItem
                    .getSkuPrice()
                    .multiply(new BigDecimal(orderItem
                            .getSkuNum())));
        }

        TradeVo tradeVo=new TradeVo();
        tradeVo.setOrderItemList(orderItemList);
        tradeVo.setTotalAmount(totalAmount);
        return tradeVo;
    }

    @Override
    public Long submitOrder(OrderInfoDto orderInfoDto) {
        // orderInfoDto获取所有订单项list List<OrderItem>
        List<OrderItem> orderItemList = orderInfoDto.getOrderItemList();

        // 判断List<OrderItem>为空，抛出异常
        if(CollectionUtils.isEmpty(orderItemList)){
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }

        // 校验商品库存是否充足
        // 遍历List<OrderItem>集合，得到每个orderItem
        for (OrderItem orderItem : orderItemList) {
            // 根据skuId获取sku信息
            // 远程调用获取商品sku信息，（包含库存量）
            ProductSku productSku = productFeignClient.getBySkuId(orderItem.getSkuId());
            if (productSku == null) {
                throw new MyException(ResultCodeEnum.DATA_ERROR);
            }
            // 校验每个orderItem库存量是否充足
            if (orderItem.getSkuNum() > productSku.getStockNum()) {
                throw new MyException(ResultCodeEnum.STOCK_LESS);
            }
        }

        // 添加数据到order_info表
        // 封装数据到OrderInfo对象
        // 远程调用：获取用户收货地址信息
        OrderInfo orderInfo = new OrderInfo();
        UserInfo userInfo = AuthContextUtil.getUserInfo();
        //订单编号
        orderInfo.setOrderNo(String.valueOf(System.currentTimeMillis()));
        //用户id
        orderInfo.setUserId(userInfo.getId());
        //用户昵称
        orderInfo.setNickName(userInfo.getNickName());

        // 封装收货地址信息
        Long userAddressId = orderInfoDto.getUserAddressId();
        // 远程调用： 根据收货地址id，获取用户地址信息
        UserAddress userAddress = userFeignClient.getUserAddress(orderInfoDto.getUserAddressId());
        orderInfo.setReceiverName(userAddress.getName());
        orderInfo.setReceiverPhone(userAddress.getPhone());
        orderInfo.setReceiverTagName(userAddress.getTagName());
        orderInfo.setReceiverProvince(userAddress.getProvinceCode());
        orderInfo.setReceiverCity(userAddress.getCityCode());
        orderInfo.setReceiverDistrict(userAddress.getDistrictCode());
        orderInfo.setReceiverAddress(userAddress.getFullAddress());
        //订单金额
        BigDecimal totalAmount = new BigDecimal(0);
        for (OrderItem orderItem : orderItemList) {
            totalAmount = totalAmount.add(orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum())));
        }
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setCouponAmount(new BigDecimal(0));
        orderInfo.setOriginalTotalAmount(totalAmount);
        orderInfo.setFeightFee(orderInfoDto.getFeightFee());
        orderInfo.setPayType(2);
        orderInfo.setOrderStatus(0);

        orderInfoMapper.save(orderInfo);

        // 添加数据到order_item表
        // 添加List<OrderItem>里面数据，把集合每个orderItem添加表
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderId(orderInfo.getId());
            orderItemMapper.save(orderItem);
        }

        // 添加数据到order_log表
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderInfo.getId());
        orderLog.setProcessStatus(0);
        orderLog.setNote("提交订单");
        orderLogMapper.save(orderLog);

        // 把生成订单的商品，从购物车删除
        cartFeignClient.deleteChecked();

        // 返回订单id
        return orderInfo.getId();
    }

    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        return orderInfoMapper.getById(orderId);
    }

    @Override
    public TradeVo buy(Long skuId) {
        // 查询商品
        ProductSku productSku = productFeignClient.getBySkuId(skuId);
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setSkuId(skuId);
        orderItem.setSkuName(productSku.getSkuName());
        orderItem.setSkuNum(1);
        orderItem.setSkuPrice(productSku.getSalePrice());
        orderItem.setThumbImg(productSku.getThumbImg());
        orderItemList.add(orderItem);

        // 计算总金额
        BigDecimal totalAmount = productSku.getSalePrice();
        TradeVo tradeVo = new TradeVo();
        tradeVo.setTotalAmount(totalAmount);
        tradeVo.setOrderItemList(orderItemList);

        // 返回
        return tradeVo;
    }

    @Override
    public PageInfo<OrderInfo> findUserPage(Integer page, Integer limit, Integer orderStatus) {
        PageHelper.startPage(page, limit);
        Long userId = AuthContextUtil.getUserInfo().getId();
        List<OrderInfo> orderInfoList = orderInfoMapper.findUserPage(userId, orderStatus);

        orderInfoList.forEach(orderInfo -> {
            List<OrderItem> orderItem = orderItemMapper.findByOrderId(orderInfo.getId());
            orderInfo.setOrderItemList(orderItem);
        });

        return new PageInfo<>(orderInfoList);
    }

    @Override
    public OrderInfo getByOrderNo(String orderNo) {
        OrderInfo orderInfo = orderInfoMapper.getByOrderNo(orderNo);
        List<OrderItem> orderItem = orderItemMapper.findByOrderId(orderInfo.getId());
        orderInfo.setOrderItemList(orderItem);
        return orderInfo;
    }

    @Transactional
    @Override
    public void updateOrderStatus(String orderNo, Integer orderStatus) {

        // 更新订单状态
        OrderInfo orderInfo = orderInfoMapper.getByOrderNo(orderNo);
        orderInfo.setOrderStatus(1);
        orderInfo.setPayType(orderStatus);
        orderInfo.setPaymentTime(new Date());
        orderInfoMapper.updateById(orderInfo);

        // 记录日志
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderInfo.getId());
        orderLog.setProcessStatus(1);
        orderLog.setNote("支付宝支付成功");
        orderLogMapper.save(orderLog);
    }
}
