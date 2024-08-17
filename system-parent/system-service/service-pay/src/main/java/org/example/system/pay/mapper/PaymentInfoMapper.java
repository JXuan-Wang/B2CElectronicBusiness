package org.example.system.pay.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.pay.PaymentInfo;

@Mapper
public interface PaymentInfoMapper {
    void save(PaymentInfo paymentInfo);
    PaymentInfo getByOrderNo(String orderNo);

    void updateById(PaymentInfo paymentInfo);
}