package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Room;
import com.example.entity.User;
import com.example.entity.Violation;
import com.example.mapper.ViolationMapper;
import com.example.service.RoomService;
import com.example.service.UserService;
import com.example.service.ViolationService;
import com.example.vo.ViolationVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 违纪记录Service实现
 */
@Service
public class ViolationServiceImpl extends ServiceImpl<ViolationMapper, Violation> implements ViolationService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Override
    public IPage<ViolationVO> pageWithDetails(Page<Violation> page, String studentNo, String roomNo, String violationType, Integer handleStatus) {
        // 根据学号查询学生ID列表
        List<Long> studentIds = null;
        if (StringUtils.hasText(studentNo)) {
            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.like(User::getStudentNo, studentNo);
            List<User> users = userService.list(userWrapper);
            studentIds = users.stream().map(User::getId).collect(Collectors.toList());
            if (studentIds.isEmpty()) {
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
                return new Page<>(page.getCurrent(), page.getSize(), 0);
            }
        }

        // 构建查询条件
        LambdaQueryWrapper<Violation> wrapper = new LambdaQueryWrapper<>();
        if (studentIds != null) {
            // studentIds字段是逗号分隔的字符串，需要用like查询
            final List<Long> finalStudentIds = studentIds;
            wrapper.and(w -> {
                for (Long studentId : finalStudentIds) {
                    w.or().like(Violation::getStudentIds, studentId.toString());
                }
            });
        }
        if (roomIds != null) {
            wrapper.in(Violation::getRoomId, roomIds);
        }
        if (StringUtils.hasText(violationType)) {
            wrapper.like(Violation::getViolationType, violationType);
        }
        if (handleStatus != null) {
            wrapper.eq(Violation::getHandleStatus, handleStatus);
        }
        wrapper.orderByDesc(Violation::getViolationTime);

        // 查询分页数据
        IPage<Violation> violationPage = this.page(page, wrapper);

        // 转换为VO对象
        IPage<ViolationVO> voPage = violationPage.convert(violation -> {
            ViolationVO vo = new ViolationVO();
            BeanUtils.copyProperties(violation, vo);

            // 查询房间信息
            if (violation.getRoomId() != null) {
                Room room = roomService.getById(violation.getRoomId());
                if (room != null) {
                    vo.setRoomNo(room.getRoomNo());
                }
            }

            // 解析学生ID列表并查询学生姓名
            if (StringUtils.hasText(violation.getStudentIds())) {
                String[] idArray = violation.getStudentIds().split(",");
                List<Long> ids = Arrays.stream(idArray)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                
                if (!ids.isEmpty()) {
                    List<User> students = userService.listByIds(ids);
                    String studentNames = students.stream()
                            .map(User::getNickname)
                            .collect(Collectors.joining(", "));
                    vo.setStudentNames(studentNames);
                }
            }

            return vo;
        });

        return voPage;
    }
}
