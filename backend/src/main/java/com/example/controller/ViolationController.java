package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.Log;
import com.example.common.Result;
import com.example.entity.Violation;
import com.example.service.ViolationService;
import com.example.vo.ViolationVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 违纪记录控制器
 */
@Tag(name = "违纪记录", description = "违纪记录增删改查接口")
@RestController
@RequestMapping("/api/violation")
public class ViolationController {

    @Autowired
    private ViolationService violationService;

    @Operation(summary = "分页查询违纪记录")
    @GetMapping("/page")
    public Result<IPage<ViolationVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "学生学号") @RequestParam(required = false) String studentNo,
            @Parameter(description = "房间号") @RequestParam(required = false) String roomNo,
            @Parameter(description = "违纪类型") @RequestParam(required = false) String violationType,
            @Parameter(description = "处理状态") @RequestParam(required = false) Integer handleStatus) {
        
        Page<Violation> page = new Page<>(pageNum, pageSize);
        IPage<ViolationVO> result = violationService.pageWithDetails(page, studentNo, roomNo, violationType, handleStatus);
        return Result.success(result);
    }

    @Operation(summary = "查询所有违纪记录")
    @GetMapping("/list")
    public Result<List<Violation>> list() {
        List<Violation> list = violationService.list();
        return Result.success(list);
    }

    @Operation(summary = "根据ID查询违纪记录")
    @GetMapping("/{id}")
    public Result<Violation> getById(@PathVariable Long id) {
        Violation violation = violationService.getById(id);
        return Result.success(violation);
    }

    @Log("新增违纪记录")
    @Operation(summary = "新增违纪记录")
    @PostMapping
    public Result<Void> save(@RequestBody Violation violation) {
        violationService.save(violation);
        return Result.success();
    }

    @Log("更新违纪记录")
    @Operation(summary = "更新违纪记录")
    @PutMapping
    public Result<Void> update(@RequestBody Violation violation) {
        violationService.updateById(violation);
        return Result.success();
    }

    @Log("删除违纪记录")
    @Operation(summary = "删除违纪记录")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        violationService.removeById(id);
        return Result.success();
    }
}
