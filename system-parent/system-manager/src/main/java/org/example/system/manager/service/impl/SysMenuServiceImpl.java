package org.example.system.manager.service.impl;

import jakarta.annotation.Resource;
import org.example.system.common.exception.MyException;
import org.example.system.manager.mapper.SysMenuMapper;
import org.example.system.manager.mapper.SysRoleMenuMapper;
import org.example.system.manager.service.SysMenuService;
import org.example.system.manager.utils.MenuHelper;
import org.example.system.model.entity.system.SysMenu;
import org.example.system.model.entity.system.SysUser;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.example.system.model.vo.system.SysMenuVo;
import org.example.system.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.LinkedList;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Resource
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    //菜单列表方法
    @Override
    public List<SysMenu> findNodes() {
        //查询所有菜单，返回list集合
        List<SysMenu> sysMenuList=sysMenuMapper.findAll();
        if(CollectionUtils.isEmpty(sysMenuList)){
            return null;
        }

        //调用工具类方法，把返回list集合封装要求数据格式
        List<SysMenu> treeList=MenuHelper.buildTree(sysMenuList);
        return List.of();
    }

    //菜单添加
    @Override
    public void save(SysMenu sysMenu) {
        sysMenuMapper.save(sysMenu);

        //新添加子菜单，把父菜单ifHalf半开状态1
        updateSysRoleMenu(sysMenu);
    }
    //新添加子菜单，把父菜单ifHalf半开状态1
    private void updateSysRoleMenu(SysMenu sysMenu) {
        //获取当前添加菜单的父菜单
        SysMenu parentMenu=sysMenuMapper.selectParentMenu(sysMenu.getParentId());

        if(parentMenu!=null){
            //把父菜单ifHalf半开状态1
            sysRoleMenuMapper.updateSysRoleMenuIsHalf(parentMenu.getId());
            //递归调用
            updateSysRoleMenu(parentMenu);
        }
    }

    //菜单修改
    @Override
    public void update(SysMenu sysMenu) {
        sysMenuMapper.updateById(sysMenu);
    }

    //菜单删除
    @Override
    public void removeById(Long id) {
        //根据当前菜单id，查询是否包含子菜单
        int count=sysMenuMapper.selectCountById(id);

        //判断，count大于0，包含子菜单
        if(count>0){
            throw new MyException(ResultCodeEnum.NODE_ERROR);
        }

        //count等于0，直接删除
        sysMenuMapper.delete(id);
    }

    //查询用户可以操作的菜单
    @Override
    public List<SysMenuVo> findMenusByUserId() {
        //获取当前用户id
        SysUser sysUser= AuthContextUtil.get();
        Long userId=sysUser.getId();

        //根据userId查询可以操作菜单
        List<SysMenu> sysMenuList=sysMenuMapper.findMenusByUserId(userId);

        //封装要求数据格式，返回
        List<SysMenu> sysMenuList1 = MenuHelper.buildTree(sysMenuList);

        return this.buildMenus(sysMenuList1);
    }

    // 将List<SysMenu>对象转换成List<SysMenuVo>对象
    private List<SysMenuVo> buildMenus(List<SysMenu> menus) {

        List<SysMenuVo> sysMenuVoList = new LinkedList<SysMenuVo>();
        for (SysMenu sysMenu : menus) {
            SysMenuVo sysMenuVo = new SysMenuVo();
            sysMenuVo.setTitle(sysMenu.getTitle());
            sysMenuVo.setName(sysMenu.getComponent());
            List<SysMenu> children = sysMenu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sysMenuVo.setChildren(buildMenus(children));
            }
            sysMenuVoList.add(sysMenuVo);
        }
        return sysMenuVoList;
    }
}
