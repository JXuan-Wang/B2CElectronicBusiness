package org.example.system.order.service;

import com.github.pagehelper.PageInfo;
import org.example.system.model.dto.h5.OrderInfoDto;
import org.example.system.model.entity.order.OrderInfo;
import org.example.system.model.vo.h5.TradeVo;

public interface OrderInfoService {
    TradeVo getTrade();

    Long submitOrder(OrderInfoDto orderInfoDto);

    OrderInfo getOrderInfo(Long orderId);

    TradeVo buy(Long skuId);

    PageInfo<OrderInfo> findUserPage(Integer page, Integer limit, Integer orderStatus);

    OrderInfo getByOrderNo(String orderNo);

    void updateOrderStatus(String orderNo, Integer orderStatus);
}
