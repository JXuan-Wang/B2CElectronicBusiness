package org.example.system.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.system.common.exception.MyException;
import org.example.system.model.entity.pay.PaymentInfo;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.example.system.pay.properties.AlipayProperties;
import org.example.system.pay.service.AlipayService;
import org.example.system.pay.service.PaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
@Slf4j
public class AlipayServiceImpl implements AlipayService {
    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Autowired
    private AlipayProperties alipayProperties ;

    @Override
    public String submitAlipay(String orderNo) {
        // 保存支付记录
        PaymentInfo paymentInfo = paymentInfoService.savePaymentInfo(orderNo);

        // 调用支付宝服务接口
        // 构建需要参数，进行调用
        AlipayTradePayRequest alipayRequest=new AlipayTradePayRequest();
        // 同步回调
        alipayRequest.setReturnUrl(alipayProperties.getReturnPaymentUrl());

        // 异步回调
        alipayRequest.setNotifyUrl(alipayProperties.getNotifyPaymentUrl());
        // 准备请求参数 ，声明一个map 集合
        HashMap<String, Object> map = new HashMap<>();
        map.put("out_trade_no",paymentInfo.getOrderNo());
        map.put("product_code","QUICK_WAP_WAY");
        //map.put("total_amount",paymentInfo.getAmount());
        map.put("total_amount",new BigDecimal("0.01"));
        map.put("subject",paymentInfo.getContent());
        alipayRequest.setBizContent(JSON.toJSONString(map));

        // 发送请求
        try {
            AlipayTradePayResponse response = alipayClient.pageExecute(alipayRequest);
            if (response.isSuccess()) {
                log.info("调用成功");
                return response.getBody();
            } else {
                log.info("调用失败");
                throw new MyException(ResultCodeEnum.DATA_ERROR);
            }
        }catch (AlipayApiException e){
            throw new RuntimeException(e);
        }
    }
}
