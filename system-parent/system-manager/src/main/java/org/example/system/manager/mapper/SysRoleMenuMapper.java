package org.example.system.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.dto.system.AssginMenuDto;

import java.util.List;

@Mapper
public interface SysRoleMenuMapper {
    //查询角色分配过菜单id列表
    List<Long> findSysRoleMenuByRoleId(Long roleId);
    //删除角色之前分配过菜单数据
    void deleteByRoleId(Long roleId);
    //保存分配数据
    void doAssign(AssginMenuDto assginMenuDto);

    //把父菜单ifHalf半开状态1
    void updateSysRoleMenuIsHalf(Long id);
}
