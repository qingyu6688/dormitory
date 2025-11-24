package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.Log;
import com.example.common.Result;
import com.example.entity.Visitor;
import com.example.service.VisitorService;
import com.example.vo.VisitorVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 访客登记控制器
 */
@Tag(name = "访客登记", description = "访客登记增删改查接口")
@RestController
@RequestMapping("/api/visitor")
public class VisitorController {

    @Autowired
    private VisitorService visitorService;

    @Operation(summary = "分页查询访客记录")
    @GetMapping("/page")
    public Result<IPage<VisitorVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "被访学生ID") @RequestParam(required = false) Long visitedStudentId,
            @Parameter(description = "宿舍楼ID") @RequestParam(required = false) Long buildingId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        Page<Visitor> page = new Page<>(pageNum, pageSize);
        IPage<VisitorVO> result = visitorService.pageWithDetails(page, visitedStudentId, buildingId, status);
        return Result.success(result);
    }

    @Operation(summary = "查询所有访客记录")
    @GetMapping("/list")
    public Result<List<Visitor>> list() {
        List<Visitor> list = visitorService.list();
        return Result.success(list);
    }

    @Operation(summary = "根据ID查询访客记录")
    @GetMapping("/{id}")
    public Result<Visitor> getById(@PathVariable Long id) {
        Visitor visitor = visitorService.getById(id);
        return Result.success(visitor);
    }

    @Log("新增访客登记")
    @Operation(summary = "新增访客登记")
    @PostMapping
    public Result<Void> save(@RequestBody Visitor visitor) {
        visitorService.save(visitor);
        return Result.success();
    }

    @Log("更新访客记录")
    @Operation(summary = "更新访客记录")
    @PutMapping
    public Result<Void> update(@RequestBody Visitor visitor) {
        visitorService.updateById(visitor);
        return Result.success();
    }

    @Log("删除访客记录")
    @Operation(summary = "删除访客记录")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        visitorService.removeById(id);
        return Result.success();
    }

    @Operation(summary = "获取我的访客记录")
    @GetMapping("/my-list")
    public Result<List<VisitorVO>> getMyVisitorList() {
        return Result.success(visitorService.getMyVisitorList());
    }

    @Log("学生登记访客")
    @Operation(summary = "学生登记访客")
    @PostMapping("/register")
    public Result<Void> registerVisitor(@RequestBody Visitor visitor) {
        visitorService.registerVisitor(visitor);
        return Result.success();
    }
}
