package org.example.system.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.system.manager.mapper.SysRoleMapper;
import org.example.system.manager.mapper.SysRoleUserMapper;
import org.example.system.manager.service.SysRoleService;
import org.example.system.model.dto.system.SysRoleDto;
import org.example.system.model.entity.system.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    //角色列表的方法
    @Override
    public PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, int current, int limit) {
        //设置分页参数
        PageHelper.startPage(current,limit);
        //根据条件查询所有数据
        List<SysRole> list=sysRoleMapper.findByPage(sysRoleDto);
        return new PageInfo<>(list);
    }

    //角色添加的方法
    @Override
    public void saveSysRole(SysRole sysRole) {
        sysRoleMapper.save(sysRole);
    }

    //角色修改的方法
    @Override
    public void updateSysRole(SysRole sysRole) {
        sysRoleMapper.update(sysRole);
    }

    //角色删除的方法
    @Override
    public void deleteById(Long roleId) {
        sysRoleMapper.delete(roleId);
    }

    //查询所有角色
    @Override
    public Map<String, Object> findAll(Long userId) {
        //查询所有角色
        List<SysRole> roleList=sysRoleMapper.findAll();

        //分配过的角色列表
        //根据userId查询用户分配过角色id列表
        List<Long> roleIds=sysRoleUserMapper.selectRoleIdsByUserId(userId);

        Map<String, Object> map=new HashMap<>();
        map.put("allRolesList",roleList);
        map.put("sysUserRoles",roleIds);
        return map;
    }
}
