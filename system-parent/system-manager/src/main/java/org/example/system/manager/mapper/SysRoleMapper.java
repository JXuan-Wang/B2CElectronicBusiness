package org.example.system.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.dto.system.SysRoleDto;
import org.example.system.model.entity.system.SysRole;

import java.util.List;

@Mapper
public interface SysRoleMapper {

    List<SysRole> findByPage(SysRoleDto sysRoleDto);

    void save(SysRole sysRole);

    void update(SysRole sysRole);

    void delete(Long roleId);

    List<SysRole> findAll();
}
