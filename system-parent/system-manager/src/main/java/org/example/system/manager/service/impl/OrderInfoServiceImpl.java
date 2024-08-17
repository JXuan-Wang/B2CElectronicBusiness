package org.example.system.manager.service.impl;

import cn.hutool.core.date.DateUtil;
import jakarta.annotation.Resource;
import org.example.system.manager.mapper.OrderInfoMapper;
import org.example.system.manager.mapper.OrderStatisticsMapper;
import org.example.system.manager.service.OrderInfoService;
import org.example.system.model.dto.order.OrderStatisticsDto;
import org.example.system.model.entity.order.OrderStatistics;
import org.example.system.model.vo.order.OrderStatisticsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private OrderStatisticsMapper orderStatisticsMapper;

    @Override
    public OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto) {
        //根据dto条件查询统计结果数据，返回list集合
        List<OrderStatistics> orderStatisticsList=
                orderStatisticsMapper.selectList(orderStatisticsDto);

        //遍历list集合，得到所有日期，把所有日期封装到list新集合里面
        List<String> dateList = orderStatisticsList.stream()
                .map(orderStatistics ->
                        DateUtil.format(
                                orderStatistics.getOrderDate()
                                , "yyyy-MM-dd HH:mm:ss"))
                .collect(Collectors.toList());

        //遍历list集合，得到所有日期的总金额，把所有日期封装到list新集合里面
        List<BigDecimal> decimalList = orderStatisticsList.stream()
                .map(OrderStatistics::getTotalAmount)
                .collect(Collectors.toList());

        //把两个list集合封装到OrderStatisticsVo并返回
        OrderStatisticsVo orderStatisticsVo = new OrderStatisticsVo();
        orderStatisticsVo.setDateList(dateList);
        orderStatisticsVo.setAmountList(decimalList);

        return orderStatisticsVo;
    }
}
