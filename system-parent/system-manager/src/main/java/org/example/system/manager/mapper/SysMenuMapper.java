package org.example.system.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.system.model.entity.system.SysMenu;

import java.util.List;

@Mapper
public interface SysMenuMapper {
    List<SysMenu> findAll();

    //菜单添加
    void save(SysMenu sysMenu);

    //菜单修改
    void updateById(SysMenu sysMenu);

    //根据当前菜单id，查询是否包含子菜单
    int selectCountById(Long id);

    //菜单删除
    void delete(Long id);

    //根据userId查询可以操作菜单
    List<SysMenu> findMenusByUserId(Long userId);

    //获取当前添加菜单的父菜单
    SysMenu selectParentMenu(Long parentId);
}
