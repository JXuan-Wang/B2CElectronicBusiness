package org.example.system.manager.service;

import org.example.system.model.dto.system.AssginMenuDto;

import java.util.Map;

public interface SysRoleMenuService {
    //查询所有菜单 查询角色分配过菜单id列表
    Map<String, Object> findSysRoleMenuByRoleId(Long roleId);

    //为角色分配菜单
    void doAssign(AssginMenuDto assginMenuDto);

}
