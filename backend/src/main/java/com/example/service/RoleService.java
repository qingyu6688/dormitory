package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Role;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService extends IService<Role> {
    
    /**
     * 更新角色的菜单权限
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     * @return 是否成功
     */
    boolean updateRoleMenus(Long roleId, List<Long> menuIds);
}
