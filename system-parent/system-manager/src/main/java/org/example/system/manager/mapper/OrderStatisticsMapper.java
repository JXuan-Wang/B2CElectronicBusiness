package org.example.system.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.dto.order.OrderStatisticsDto;
import org.example.system.model.entity.order.OrderStatistics;

import java.util.List;

@Mapper
public interface OrderStatisticsMapper {
    void insert(OrderStatistics orderStatistics);

    List<OrderStatistics> selectList(OrderStatisticsDto orderStatisticsDto);
}
