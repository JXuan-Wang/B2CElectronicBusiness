package org.example.system.manager.utils;

import org.example.system.model.entity.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

//封装树形菜单数据
public class MenuHelper {
    //递归实现封装过程
    public static List<SysMenu> buildTree(List<SysMenu> sysMenus) {
        //sysMenuList所有菜单集合
        //创建list集合，用于封装最终的数据
        List<SysMenu> trees = new ArrayList<>();
        //遍历所有菜单集合
        for (SysMenu sysMenu : sysMenus) {
            //找到递归操作入口，第一层的菜单
            if (sysMenu.getParentId() == 0) {
                //根据第一层，找下层，使用递归完成
                //写方法实现下层过程，
                //方法里面传递两个参数，第一个参数当前第一层菜单，第二参数是所有菜单集合
                trees.add(findChildren(sysMenu,sysMenus));
            }
        }

        return trees;
    }

    //递归查找下层菜单
    private static SysMenu findChildren(SysMenu sysMenu, List<SysMenu> sysMenus) {
        //SysMenu有属性private List<SysMenu> children；封装下一层数据
        sysMenu.setChildren(new ArrayList<>());
        //递归查询
        //sysMenu的id值和sysMenuList中parentId相同
        for (SysMenu child : sysMenus) {
            if (child.getParentId().longValue() == sysMenu.getId().longValue()) {
                sysMenu.getChildren().add(findChildren(child,sysMenus));
            }
        }
        return sysMenu;
    }
}