package org.example.system.manager.service;

import com.github.pagehelper.PageInfo;
import org.example.system.model.dto.system.AssginRoleDto;
import org.example.system.model.dto.system.LoginDto;
import org.example.system.model.dto.system.SysUserDto;
import org.example.system.model.entity.system.SysUser;
import org.example.system.model.vo.system.LoginVo;

public interface SysUserService {
    LoginVo login(LoginDto loginDto);

    SysUser getUserInfo(String token);

    void logout(String token);

    PageInfo<SysUser> findByPage(Integer pageNum, Integer pageSize, SysUserDto sysUserDto);

    void saveSysUser(SysUser sysUser);

    void updateSysUser(SysUser sysUser);

    void deleteById(Integer userId);

    //用户分配角色
    void doAssign(AssginRoleDto assginRoleDto);
}
