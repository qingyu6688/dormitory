package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.CheckIn;
import com.example.entity.Room;
import com.example.entity.User;
import com.example.mapper.CheckInMapper;
import com.example.common.ResultCode;
import com.example.exception.ServiceException;
import com.example.service.CheckInService;
import com.example.service.RoomService;
import com.example.service.UserService;
import com.example.vo.CheckInVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 入住记录Service实现
 */
@Service
public class CheckInServiceImpl extends ServiceImpl<CheckInMapper, CheckIn> implements CheckInService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Override
    public IPage<CheckInVO> pageWithDetails(Page<CheckIn> page, String studentNo, String studentName, String roomNo, Integer status) {
        // 先根据学号或姓名查询学生ID列表
        List<Long> studentIds = null;
        if (StringUtils.hasText(studentNo) || StringUtils.hasText(studentName)) {
            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.hasText(studentNo)) {
                userWrapper.like(User::getStudentNo, studentNo);
            }
            if (StringUtils.hasText(studentName)) {
                userWrapper.like(User::getNickname, studentName);
            }
            List<User> users = userService.list(userWrapper);
            studentIds = users.stream().map(User::getId).collect(Collectors.toList());
            if (studentIds.isEmpty()) {
                // 没有匹配的学生，返回空结果
                return new Page<>(page.getCurrent(), page.getSize(), 0);
            }
        }

        // 根据房间号查询房间ID列表
        List<Long> roomIds = null;
        if (StringUtils.hasText(roomNo)) {
            LambdaQueryWrapper<Room> roomWrapper = new LambdaQueryWrapper<>();
            roomWrapper.like(Room::getRoomNo, roomNo);
            List<Room> rooms = roomService.list(roomWrapper);
            roomIds = rooms.stream().map(Room::getId).collect(Collectors.toList());
            if (roomIds.isEmpty()) {
                // 没有匹配的房间，返回空结果
                return new Page<>(page.getCurrent(), page.getSize(), 0);
            }
        }

        // 构建查询条件
        LambdaQueryWrapper<CheckIn> wrapper = new LambdaQueryWrapper<>();
        if (studentIds != null) {
            wrapper.in(CheckIn::getStudentId, studentIds);
        }
        if (roomIds != null) {
            wrapper.in(CheckIn::getRoomId, roomIds);
        }
        if (status != null) {
            wrapper.eq(CheckIn::getStatus, status);
        }
        wrapper.orderByDesc(CheckIn::getCheckInDate);

        // 查询分页数据
        IPage<CheckIn> checkInPage = this.page(page, wrapper);

        // 转换为VO对象
        IPage<CheckInVO> voPage = checkInPage.convert(checkIn -> {
            CheckInVO vo = new CheckInVO();
            BeanUtils.copyProperties(checkIn, vo);

            // 查询学生信息
            if (checkIn.getStudentId() != null) {
                User student = userService.getById(checkIn.getStudentId());
                if (student != null) {
                    vo.setStudentNo(student.getStudentNo());
                    vo.setStudentName(student.getNickname());
                }
            }

            // 查询房间信息
            if (checkIn.getRoomId() != null) {
                Room room = roomService.getById(checkIn.getRoomId());
                if (room != null) {
                    vo.setRoomNo(room.getRoomNo());
                }
            }

            // 查询办理人信息
            if (checkIn.getOperatorId() != null) {
                User operator = userService.getById(checkIn.getOperatorId());
                if (operator != null) {
                    vo.setOperatorName(operator.getNickname());
                }
            }

            return vo;
        });

        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoom(Long studentId, Long roomId, Integer bedNo) {
        // 1. 验证学生是否存在
        User student = userService.getById(studentId);
        if (student == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }

        // 2. 验证房间是否存在
        Room room = roomService.getById(roomId);
        if (room == null) {
            throw new ServiceException(ResultCode.DATA_NOT_EXIST.getCode(), "房间不存在");
        }

        // 3. 验证床位号是否有效
        if (bedNo == null || bedNo < 1 || bedNo > room.getBedCount()) {
            throw new ServiceException(ResultCode.VALIDATE_FAILED.getCode(), "床位号无效");
        }

        // 4. 检查该学生是否已经有入住记录
        LambdaQueryWrapper<CheckIn> existWrapper = new LambdaQueryWrapper<>();
        existWrapper.eq(CheckIn::getStudentId, studentId)
                   .eq(CheckIn::getStatus, 1);
        CheckIn existCheckIn = this.getOne(existWrapper);
        if (existCheckIn != null) {
            throw new ServiceException(ResultCode.CONFLICT.getCode(), "该学生已分配宿舍，请先退宿");
        }

        // 5. 检查该床位是否已被占用
        LambdaQueryWrapper<CheckIn> bedWrapper = new LambdaQueryWrapper<>();
        bedWrapper.eq(CheckIn::getRoomId, roomId)
                 .eq(CheckIn::getBedNo, bedNo)
                 .eq(CheckIn::getStatus, 1);
        CheckIn bedCheckIn = this.getOne(bedWrapper);
        if (bedCheckIn != null) {
            throw new ServiceException(ResultCode.CONFLICT.getCode(), "该床位已被占用");
        }

        // 6. 检查房间是否已满
        if (room.getOccupiedCount() >= room.getBedCount()) {
            throw new ServiceException(ResultCode.CONFLICT.getCode(), "该房间已满");
        }

        // 7. 创建入住记录
        CheckIn checkIn = new CheckIn();
        checkIn.setStudentId(studentId);
        checkIn.setRoomId(roomId);
        checkIn.setBedNo(bedNo);
        checkIn.setCheckInDate(LocalDate.now());
        checkIn.setStatus(1); // 1-已入住
        checkIn.setDepositStatus(1); // 1-未缴纳
        checkIn.setDepositAmount(BigDecimal.ZERO);
        // 办理人ID可以后续通过拦截器或AOP设置，这里暂不设置

        this.save(checkIn);

        // 8. 更新房间已入住人数
        room.setOccupiedCount(room.getOccupiedCount() + 1);
        roomService.updateById(room);
    }
}
