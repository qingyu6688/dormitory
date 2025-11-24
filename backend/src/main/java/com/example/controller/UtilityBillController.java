package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.Log;
import com.example.common.Result;
import com.example.entity.UtilityBill;
import com.example.service.UtilityBillService;
import com.example.vo.UtilityBillVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 水电费管理控制器
 */
@Tag(name = "水电费管理", description = "水电费增删改查接口")
@RestController
@RequestMapping("/api/utilitybill")
public class UtilityBillController {

    @Autowired
    private UtilityBillService utilityBillService;

    @Operation(summary = "分页查询水电费记录")
    @GetMapping("/page")
    public Result<IPage<UtilityBillVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "房间号") @RequestParam(required = false) String roomNo,
            @Parameter(description = "账单月份") @RequestParam(required = false) String billMonth,
            @Parameter(description = "缴费状态") @RequestParam(required = false) Integer paymentStatus) {
        
        Page<UtilityBill> page = new Page<>(pageNum, pageSize);
        IPage<UtilityBillVO> result = utilityBillService.pageWithDetails(page, roomNo, billMonth, paymentStatus);
        return Result.success(result);
    }

    @Operation(summary = "查询所有水电费记录")
    @GetMapping("/list")
    public Result<List<UtilityBill>> list() {
        List<UtilityBill> list = utilityBillService.list();
        return Result.success(list);
    }

    @Operation(summary = "根据ID查询水电费记录")
    @GetMapping("/{id}")
    public Result<UtilityBill> getById(@PathVariable Long id) {
        UtilityBill utilityBill = utilityBillService.getById(id);
        return Result.success(utilityBill);
    }

    @Log("录入水电费")
    @Operation(summary = "录入水电费")
    @PostMapping
    public Result<Void> save(@RequestBody UtilityBill utilityBill) {
        utilityBillService.save(utilityBill);
        return Result.success();
    }

    @Log("更新水电费记录")
    @Operation(summary = "更新水电费记录")
    @PutMapping
    public Result<Void> update(@RequestBody UtilityBill utilityBill) {
        utilityBillService.updateById(utilityBill);
        return Result.success();
    }

    @Log("删除水电费记录")
    @Operation(summary = "删除水电费记录")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        utilityBillService.removeById(id);
        return Result.success();
    }

    @Operation(summary = "获取我的水电费账单")
    @GetMapping("/my-bills")
    public Result<List<UtilityBillVO>> getMyBills() {
        return Result.success(utilityBillService.getMyBills());
    }

    @Log("学生缴费")
    @Operation(summary = "学生缴费")
    @PostMapping("/pay/{id}")
    public Result<Void> payBill(@PathVariable Long id, @RequestParam String paymentMethod) {
        utilityBillService.payBill(id, paymentMethod);
        return Result.success();
    }
}
