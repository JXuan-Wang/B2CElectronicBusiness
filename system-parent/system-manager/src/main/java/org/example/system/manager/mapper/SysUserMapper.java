package org.example.system.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.dto.system.SysUserDto;
import org.example.system.model.entity.system.SysUser;

import java.util.List;

@Mapper
public interface SysUserMapper {

    SysUser selectUserInfoByUserName(String username);

    List<SysUser> findByPage(SysUserDto sysUserDto);

    void save(SysUser sysUser);

    void update(SysUser sysUser);

    void delete(Integer userId);
}
