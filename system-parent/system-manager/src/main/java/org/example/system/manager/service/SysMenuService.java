package org.example.system.manager.service;

import org.example.system.model.entity.system.SysMenu;
import org.example.system.model.vo.system.SysMenuVo;

import java.util.List;

public interface SysMenuService {
    //菜单列表方法
    List<SysMenu> findNodes();

    //菜单添加
    void save(SysMenu sysMenu);

    //菜单修改
    void update(SysMenu sysMenu);

    //菜单删除
    void removeById(Long id);

    //查询用户可以操作的菜单
    List<SysMenuVo> findMenusByUserId();
}
