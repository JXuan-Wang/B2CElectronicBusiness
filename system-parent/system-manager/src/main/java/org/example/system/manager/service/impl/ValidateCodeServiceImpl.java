package org.example.system.manager.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import jakarta.annotation.Resource;
import org.example.system.manager.finalArgs.RedisKeyPrefix;
import org.example.system.manager.service.ValidateCodeService;
import org.example.system.model.vo.system.ValidateCodeVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    //生成图片验证码
    @Override
    public ValidateCodeVo generateValidateCode() {
        //通过工具生成图片验证码
        //hutool
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(150, 48, 4, 2);
        String code = circleCaptcha.getCode();//生成4位验证码值
        String imageBase64 = circleCaptcha.getImageBase64();//图片

        //把验证码存储到redis里面，设置redis的key：uuid redis的value：验证码值
        //设置过期时间
        String key= UUID.randomUUID().toString().replaceAll("-","");
        redisTemplate.opsForValue().set(RedisKeyPrefix.USER_VALIDATE+key
                ,code,5, TimeUnit.MINUTES);

        //返回ValidateCodeVo对象
        ValidateCodeVo validateCodeVo=new ValidateCodeVo();
        validateCodeVo.setCodeKey(key);
        validateCodeVo.setCodeValue("data:image/png;base64," + imageBase64);

        return validateCodeVo;
    }
}
