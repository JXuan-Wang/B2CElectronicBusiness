package org.example.system.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.order.OrderLog;

@Mapper
public interface OrderLogMapper {
    void save(OrderLog orderLog);
}