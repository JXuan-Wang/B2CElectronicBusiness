package org.example.system.manager.controller;

import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.system.common.log.annotation.Log;
import org.example.system.manager.service.SysRoleService;
import org.example.system.model.dto.system.SysRoleDto;
import org.example.system.model.entity.system.SysRole;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.simpleframework.xml.Path;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    @Resource
    private SysRoleService sysRoleService;

    //角色列表的方法
    //current:代表当前页
    //limit:代表每页记录数
    //sysRoleDto:条件角色名称对象
    @PostMapping("/findByPage/{current}/{limit}")
    public Result findByPage(@PathVariable("current") int current,
                             @PathVariable("limit") int limit,
                             @RequestBody SysRoleDto sysRoleDto) {
        PageInfo<SysRole> pageInfo=sysRoleService.findByPage(sysRoleDto,current,limit);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    //角色添加的方法
    @Log(title = "角色管理：添加", businessType = 1)
    @PostMapping("/saveSysRole")
    public Result saveSysRole(@RequestBody SysRole sysRole){
        sysRoleService.saveSysRole(sysRole);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //角色修改的方法
    @PutMapping("/updateSysRole")
    public Result updateSysRole(@RequestBody SysRole sysRole){
        sysRoleService.updateSysRole(sysRole);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //角色删除的方法
    @DeleteMapping("/deleteById/{roleId}")
    public Result deleteById(@PathVariable("roleId") Long roleId){
        sysRoleService.deleteById(roleId);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //查询所有角色
    @GetMapping("/findAllRoles/{userId}")
    public Result findAllRoles(@PathVariable("userId") Long userId){
        Map<String,Object> map= sysRoleService.findAll(userId);
        return Result.build(map, ResultCodeEnum.SUCCESS);
    }
}
