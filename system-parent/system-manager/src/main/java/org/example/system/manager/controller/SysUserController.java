package org.example.system.manager.controller;

import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.system.manager.service.SysUserService;
import org.example.system.model.dto.system.AssginRoleDto;
import org.example.system.model.dto.system.SysUserDto;
import org.example.system.model.entity.system.SysUser;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    //用户的条件分页查询接口
    @GetMapping("/findByPage/{pageNum}/{pageSize}")
    public Result findByPage(@PathVariable("pageNum") Integer pageNum,
                             @PathVariable("pageSize") Integer pageSize,
                             SysUserDto sysUserDto) {
        PageInfo<SysUser> pageInfo = sysUserService.findByPage(pageNum, pageSize, sysUserDto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    //用户添加
    @PostMapping("/saveSysUser")
    public Result saveSysUser(@RequestBody SysUser sysUser) {
        sysUserService.saveSysUser(sysUser);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //用户修改
    @PutMapping("/updateSysUser")
    public Result updateSysUser(@RequestBody SysUser sysUser) {
        sysUserService.updateSysUser(sysUser);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //用户删除
    @DeleteMapping("/deleteById/{userId}")
    public Result deleteById(@PathVariable("userId") Integer userId) {
        sysUserService.deleteById(userId);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //用户分配角色
    //保存分配数据
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleDto assginRoleDto) {
        sysUserService.doAssign(assginRoleDto);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
