package org.example.system.manager.service.impl;

import jakarta.annotation.Resource;
import org.example.system.manager.mapper.SysMenuMapper;
import org.example.system.manager.mapper.SysRoleMenuMapper;
import org.example.system.manager.service.SysRoleMenuService;
import org.example.system.model.dto.system.AssginMenuDto;
import org.example.system.model.entity.system.SysMenu;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleMenuServiceImpl implements SysRoleMenuService {
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Resource
    private SysMenuMapper sysMenuMapper;

    //查询所有菜单 查询角色分配过菜单id列表
    @Override
    public Map<String, Object> findSysRoleMenuByRoleId(Long roleId) {
        //查询所有菜单
        List<SysMenu> sysMenuList = sysMenuMapper.findAll();

        //查询角色分配过菜单id列表
        List<Long> roleMenuIds=sysRoleMenuMapper.findSysRoleMenuByRoleId(roleId);

        Map<String, Object> map=new HashMap<>();
        map.put("sysMenuList",sysMenuList);
        map.put("roleMenuIds",roleMenuIds);

        return map;
    }

    //为角色分配菜单
    @Override
    public void doAssign(AssginMenuDto assginMenuDto) {
        //删除角色之前分配过菜单数据
        sysRoleMenuMapper.deleteByRoleId(assginMenuDto.getRoleId());

        //保存分配数据
        List<Map<String, Number>> menuIdList = assginMenuDto.getMenuIdList();
        if(menuIdList!=null&& !menuIdList.isEmpty()){
            sysRoleMenuMapper.doAssign(assginMenuDto);
        }
    }
}
