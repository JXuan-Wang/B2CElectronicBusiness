package org.example.system.user.controller;

import jakarta.annotation.Resource;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.example.system.user.service.SmsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user/sms")
public class SmsController {
    @Resource
    private SmsService smsService;

    @GetMapping(value = "/sendCode/{phone}")
    public Result sendValidateCode(@PathVariable String phone) {
        smsService.sendCode(phone);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
