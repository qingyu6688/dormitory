package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.Log;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.entity.Menu;
import com.example.exception.ServiceException;
import com.example.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理控制器
 */
@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 分页查询菜单列表
     */
    @GetMapping("/page")
    public Result<IPage<Menu>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer type) {
        
        Page<Menu> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(title)) {
            wrapper.like(Menu::getTitle, title);
        }
        if (type != null) {
            wrapper.eq(Menu::getType, type);
        }
        
        wrapper.orderByAsc(Menu::getSortNum);
        IPage<Menu> result = menuService.page(page, wrapper);
        
        return Result.success(result);
    }

    /**
     * 查询所有菜单（树形结构）
     */
    @GetMapping("/tree")
    public Result<List<Menu>> tree() {
        List<Menu> list = menuService.getTreeList();
        return Result.success(list);
    }

    /**
     * 查询所有菜单（列表）
     */
    @GetMapping("/list")
    public Result<List<Menu>> list() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Menu::getSortNum);
        List<Menu> list = menuService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 根据ID查询菜单
     */
    @GetMapping("/{id}")
    public Result<Menu> getById(@PathVariable Long id) {
        Menu menu = menuService.getById(id);
        if (menu == null) {
            throw new ServiceException(ResultCode.DATA_NOT_EXIST, "菜单不存在");
        }
        return Result.success(menu);
    }

    /**
     * 根据角色ID查询菜单
     */
    @GetMapping("/role/{roleId}")
    public Result<List<Menu>> getByRoleId(@PathVariable Long roleId) {
        List<Menu> list = menuService.getMenusByRoleId(roleId);
        return Result.success(list);
    }

    /**
     * 新增菜单
     */
    @Log("新增菜单")
    @PostMapping
    public Result<Void> add(@RequestBody Menu menu) {
        boolean success = menuService.save(menu);
        return success ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改菜单
     */
    @Log("修改菜单")
    @PutMapping
    public Result<Void> update(@RequestBody Menu menu) {
        if (menu.getId() == null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "菜单ID不能为空");
        }
        
        Menu existMenu = menuService.getById(menu.getId());
        if (existMenu == null) {
            throw new ServiceException(ResultCode.DATA_NOT_EXIST, "菜单不存在");
        }
        
        boolean success = menuService.updateById(menu);
        return success ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除菜单
     */
    @Log("删除菜单")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Menu menu = menuService.getById(id);
        if (menu == null) {
            throw new ServiceException(ResultCode.DATA_NOT_EXIST, "菜单不存在");
        }
        
        // 检查是否有子菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, id);
        long count = menuService.count(wrapper);
        if (count > 0) {
            throw new ServiceException(ResultCode.DATA_IN_USE, "该菜单下存在子菜单，无法删除");
        }
        
        boolean success = menuService.removeById(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    /**
     * 批量删除菜单
     */
    @Log("批量删除菜单")
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "请选择要删除的菜单");
        }
        
        boolean success = menuService.removeByIds(ids);
        return success ? Result.success() : Result.error("批量删除失败");
    }
}
