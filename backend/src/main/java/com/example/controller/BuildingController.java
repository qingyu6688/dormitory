package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.Log;
import com.example.common.Result;
import com.example.entity.Building;
import com.example.service.BuildingService;
import com.example.vo.BuildingVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宿舍楼管理控制器
 */
@Tag(name = "宿舍楼管理", description = "宿舍楼增删改查接口")
@RestController
@RequestMapping("/api/building")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @Operation(summary = "分页查询宿舍楼列表")
    @GetMapping("/page")
    public Result<IPage<BuildingVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "楼栋编号") @RequestParam(required = false) String buildingNo,
            @Parameter(description = "楼栋名称") @RequestParam(required = false) String buildingName,
            @Parameter(description = "性别限制") @RequestParam(required = false) Integer genderType,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        Page<Building> page = new Page<>(pageNum, pageSize);
        IPage<BuildingVO> result = buildingService.pageWithDetails(page, buildingNo, buildingName, genderType, status);
        return Result.success(result);
    }

    @Operation(summary = "查询所有宿舍楼")
    @GetMapping("/list")
    public Result<List<Building>> list() {
        List<Building> list = buildingService.list();
        return Result.success(list);
    }

    @Operation(summary = "根据ID查询宿舍楼")
    @GetMapping("/{id}")
    public Result<Building> getById(@PathVariable Long id) {
        Building building = buildingService.getById(id);
        return Result.success(building);
    }

    @Log("新增宿舍楼")
    @Operation(summary = "新增宿舍楼并自动生成房间")
    @PostMapping
    public Result<Void> save(@RequestBody Building building) {
        buildingService.saveWithRooms(building);
        return Result.success();
    }

    @Log("更新宿舍楼")
    @Operation(summary = "更新宿舍楼")
    @PutMapping
    public Result<Void> update(@RequestBody Building building) {
        buildingService.updateById(building);
        return Result.success();
    }

    @Log("删除宿舍楼")
    @Operation(summary = "删除宿舍楼")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        buildingService.removeById(id);
        return Result.success();
    }

    @Log("批量删除宿舍楼")
    @Operation(summary = "批量删除宿舍楼")
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        buildingService.removeByIds(ids);
        return Result.success();
    }
}
