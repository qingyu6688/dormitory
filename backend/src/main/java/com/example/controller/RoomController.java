package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.Log;
import com.example.common.Result;
import com.example.entity.Room;
import com.example.service.RoomService;
import com.example.vo.MyRoomVO;
import com.example.vo.RoommateVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宿舍管理控制器
 */
@Tag(name = "宿舍管理", description = "宿舍增删改查接口")
@RestController
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Operation(summary = "分页查询宿舍列表")
    @GetMapping("/page")
    public Result<IPage<Room>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "宿舍楼ID") @RequestParam(required = false) Long buildingId,
            @Parameter(description = "房间号") @RequestParam(required = false) String roomNo,
            @Parameter(description = "楼层") @RequestParam(required = false) Integer floor,
            @Parameter(description = "房间状态") @RequestParam(required = false) Integer roomStatus) {
        
        Page<Room> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        
        if (buildingId != null) {
            wrapper.eq(Room::getBuildingId, buildingId);
        }
        if (StringUtils.hasText(roomNo)) {
            wrapper.like(Room::getRoomNo, roomNo);
        }
        if (floor != null) {
            wrapper.eq(Room::getFloor, floor);
        }
        if (roomStatus != null) {
            wrapper.eq(Room::getRoomStatus, roomStatus);
        }
        
        wrapper.orderByAsc(Room::getBuildingId, Room::getFloor, Room::getRoomNo);
        IPage<Room> result = roomService.page(page, wrapper);
        return Result.success(result);
    }

    @Operation(summary = "查询所有宿舍")
    @GetMapping("/list")
    public Result<List<Room>> list(@RequestParam(required = false) Long buildingId) {
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        if (buildingId != null) {
            wrapper.eq(Room::getBuildingId, buildingId);
        }
        List<Room> list = roomService.list(wrapper);
        return Result.success(list);
    }

    @Operation(summary = "根据ID查询宿舍")
    @GetMapping("/{id}")
    public Result<Room> getById(@PathVariable Long id) {
        Room room = roomService.getById(id);
        return Result.success(room);
    }

    @Log("新增宿舍")
    @Operation(summary = "新增宿舍")
    @PostMapping
    public Result<Void> save(@RequestBody Room room) {
        roomService.save(room);
        return Result.success();
    }

    @Log("更新宿舍")
    @Operation(summary = "更新宿舍")
    @PutMapping
    public Result<Void> update(@RequestBody Room room) {
        roomService.updateById(room);
        return Result.success();
    }

    @Log("删除宿舍")
    @Operation(summary = "删除宿舍")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roomService.removeById(id);
        return Result.success();
    }

    @Log("批量删除宿舍")
    @Operation(summary = "批量删除宿舍")
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        roomService.removeByIds(ids);
        return Result.success();
    }

    @Operation(summary = "查询宿舍住宿人员")
    @GetMapping("/{id}/residents")
    public Result<?> getResidents(@Parameter(description = "宿舍ID") @PathVariable Long id) {
        return Result.success(roomService.getRoomResidents(id));
    }

    @Operation(summary = "获取我的宿舍信息")
    @GetMapping("/my-room")
    public Result<MyRoomVO> getMyRoom() {
        return Result.success(roomService.getMyRoom());
    }

    @Operation(summary = "获取室友列表")
    @GetMapping("/{id}/roommates")
    public Result<List<RoommateVO>> getRoommates(@Parameter(description = "宿舍ID") @PathVariable Long id) {
        return Result.success(roomService.getRoommates(id));
    }

    @Operation(summary = "获取可用房间列表")
    @GetMapping("/available/{buildingId}")
    public Result<?> getAvailableRooms(@Parameter(description = "楼栋ID") @PathVariable Long buildingId) {
        return Result.success(roomService.getAvailableRooms(buildingId));
    }

    @Operation(summary = "获取已占用床位")
    @GetMapping("/{id}/occupied-beds")
    public Result<?> getOccupiedBeds(@Parameter(description = "宿舍ID") @PathVariable Long id) {
        return Result.success(roomService.getOccupiedBeds(id));
    }

    @Log("选择宿舍")
    @Operation(summary = "学生选择宿舍")
    @PostMapping("/select")
    public Result<Void> selectRoom(@RequestBody java.util.Map<String, Object> params) {
        Long roomId = Long.valueOf(params.get("roomId").toString());
        Integer bedNo = Integer.valueOf(params.get("bedNo").toString());
        roomService.selectRoom(roomId, bedNo);
        return Result.success();
    }
}
