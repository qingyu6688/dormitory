package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Menu;
import com.example.entity.RoleMenu;
import com.example.mapper.MenuMapper;
import com.example.mapper.RoleMenuMapper;
import com.example.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<Menu> getTreeList() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Menu::getSortNum);
        return this.list(wrapper);
    }

    @Override
    public List<Menu> getMenusByRoleId(Long roleId) {
        // 查询角色关联的菜单ID
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        List<Long> menuIds = roleMenuMapper.selectList(wrapper)
                .stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toList());

        if (menuIds.isEmpty()) {
            return List.of();
        }

        // 查询菜单信息（只查询菜单类型，type=1）
        LambdaQueryWrapper<Menu> menuWrapper = new LambdaQueryWrapper<>();
        menuWrapper.in(Menu::getId, menuIds);
        menuWrapper.eq(Menu::getType, 1); // 只返回菜单，不返回按钮
        menuWrapper.orderByAsc(Menu::getSortNum);
        List<Menu> allMenus = this.list(menuWrapper);
        
        // 构建树形结构
        return buildMenuTree(allMenus);
    }
    
    /**
     * 构建菜单树
     */
    private List<Menu> buildMenuTree(List<Menu> allMenus) {
        // 找出所有一级菜单（parentId为null）
        List<Menu> rootMenus = allMenus.stream()
                .filter(menu -> menu.getParentId() == null)
                .collect(Collectors.toList());
        
        // 为每个一级菜单设置子菜单
        for (Menu rootMenu : rootMenus) {
            rootMenu.setChildren(getChildMenus(rootMenu.getId(), allMenus));
        }
        
        return rootMenus;
    }
    
    /**
     * 递归获取子菜单
     */
    private List<Menu> getChildMenus(Long parentId, List<Menu> allMenus) {
        List<Menu> children = allMenus.stream()
                .filter(menu -> parentId.equals(menu.getParentId()))
                .collect(Collectors.toList());
        
        // 递归设置子菜单的子菜单
        for (Menu child : children) {
            child.setChildren(getChildMenus(child.getId(), allMenus));
        }
        
        return children;
    }
}
