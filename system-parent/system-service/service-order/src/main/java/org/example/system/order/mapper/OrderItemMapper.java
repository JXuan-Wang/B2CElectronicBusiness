package org.example.system.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.order.OrderItem;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    void save(OrderItem orderItem);

    List<OrderItem> findByOrderId(Long id);
}
