package org.example.system.user.service.impl;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.example.system.user.service.SmsService;
import org.example.system.utils.HttpUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService {
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void sendCode(String phone) {
        String code=redisTemplate.opsForValue().get(phone);
        if(StringUtils.hasText(code)){
            return;
        }

        // 生成验证码
        code = RandomStringUtils.randomNumeric(4);

        // 把生成验证码放到redis里面，设置过期时间
        redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

        // 向手机号发送短信验证码
        sendMessage(phone,code);
    }

    private void sendMessage(String phone, String code) {
        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = "999a75796c9b43efa4de35d80d775d42";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        querys.put("param", "**code**:"+code+",**minute**:5");

//smsSignId（短信前缀）和templateId（短信模板），可登录国阳云控制台自助申请。参考文档：http://help.guoyangyun.com/Problem/Qm.html

        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从\r\n\t    \t* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java\r\n\t    \t* 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
//            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
