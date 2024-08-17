package org.example.system.pay;

import org.example.system.common.anno.EnableUserLoginAuthInterceptor;
import org.example.system.pay.properties.AlipayProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {
        "org.example.system.feign.order",
        "org.example.system.feign.product"
})
@EnableConfigurationProperties(value = { AlipayProperties.class })
@EnableUserLoginAuthInterceptor
public class PayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class , args) ;
    }

}