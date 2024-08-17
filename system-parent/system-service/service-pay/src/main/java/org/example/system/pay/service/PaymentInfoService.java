package org.example.system.pay.service;

import org.example.system.model.entity.pay.PaymentInfo;

import java.util.Map;

public interface PaymentInfoService {
    PaymentInfo savePaymentInfo(String orderNo);

    void updatePaymentStatus(Map<String, String> paramMap, Integer i);
}