package org.example.system.manager.controller;

import jakarta.annotation.Resource;
import org.example.system.manager.service.SysMenuService;
import org.example.system.model.entity.system.SysMenu;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {
    @Resource
    private SysMenuService sysMenuService;

    //菜单删除
    @DeleteMapping("/removeById/{id}")
    public Result removeById(@PathVariable("id") Long id) {
        sysMenuService.removeById(id);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //菜单修改
    @PutMapping("/update")
    public Result update(@RequestBody SysMenu sysMenu) {
        sysMenuService.update(sysMenu);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //菜单添加
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //菜单列表方法
    @GetMapping("/findNodes")
    public Result findNodes() {
        List<SysMenu> list=sysMenuService.findNodes();
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
