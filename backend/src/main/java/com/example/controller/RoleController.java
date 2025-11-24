package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.Log;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.entity.Role;
import com.example.entity.Menu;
import com.example.exception.ServiceException;
import com.example.service.RoleService;
import com.example.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private MenuService menuService;

    /**
     * 分页查询角色列表
     */
    @GetMapping("/page")
    public Result<IPage<Role>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code) {
        
        Page<Role> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(name)) {
            wrapper.like(Role::getName, name);
        }
        if (StringUtils.hasText(code)) {
            wrapper.like(Role::getCode, code);
        }
        
        wrapper.orderByAsc(Role::getId);
        IPage<Role> result = roleService.page(page, wrapper);
        
        return Result.success(result);
    }

    /**
     * 查询所有角色
     */
    @GetMapping("/list")
    public Result<List<Role>> list() {
        List<Role> list = roleService.list();
        return Result.success(list);
    }

    /**
     * 根据ID查询角色
     */
    @GetMapping("/{id}")
    public Result<Role> getById(@PathVariable Long id) {
        Role role = roleService.getById(id);
        if (role == null) {
            throw new ServiceException(ResultCode.ROLE_NOT_EXIST);
        }
        return Result.success(role);
    }

    /**
     * 新增角色
     */
    @Log("新增角色")
    @PostMapping
    public Result<Void> add(@RequestBody Role role) {
        // 检查角色编码是否已存在
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getCode, role.getCode());
        Role existRole = roleService.getOne(wrapper);
        
        if (existRole != null) {
            throw new ServiceException(ResultCode.ROLE_ALREADY_EXIST);
        }
        
        boolean success = roleService.save(role);
        return success ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改角色
     */
    @Log("修改角色")
    @PutMapping
    public Result<Void> update(@RequestBody Role role) {
        if (role.getId() == null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "角色ID不能为空");
        }
        
        Role existRole = roleService.getById(role.getId());
        if (existRole == null) {
            throw new ServiceException(ResultCode.ROLE_NOT_EXIST);
        }
        
        // 如果修改了角色编码，检查新编码是否已存在
        if (!existRole.getCode().equals(role.getCode())) {
            LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Role::getCode, role.getCode());
            Role checkRole = roleService.getOne(wrapper);
            
            if (checkRole != null) {
                throw new ServiceException(ResultCode.ROLE_ALREADY_EXIST);
            }
        }
        
        boolean success = roleService.updateById(role);
        return success ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除角色
     */
    @Log("删除角色")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Role role = roleService.getById(id);
        if (role == null) {
            throw new ServiceException(ResultCode.ROLE_NOT_EXIST);
        }
        
        boolean success = roleService.removeById(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    /**
     * 批量删除角色
     */
    @Log("批量删除角色")
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "请选择要删除的角色");
        }
        
        boolean success = roleService.removeByIds(ids);
        return success ? Result.success() : Result.error("批量删除失败");
    }
    
    /**
     * 获取角色的菜单权限
     */
    @GetMapping("/{id}/menus")
    public Result<List<Menu>> getRoleMenus(@PathVariable Long id) {
        Role role = roleService.getById(id);
        if (role == null) {
            throw new ServiceException(ResultCode.ROLE_NOT_EXIST);
        }
        
        List<Menu> menus = menuService.getMenusByRoleId(id);
        return Result.success(menus);
    }
    
    /**
     * 更新角色的菜单权限
     */
    @Log("分配角色权限")
    @PutMapping("/{id}/menus")
    public Result<Void> updateRoleMenus(@PathVariable Long id, @RequestBody List<Long> menuIds) {
        Role role = roleService.getById(id);
        if (role == null) {
            throw new ServiceException(ResultCode.ROLE_NOT_EXIST);
        }
        
        boolean success = roleService.updateRoleMenus(id, menuIds);
        return success ? Result.success() : Result.error("权限分配失败");
    }
}
