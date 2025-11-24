package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.Log;
import com.example.common.Result;
import com.example.entity.CheckIn;
import com.example.service.CheckInService;
import com.example.vo.CheckInVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 入住管理控制器
 */
@Tag(name = "入住管理", description = "入住记录增删改查接口")
@RestController
@RequestMapping("/api/checkin")
public class CheckInController {

    @Autowired
    private CheckInService checkInService;

    @Operation(summary = "分页查询入住记录")
    @GetMapping("/page")
    public Result<IPage<CheckInVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "学生学号") @RequestParam(required = false) String studentNo,
            @Parameter(description = "学生姓名") @RequestParam(required = false) String studentName,
            @Parameter(description = "房间号") @RequestParam(required = false) String roomNo,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        Page<CheckIn> page = new Page<>(pageNum, pageSize);
        IPage<CheckInVO> result = checkInService.pageWithDetails(page, studentNo, studentName, roomNo, status);
        return Result.success(result);
    }

    @Operation(summary = "查询所有入住记录")
    @GetMapping("/list")
    public Result<List<CheckIn>> list() {
        List<CheckIn> list = checkInService.list();
        return Result.success(list);
    }

    @Operation(summary = "根据ID查询入住记录")
    @GetMapping("/{id}")
    public Result<CheckIn> getById(@PathVariable Long id) {
        CheckIn checkIn = checkInService.getById(id);
        return Result.success(checkIn);
    }

    @Log("办理入住")
    @Operation(summary = "办理入住")
    @PostMapping
    public Result<Void> save(@RequestBody CheckIn checkIn) {
        checkInService.save(checkIn);
        return Result.success();
    }

    @Log("更新入住记录")
    @Operation(summary = "更新入住记录")
    @PutMapping
    public Result<Void> update(@RequestBody CheckIn checkIn) {
        checkInService.updateById(checkIn);
        return Result.success();
    }

    @Log("删除入住记录")
    @Operation(summary = "删除入住记录")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        checkInService.removeById(id);
        return Result.success();
    }

    @Log("管理员分配宿舍")
    @Operation(summary = "管理员分配宿舍")
    @PostMapping("/assign")
    public Result<Void> assignRoom(@RequestBody java.util.Map<String, Object> params) {
        Long studentId = Long.valueOf(params.get("studentId").toString());
        Long roomId = Long.valueOf(params.get("roomId").toString());
        Integer bedNo = Integer.valueOf(params.get("bedNo").toString());
        checkInService.assignRoom(studentId, roomId, bedNo);
        return Result.success();
    }
}
