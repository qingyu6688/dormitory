package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.entity.CheckIn;
import com.example.entity.Repair;
import com.example.entity.Room;
import com.example.entity.User;
import com.example.exception.ServiceException;
import com.example.mapper.RepairMapper;
import com.example.service.CheckInService;
import com.example.service.RepairService;
import com.example.service.RoomService;
import com.example.service.UserService;
import com.example.util.JwtUtil;
import com.example.vo.RepairVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 报修管理Service实现
 */
@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private CheckInService checkInService;

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
    public IPage<RepairVO> pageWithDetails(Page<Repair> page, String reporterNo, String reporterName, String roomNo, String repairType, Integer status) {
        // 根据报修人学号或姓名查询用户ID列表
        List<Long> reporterIds = null;
        if (StringUtils.hasText(reporterNo) || StringUtils.hasText(reporterName)) {
            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.hasText(reporterNo)) {
                userWrapper.like(User::getStudentNo, reporterNo);
            }
            if (StringUtils.hasText(reporterName)) {
                userWrapper.like(User::getNickname, reporterName);
            }
            List<User> users = userService.list(userWrapper);
            reporterIds = users.stream().map(User::getId).collect(Collectors.toList());
            if (reporterIds.isEmpty()) {
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
        LambdaQueryWrapper<Repair> wrapper = new LambdaQueryWrapper<>();
        if (reporterIds != null) {
            wrapper.in(Repair::getReporterId, reporterIds);
        }
        if (roomIds != null) {
            wrapper.in(Repair::getRoomId, roomIds);
        }
        if (StringUtils.hasText(repairType)) {
            wrapper.like(Repair::getRepairType, repairType);
        }
        if (status != null) {
            wrapper.eq(Repair::getStatus, status);
        }
        wrapper.orderByDesc(Repair::getReportTime);

        // 查询分页数据
        IPage<Repair> repairPage = this.page(page, wrapper);

        // 转换为VO对象
        IPage<RepairVO> voPage = repairPage.convert(repair -> {
            RepairVO vo = new RepairVO();
            BeanUtils.copyProperties(repair, vo);

            // 查询报修人信息
            if (repair.getReporterId() != null) {
                User reporter = userService.getById(repair.getReporterId());
                if (reporter != null) {
                    vo.setReporterNo(reporter.getStudentNo());
                    vo.setReporterName(reporter.getNickname());
                }
            }

            // 查询房间信息
            if (repair.getRoomId() != null) {
                Room room = roomService.getById(repair.getRoomId());
                if (room != null) {
                    vo.setRoomNo(room.getRoomNo());
                }
            }

            // 查询维修人员信息
            if (repair.getAssignedTo() != null) {
                User assignedUser = userService.getById(repair.getAssignedTo());
                if (assignedUser != null) {
                    vo.setAssignedToName(assignedUser.getNickname());
                }
            }

            return vo;
        });

        return voPage;
    }

    @Override
    public List<RepairVO> getMyRepairList() {
        // 获取当前登录用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, "未登录");
        }

        // 查询当前用户的报修记录
        LambdaQueryWrapper<Repair> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Repair::getReporterId, userId)
               .orderByDesc(Repair::getReportTime);

        List<Repair> repairList = this.list(wrapper);

        // 转换为VO对象
        List<RepairVO> voList = new ArrayList<>();
        for (Repair repair : repairList) {
            RepairVO vo = new RepairVO();
            BeanUtils.copyProperties(repair, vo);

            // 查询报修人信息
            User reporter = userService.getById(repair.getReporterId());
            if (reporter != null) {
                vo.setReporterNo(reporter.getStudentNo());
                vo.setReporterName(reporter.getNickname());
            }

            // 查询房间信息
            if (repair.getRoomId() != null) {
                Room room = roomService.getById(repair.getRoomId());
                if (room != null) {
                    vo.setRoomNo(room.getRoomNo());
                }
            }

            // 查询维修人员信息
            if (repair.getAssignedTo() != null) {
                User assignedUser = userService.getById(repair.getAssignedTo());
                if (assignedUser != null) {
                    vo.setAssignedToName(assignedUser.getNickname());
                }
            }

            voList.add(vo);
        }

        return voList;
    }

    @Override
    public void submitRepair(Repair repair) {
        // 获取当前登录用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, "未登录");
        }

        // 查询用户的入住记录，获取宿舍ID
        LambdaQueryWrapper<CheckIn> checkInWrapper = new LambdaQueryWrapper<>();
        checkInWrapper.eq(CheckIn::getStudentId, userId)
                     .eq(CheckIn::getStatus, 1) // 1:已入住
                     .orderByDesc(CheckIn::getCheckInDate)
                     .last("LIMIT 1");

        CheckIn checkIn = checkInService.getOne(checkInWrapper);
        if (checkIn == null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "您还未分配宿舍，无法提交报修");
        }

        // 设置报修信息
        repair.setReporterId(userId);
        repair.setRoomId(checkIn.getRoomId());
        repair.setReportTime(LocalDateTime.now());
        repair.setStatus(1); // 1:待处理

        // 保存报修记录
        this.save(repair);
    }

    @Override
    public List<String> getRepairTypes() {
        // 返回报修类型列表
        return Arrays.asList(
            "水电维修",
            "家具维修",
            "门窗维修",
            "网络故障",
            "空调维修",
            "照明维修",
            "卫生间维修",
            "其他"
        );
    }

    @Override
    public RepairVO getRepairDetail(Long id) {
        // 查询报修记录
        Repair repair = this.getById(id);
        if (repair == null) {
            return null;
        }

        // 转换为VO对象
        RepairVO vo = new RepairVO();
        BeanUtils.copyProperties(repair, vo);

        // 查询报修人信息
        if (repair.getReporterId() != null) {
            User reporter = userService.getById(repair.getReporterId());
            if (reporter != null) {
                vo.setReporterNo(reporter.getStudentNo());
                vo.setReporterName(reporter.getNickname());
            }
        }

        // 查询房间信息
        if (repair.getRoomId() != null) {
            Room room = roomService.getById(repair.getRoomId());
            if (room != null) {
                vo.setRoomNo(room.getRoomNo());
            }
        }

        // 查询维修人员信息
        if (repair.getAssignedTo() != null) {
            User assignedUser = userService.getById(repair.getAssignedTo());
            if (assignedUser != null) {
                vo.setAssignedToName(assignedUser.getNickname());
            }
        }

        return vo;
    }
}
