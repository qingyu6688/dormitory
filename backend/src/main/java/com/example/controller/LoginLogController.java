package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.LoginLog;
import com.example.service.LoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 登录日志管理控制器
 */
@Tag(name = "登录日志管理", description = "登录日志查询接口")
@RestController
@RequestMapping("/api/loginLog")
public class LoginLogController {

    @Autowired
    private LoginLogService loginLogService;

    /**
     * 分页查询登录日志
     */
    @Operation(summary = "分页查询登录日志", description = "支持用户名、IP地址、登录状态筛选")
    @GetMapping("/page")
    public Result<IPage<LoginLog>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "用户名（模糊查询）") @RequestParam(required = false) String username,
            @Parameter(description = "IP地址") @RequestParam(required = false) String ipAddress,
            @Parameter(description = "登录状态 1:成功 0:失败") @RequestParam(required = false) Integer status) {
        
        Page<LoginLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<LoginLog> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(username)) {
            wrapper.like(LoginLog::getUsername, username);
        }
        if (StringUtils.hasText(ipAddress)) {
            wrapper.like(LoginLog::getIpAddress, ipAddress);
        }
        if (status != null) {
            wrapper.eq(LoginLog::getStatus, status);
        }
        
        wrapper.orderByDesc(LoginLog::getLoginTime);
        IPage<LoginLog> result = loginLogService.page(page, wrapper);
        
        return Result.success(result);
    }

    /**
     * 查询所有登录日志
     */
    @Operation(summary = "查询所有登录日志")
    @GetMapping("/list")
    public Result<List<LoginLog>> list() {
        LambdaQueryWrapper<LoginLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(LoginLog::getLoginTime);
        List<LoginLog> list = loginLogService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 根据ID查询登录日志
     */
    @Operation(summary = "根据ID查询登录日志")
    @GetMapping("/{id}")
    public Result<LoginLog> getById(@PathVariable Long id) {
        LoginLog loginLog = loginLogService.getById(id);
        return Result.success(loginLog);
    }

    /**
     * 删除登录日志
     */
    @Operation(summary = "删除登录日志")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = loginLogService.removeById(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    /**
     * 批量删除登录日志
     */
    @Operation(summary = "批量删除登录日志")
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        boolean success = loginLogService.removeByIds(ids);
        return success ? Result.success() : Result.error("批量删除失败");
    }

    /**
     * 清空登录日志
     */
    @Operation(summary = "清空登录日志")
    @DeleteMapping("/clear")
    public Result<Void> clear() {
        boolean success = loginLogService.remove(new LambdaQueryWrapper<>());
        return success ? Result.success() : Result.error("清空失败");
    }
}
