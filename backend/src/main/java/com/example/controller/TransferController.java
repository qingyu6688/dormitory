package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.Log;
import com.example.common.Result;
import com.example.entity.Transfer;
import com.example.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调宿申请控制器
 */
@Tag(name = "调宿申请", description = "调宿申请增删改查接口")
@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Operation(summary = "分页查询调宿申请")
    @GetMapping("/page")
    public Result<IPage<Transfer>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "学生ID") @RequestParam(required = false) Long studentId,
            @Parameter(description = "审批状态") @RequestParam(required = false) Integer approveStatus) {
        
        Page<Transfer> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Transfer> wrapper = new LambdaQueryWrapper<>();
        
        if (studentId != null) {
            wrapper.eq(Transfer::getStudentId, studentId);
        }
        if (approveStatus != null) {
            wrapper.eq(Transfer::getApproveStatus, approveStatus);
        }
        
        wrapper.orderByDesc(Transfer::getApplyTime);
        IPage<Transfer> result = transferService.page(page, wrapper);
        return Result.success(result);
    }

    @Operation(summary = "查询所有调宿申请")
    @GetMapping("/list")
    public Result<List<Transfer>> list() {
        List<Transfer> list = transferService.list();
        return Result.success(list);
    }

    @Operation(summary = "根据ID查询调宿申请")
    @GetMapping("/{id}")
    public Result<Transfer> getById(@PathVariable Long id) {
        Transfer transfer = transferService.getById(id);
        return Result.success(transfer);
    }

    @Log("提交调宿申请")
    @Operation(summary = "提交调宿申请")
    @PostMapping
    public Result<Void> save(@RequestBody Transfer transfer) {
        transferService.save(transfer);
        return Result.success();
    }

    @Log("审批调宿申请")
    @Operation(summary = "审批调宿申请")
    @PutMapping("/approve")
    public Result<Void> approve(@RequestBody Transfer transfer) {
        transferService.updateById(transfer);
        return Result.success();
    }

    @Log("更新调宿申请")
    @Operation(summary = "更新调宿申请")
    @PutMapping
    public Result<Void> update(@RequestBody Transfer transfer) {
        transferService.updateById(transfer);
        return Result.success();
    }

    @Log("删除调宿申请")
    @Operation(summary = "删除调宿申请")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        transferService.removeById(id);
        return Result.success();
    }
}
