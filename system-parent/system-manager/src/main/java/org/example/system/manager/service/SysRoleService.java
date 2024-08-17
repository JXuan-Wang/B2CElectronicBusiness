package org.example.system.manager.service;

import com.github.pagehelper.PageInfo;
import org.example.system.model.dto.system.SysRoleDto;
import org.example.system.model.entity.system.SysRole;

import java.util.Map;

public interface SysRoleService {
    PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, int current, int limit);

    //添加角色
    void saveSysRole(SysRole sysRole);

    //修改角色
    void updateSysRole(SysRole sysRole);

    //删除角色
    void deleteById(Long roleId);

    //查询所有角色
    Map<String, Object> findAll(Long userId);
}
