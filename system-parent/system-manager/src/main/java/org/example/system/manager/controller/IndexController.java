package org.example.system.manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.example.system.manager.service.SysMenuService;
import org.example.system.manager.service.SysUserService;
import org.example.system.manager.service.ValidateCodeService;
import org.example.system.model.dto.system.LoginDto;
import org.example.system.model.entity.system.SysUser;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.example.system.model.vo.system.LoginVo;
import org.example.system.model.vo.system.SysMenuVo;
import org.example.system.model.vo.system.ValidateCodeVo;
import org.example.system.utils.AuthContextUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="用户接口")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private ValidateCodeService validateCodeService;
    @Resource
    private SysMenuService sysMenuService;

    //用户退出
    @GetMapping(value = "/logout")
    public Result logout(@RequestHeader(name="token") String token) {
        sysUserService.logout(token);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //获取当前登录用户信息
    @GetMapping(value = "/getUserInfo")
    public Result getUserInfo(@RequestHeader(name="token")String token){
        //方式一：
        //从请求头获取token
//        String token=request.getHeader("token");
        //根据token查询redis获取用户信息
//        SysUser sysUser=sysUserService.getUserInfo(token);

        //方式二：
        //从ThreadLocal取出线程变量数据
        SysUser sysUser = AuthContextUtil.get();

        //用户信息返回
        return Result.build(sysUser, ResultCodeEnum.SUCCESS);
    }

    //用户登录
    @Operation(summary = "登录的方法")
    @PostMapping("login")
    public Result login(@RequestBody LoginDto loginDto){
        LoginVo loginVo=sysUserService.login(loginDto);
        return Result.build(loginVo, ResultCodeEnum.SUCCESS);
    }

    //生成图片验证码
    @GetMapping(value = "/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode() {
        ValidateCodeVo validateCodeVo=validateCodeService.generateValidateCode();
        return Result.build(validateCodeVo, ResultCodeEnum.SUCCESS);
    }

    //查询用户可以操作的菜单
    @GetMapping("/menus")
    public Result menus() {
        List<SysMenuVo> list=sysMenuService.findMenusByUserId();
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }
}
