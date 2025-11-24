package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.Log;
import com.example.common.Result;
import com.example.entity.Repair;
import com.example.service.RepairService;
import com.example.vo.RepairVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 报修管理控制器
 */
@Tag(name = "报修管理", description = "报修记录增删改查接口")
@RestController
@RequestMapping("/api/repair")
public class RepairController {

    @Autowired
    private RepairService repairService;

    @Operation(summary = "分页查询报修记录")
    @GetMapping("/page")
    public Result<IPage<RepairVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "报修人学号") @RequestParam(required = false) String reporterNo,
            @Parameter(description = "报修人姓名") @RequestParam(required = false) String reporterName,
            @Parameter(description = "房间号") @RequestParam(required = false) String roomNo,
            @Parameter(description = "报修类型") @RequestParam(required = false) String repairType,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        Page<Repair> page = new Page<>(pageNum, pageSize);
        IPage<RepairVO> result = repairService.pageWithDetails(page, reporterNo, reporterName, roomNo, repairType, status);
        return Result.success(result);
    }

    @Operation(summary = "获取我的报修列表")
    @GetMapping(value = "/my-list", produces = "application/json")
    public Result<List<RepairVO>> getMyRepairList() {
        return Result.success(repairService.getMyRepairList());
    }

    @Operation(summary = "获取报修类型列表")
    @GetMapping(value = "/types", produces = "application/json")
    public Result<List<String>> getRepairTypes() {
        return Result.success(repairService.getRepairTypes());
    }

    @Operation(summary = "查询所有报修记录")
    @GetMapping("/list")
    public Result<List<Repair>> list() {
        List<Repair> list = repairService.list();
        return Result.success(list);
    }

    @Operation(summary = "根据ID查询报修记录详情")
    @GetMapping("/detail/{id}")
    public Result<RepairVO> getById(@PathVariable Long id) {
        RepairVO repair = repairService.getRepairDetail(id);
        return Result.success(repair);
    }

    @Log("提交报修")
    @Operation(summary = "提交报修")
    @PostMapping
    public Result<Void> save(@RequestBody Repair repair) {
        repairService.save(repair);
        return Result.success();
    }

    @Log("更新报修记录")
    @Operation(summary = "更新报修记录")
    @PutMapping
    public Result<Void> update(@RequestBody Repair repair) {
        repairService.updateById(repair);
        return Result.success();
    }

    @Log("学生提交报修")
    @Operation(summary = "学生提交报修")
    @PostMapping("/submit")
    public Result<Void> submitRepair(@RequestBody Repair repair) {
        repairService.submitRepair(repair);
        return Result.success();
    }

    @Log("删除报修记录")
    @Operation(summary = "删除报修记录")
    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        repairService.removeById(id);
        return Result.success();
    }
}
