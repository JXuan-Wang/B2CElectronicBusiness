package org.example.system.manager.controller;

import jakarta.annotation.Resource;
import org.example.system.manager.service.OrderInfoService;
import org.example.system.model.dto.order.OrderStatisticsDto;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.example.system.model.vo.order.OrderStatisticsVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/order/orderInfo")
public class OrderInfoController {
    @Resource
    private OrderInfoService orderInfoService;

    @GetMapping("/getOrderStatisticsData")
    public Result getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto) {
        OrderStatisticsVo orderStatisticsVo = orderInfoService.getOrderStatisticsData(orderStatisticsDto) ;
        return Result.build(orderStatisticsVo , ResultCodeEnum.SUCCESS) ;
    }

}
