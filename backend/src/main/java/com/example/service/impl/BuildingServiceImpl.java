package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Building;
import com.example.entity.Room;
import com.example.entity.User;
import com.example.mapper.BuildingMapper;
import com.example.service.BuildingService;
import com.example.service.RoomService;
import com.example.service.UserService;
import com.example.vo.BuildingVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 宿舍楼Service实现
 */
@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements BuildingService {

    @Autowired
    private UserService userService;

    @Autowired
    @Lazy
    private RoomService roomService;

    @Override
    public IPage<BuildingVO> pageWithDetails(Page<Building> page, String buildingNo, String buildingName, Integer genderType, Integer status) {
        // 构建查询条件
        LambdaQueryWrapper<Building> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(buildingNo)) {
            wrapper.like(Building::getBuildingNo, buildingNo);
        }
        if (StringUtils.hasText(buildingName)) {
            wrapper.like(Building::getBuildingName, buildingName);
        }
        if (genderType != null) {
            wrapper.eq(Building::getGenderType, genderType);
        }
        if (status != null) {
            wrapper.eq(Building::getStatus, status);
        }
        wrapper.orderByDesc(Building::getCreateTime);

        // 查询分页数据
        IPage<Building> buildingPage = this.page(page, wrapper);

        // 转换为VO对象
        IPage<BuildingVO> voPage = buildingPage.convert(building -> {
            BuildingVO vo = new BuildingVO();
            BeanUtils.copyProperties(building, vo);

            // 查询宿管员信息
            if (building.getManagerId() != null) {
                User manager = userService.getById(building.getManagerId());
                if (manager != null) {
                    vo.setManagerName(manager.getNickname());
                }
            }

            return vo;
        });

        return voPage;
    }

/**
 * 保存楼栋信息并批量生成房间
 * 使用事务注解，确保操作的原子性，发生异常时回滚
 * @param building 楼栋信息，包含楼栋编号、楼层数、每层房间数等信息
 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWithRooms(Building building) {
        // 1. 保存楼栋信息
    // 设置创建时间和更新时间
        building.setCreateTime(LocalDateTime.now());
        building.setUpdateTime(LocalDateTime.now());
    // 调用父类或接口的save方法保存楼栋信息
        this.save(building);

        // 2. 检查是否需要生成房间
    // 检查楼层数和每层房间数是否为空
        if (building.getFloorCount() == null || building.getRoomPerFloor() == null) {
            return; // 如果没有设置楼层数或每层房间数，则不生成房间
        }

    // 检查楼层数和每层房间数是否有效（大于0）
        if (building.getFloorCount() <= 0 || building.getRoomPerFloor() <= 0) {
            return; // 如果数值无效，则不生成房间
        }

        // 3. 批量生成房间
    // 创建房间列表
        List<Room> rooms = new ArrayList<>();

    // 遍历每一层
        for (int floor = 1; floor <= building.getFloorCount(); floor++) {
        // 遍历每一层的每个房间
            for (int roomNum = 1; roomNum <= building.getRoomPerFloor(); roomNum++) {
            // 创建新房间对象
                Room room = new Room();
                
                // 生成三位数房间号：楼层(1位) + 房间号(2位)
                // 例如：3楼5号房间 = 305
                // 注意：楼层最大支持9层，房间号最大支持99
                String roomNo = String.format("%d%02d", floor, roomNum);
            // 设置房间号
                room.setRoomNo(roomNo);
                
            // 设置房间所属楼栋ID
                room.setBuildingId(building.getId());
            // 设置房间所在楼层
                room.setFloor(floor);
            // 设置默认床位数为4
                room.setBedCount(4); // 默认4个床位，可以后续修改
            // 设置初始已入住人数为0
                room.setOccupiedCount(0); // 初始已入住人数为0
            // 设置默认房间类型为普通宿舍
                room.setRoomType("普通宿舍"); // 默认普通宿舍
            // 设置房间状态为空闲
                room.setRoomStatus(1); // 默认空闲状态（1:空闲 2:部分入住 3:已满 4:维修中）
            // 设置创建时间和更新时间
                room.setCreateTime(LocalDateTime.now());
                room.setUpdateTime(LocalDateTime.now());
                
            // 将房间添加到列表中
                rooms.add(room);
            }
        }

        // 4. 批量保存房间
    // 检查房间列表是否为空
        if (!rooms.isEmpty()) {
        // 调用roomService的批量保存方法保存所有房间
            roomService.saveBatch(rooms);
        }
    }
}
