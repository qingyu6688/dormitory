package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.entity.Building;
import com.example.entity.CheckIn;
import com.example.entity.Room;
import com.example.entity.User;
import com.example.exception.ServiceException;
import com.example.mapper.RoomMapper;
import com.example.service.BuildingService;
import com.example.service.CheckInService;
import com.example.service.RoomService;
import com.example.service.UserService;
import com.example.util.JwtUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.example.vo.MyRoomVO;
import com.example.vo.RoommateVO;
import com.example.vo.RoomResidentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 宿舍Service实现
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    @Autowired
    @Lazy
    private CheckInService checkInService;

    @Autowired
    private UserService userService;

    @Autowired
    private BuildingService buildingService;

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        
        String token = attributes.getRequest().getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return null;
        }
        
        // 移除Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        return JwtUtil.getUserId(token);
    }

    @Override
    public List<RoomResidentVO> getRoomResidents(Long roomId) {
        List<RoomResidentVO> residents = new ArrayList<>();

        // 查询该宿舍的所有已入住记录
        LambdaQueryWrapper<CheckIn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckIn::getRoomId, roomId)
               .eq(CheckIn::getStatus, 1) // 1:已入住
               .orderByAsc(CheckIn::getBedNo);

        List<CheckIn> checkInList = checkInService.list(wrapper);

        // 组装住宿人员信息
        for (CheckIn checkIn : checkInList) {
            if (checkIn.getStudentId() != null) {
                User student = userService.getById(checkIn.getStudentId());
                if (student != null) {
                    RoomResidentVO vo = new RoomResidentVO();
                    vo.setCheckInId(checkIn.getId());
                    vo.setStudentId(student.getId());
                    vo.setStudentNo(student.getStudentNo());
                    vo.setStudentName(student.getNickname());
                    vo.setGender(student.getGender());
                    vo.setCollege(student.getCollege());
                    vo.setMajor(student.getMajor());
                    vo.setClassName(student.getClassName());
                    vo.setPhone(student.getPhone());
                    vo.setBedNo(checkIn.getBedNo());
                    vo.setCheckInDate(checkIn.getCheckInDate());
                    vo.setStatus(checkIn.getStatus());
                    residents.add(vo);
                }
            }
        }

        return residents;
    }

    @Override
    public MyRoomVO getMyRoom() {
        // 获取当前登录用户ID
        Long userId = getCurrentUserId();
        System.out.println("=== 获取我的宿舍信息 ===");
        System.out.println("当前用户ID: " + userId);
        
        if (userId == null) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, "未登录");
        }

        // 查询用户的入住记录
        LambdaQueryWrapper<CheckIn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckIn::getStudentId, userId)
               .eq(CheckIn::getStatus, 1) // 1:已入住
               .orderByDesc(CheckIn::getCheckInDate)
               .last("LIMIT 1");

        CheckIn checkIn = checkInService.getOne(wrapper);
        System.out.println("入住记录: " + checkIn);
        
        if (checkIn == null) {
            System.out.println("未找到入住记录，返回null");
            return null; // 未分配宿舍
        }

        // 查询宿舍信息
        Room room = this.getById(checkIn.getRoomId());
        System.out.println("房间信息: " + room);
        
        if (room == null) {
            System.out.println("未找到房间信息");
            return null;
        }

        // 查询楼栋信息
        Building building = buildingService.getById(room.getBuildingId());
        System.out.println("楼栋信息: " + building);
        
        if (building == null) {
            System.out.println("未找到楼栋信息");
            return null;
        }

        // 组装返回数据 - 使用VO对象
        MyRoomVO vo = new MyRoomVO();
        System.out.println("开始组装VO对象");
        vo.setId(room.getId());
        vo.setBuildingId(building.getId());
        vo.setBuildingName(building.getBuildingName());
        vo.setBuildingNo(building.getBuildingNo());
        vo.setRoomNo(room.getRoomNo());
        vo.setFloor(room.getFloor());
        vo.setCapacity(room.getBedCount());
        vo.setCurrentCount(room.getOccupiedCount());
        vo.setBedNo(checkIn.getBedNo());
        vo.setCheckInDate(checkIn.getCheckInDate());
        vo.setRoomType(room.getRoomType());
        vo.setDirection(room.getDirection());
        vo.setRoomStatus(room.getRoomStatus());

        System.out.println("VO对象组装完成: " + vo);
        System.out.println("=== 结束获取宿舍信息 ===");
        
        return vo;
    }

    @Override
    public List<RoommateVO> getRoommates(Long roomId) {
        List<RoommateVO> roommates = new ArrayList<>();

        // 获取当前登录用户ID
        Long currentUserId = getCurrentUserId();
        System.out.println("获取室友列表 - 当前用户ID: " + currentUserId + ", 房间ID: " + roomId);

        // 查询该宿舍的所有已入住记录
        LambdaQueryWrapper<CheckIn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckIn::getRoomId, roomId)
               .eq(CheckIn::getStatus, 1) // 1:已入住
               .orderByAsc(CheckIn::getBedNo);

        List<CheckIn> checkInList = checkInService.list(wrapper);
        System.out.println("查询到 " + checkInList.size() + " 条入住记录");

        // 组装室友信息 - 使用VO对象，排除当前用户
        for (CheckIn checkIn : checkInList) {
            if (checkIn.getStudentId() != null) {
                // 排除当前登录用户
                if (currentUserId != null && checkIn.getStudentId().equals(currentUserId)) {
                    System.out.println("跳过当前用户: " + currentUserId);
                    continue;
                }
                
                User student = userService.getById(checkIn.getStudentId());
                if (student != null) {
                    RoommateVO vo = new RoommateVO();
                    vo.setId(student.getId());
                    vo.setStudentNo(student.getStudentNo());
                    vo.setNickname(student.getNickname());
                    vo.setGender(student.getGender());
                    vo.setCollege(student.getCollege());
                    vo.setMajor(student.getMajor());
                    vo.setClassName(student.getClassName());
                    vo.setPhone(student.getPhone());
                    vo.setAvatar(student.getAvatar());
                    vo.setBedNo(checkIn.getBedNo());
                    roommates.add(vo);
                    System.out.println("添加室友: " + student.getNickname() + " (床位" + checkIn.getBedNo() + ")");
                }
            }
        }

        System.out.println("最终返回 " + roommates.size() + " 个室友");
        return roommates;
    }

    @Override
    public List<Object> getAvailableRooms(Long buildingId) {
        List<Object> availableRooms = new ArrayList<>();

        // 查询该楼栋的所有房间
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Room::getBuildingId, buildingId)
               .ne(Room::getRoomStatus, 4) // 排除维修中的房间
               .orderByAsc(Room::getFloor, Room::getRoomNo);

        List<Room> rooms = this.list(wrapper);

        // 组装可用房间信息
        for (Room room : rooms) {
            Map<String, Object> roomInfo = new HashMap<>();
            roomInfo.put("id", room.getId());
            roomInfo.put("roomNo", room.getRoomNo());
            roomInfo.put("floor", room.getFloor());
            roomInfo.put("capacity", room.getBedCount());
            roomInfo.put("currentCount", room.getOccupiedCount());
            roomInfo.put("roomStatus", room.getRoomStatus());
            availableRooms.add(roomInfo);
        }

        return availableRooms;
    }

    @Override
    public List<Integer> getOccupiedBeds(Long roomId) {
        // 查询该宿舍的所有已入住记录
        LambdaQueryWrapper<CheckIn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckIn::getRoomId, roomId)
               .eq(CheckIn::getStatus, 1); // 1:已入住

        List<CheckIn> checkInList = checkInService.list(wrapper);

        // 提取已占用的床位号
        return checkInList.stream()
                .map(CheckIn::getBedNo)
                .collect(Collectors.toList());
    }

    @Override
    public void selectRoom(Long roomId, Integer bedNo) {
        // 获取当前登录用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, "未登录");
        }

        // 获取用户信息并检查性别
        User user = userService.getById(userId);
        if (user == null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "用户不存在");
        }
        
        // 检查性别是否已填写
        if (user.getGender() == null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "请先完善性别信息后再选择宿舍");
        }
        
        // 检查其他必填信息
        if (user.getStudentNo() == null || user.getStudentNo().isEmpty()) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "请先完善学号信息后再选择宿舍");
        }

        // 检查用户是否已有宿舍
        LambdaQueryWrapper<CheckIn> existWrapper = new LambdaQueryWrapper<>();
        existWrapper.eq(CheckIn::getStudentId, userId)
                   .eq(CheckIn::getStatus, 1);
        CheckIn existCheckIn = checkInService.getOne(existWrapper);
        if (existCheckIn != null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "您已有宿舍，不能重复选择");
        }

        // 检查房间是否存在
        Room room = this.getById(roomId);
        if (room == null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "房间不存在");
        }
        
        // 检查楼栋性别是否匹配
        Building building = buildingService.getById(room.getBuildingId());
        if (building != null && building.getGenderType() != null) {
            if (!building.getGenderType().equals(user.getGender())) {
                String genderText = user.getGender() == 1 ? "男生" : "女生";
                String buildingGenderText = building.getGenderType() == 1 ? "男生" : "女生";
                throw new ServiceException(ResultCode.BAD_REQUEST, 
                    String.format("您是%s，不能选择%s宿舍", genderText, buildingGenderText));
            }
        }

        // 检查房间是否已满
        if (room.getOccupiedCount() >= room.getBedCount()) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "房间已满");
        }

        // 检查床位是否已被占用
        LambdaQueryWrapper<CheckIn> bedWrapper = new LambdaQueryWrapper<>();
        bedWrapper.eq(CheckIn::getRoomId, roomId)
                 .eq(CheckIn::getBedNo, bedNo)
                 .eq(CheckIn::getStatus, 1);
        CheckIn bedCheckIn = checkInService.getOne(bedWrapper);
        if (bedCheckIn != null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "该床位已被占用");
        }

        // 创建入住记录
        CheckIn checkIn = new CheckIn();
        checkIn.setStudentId(userId);
        checkIn.setRoomId(roomId);
        checkIn.setBedNo(bedNo);
        checkIn.setCheckInDate(LocalDate.now());
        checkIn.setStatus(1); // 1:已入住
        checkIn.setDepositStatus(1); // 1:未交
        checkInService.save(checkIn);

        // 更新房间已入住人数
        room.setOccupiedCount(room.getOccupiedCount() + 1);
        
        // 更新房间状态
        if (room.getOccupiedCount() >= room.getBedCount()) {
            room.setRoomStatus(3); // 3:已满
        } else if (room.getOccupiedCount() > 0) {
            room.setRoomStatus(2); // 2:部分入住
        }
        
        this.updateById(room);
    }
}
