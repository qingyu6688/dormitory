package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Menu;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface MenuService extends IService<Menu> {

    /**
     * 获取树形菜单列表
     */
    List<Menu> getTreeList();

    /**
     * 根据角色ID获取菜单列表
     */
    List<Menu> getMenusByRoleId(Long roleId);
}
