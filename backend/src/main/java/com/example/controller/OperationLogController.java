package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.OperationLog;
import com.example.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志管理控制器
 */
@Tag(name = "操作日志管理", description = "操作日志查询接口")
@RestController
@RequestMapping("/api/operationLog")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 分页查询操作日志
     */
    @Operation(summary = "分页查询操作日志", description = "支持用户名、操作类型、操作状态筛选")
    @GetMapping("/page")
    public Result<IPage<OperationLog>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "用户名（模糊查询）") @RequestParam(required = false) String username,
            @Parameter(description = "操作类型") @RequestParam(required = false) String operation,
            @Parameter(description = "操作状态 1:成功 0:失败") @RequestParam(required = false) Integer status) {
        
        Page<OperationLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(username)) {
            wrapper.like(OperationLog::getUsername, username);
        }
        if (StringUtils.hasText(operation)) {
            wrapper.eq(OperationLog::getOperation, operation);
        }
        if (status != null) {
            wrapper.eq(OperationLog::getStatus, status);
        }
        
        wrapper.orderByDesc(OperationLog::getCreateTime);
        IPage<OperationLog> result = operationLogService.page(page, wrapper);
        
        return Result.success(result);
    }

    /**
     * 查询所有操作日志
     */
    @Operation(summary = "查询所有操作日志")
    @GetMapping("/list")
    public Result<List<OperationLog>> list() {
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(OperationLog::getCreateTime);
        List<OperationLog> list = operationLogService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 根据ID查询操作日志
     */
    @Operation(summary = "根据ID查询操作日志")
    @GetMapping("/{id}")
    public Result<OperationLog> getById(@PathVariable Long id) {
        OperationLog operationLog = operationLogService.getById(id);
        return Result.success(operationLog);
    }

    /**
     * 删除操作日志
     */
    @Operation(summary = "删除操作日志")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = operationLogService.removeById(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    /**
     * 批量删除操作日志
     */
    @Operation(summary = "批量删除操作日志")
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        boolean success = operationLogService.removeByIds(ids);
        return success ? Result.success() : Result.error("批量删除失败");
    }

    /**
     * 清空操作日志
     */
    @Operation(summary = "清空操作日志")
    @DeleteMapping("/clear")
    public Result<Void> clear() {
        boolean success = operationLogService.remove(new LambdaQueryWrapper<>());
        return success ? Result.success() : Result.error("清空失败");
    }
}
