package org.example.system.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.order.OrderStatistics;

@Mapper
public interface OrderInfoMapper {
    //统计前一天交易金额
    OrderStatistics selectStatisticsByDate(String createDate);
}
