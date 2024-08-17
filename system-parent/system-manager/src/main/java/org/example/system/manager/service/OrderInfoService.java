package org.example.system.manager.service;

import org.example.system.model.dto.order.OrderStatisticsDto;
import org.example.system.model.vo.order.OrderStatisticsVo;

public interface OrderInfoService {
    OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto);
}
