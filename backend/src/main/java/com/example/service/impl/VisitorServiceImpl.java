package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.entity.Building;
import com.example.entity.CheckIn;
import com.example.entity.Room;
import com.example.entity.User;
import com.example.entity.Visitor;
import com.example.exception.ServiceException;
import com.example.mapper.VisitorMapper;
import com.example.service.BuildingService;
import com.example.service.CheckInService;
import com.example.service.RoomService;
import com.example.service.UserService;
import com.example.service.VisitorService;
import com.example.util.JwtUtil;
import com.example.vo.VisitorVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 访客登记Service实现
 */
@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorMapper, Visitor> implements VisitorService {

    @Autowired
    private UserService userService;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private RoomService roomService;

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
        
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        return JwtUtil.getUserId(token);
    }

    @Override
    public IPage<VisitorVO> pageWithDetails(Page<Visitor> page, Long visitedStudentId, Long buildingId, Integer status) {
        // 构建查询条件
        LambdaQueryWrapper<Visitor> wrapper = new LambdaQueryWrapper<>();
        if (visitedStudentId != null) {
            wrapper.eq(Visitor::getVisitedStudentId, visitedStudentId);
        }
        if (buildingId != null) {
            wrapper.eq(Visitor::getBuildingId, buildingId);
        }
        if (status != null) {
            wrapper.eq(Visitor::getStatus, status);
        }
        wrapper.orderByDesc(Visitor::getEnterTime);

        // 查询分页数据
        IPage<Visitor> visitorPage = this.page(page, wrapper);

        // 转换为VO对象
        IPage<VisitorVO> voPage = visitorPage.convert(visitor -> {
            VisitorVO vo = new VisitorVO();
            BeanUtils.copyProperties(visitor, vo);

            // 查询被访学生信息
            if (visitor.getVisitedStudentId() != null) {
                User student = userService.getById(visitor.getVisitedStudentId());
                if (student != null) {
                    vo.setVisitedStudentNo(student.getStudentNo());
                    vo.setVisitedStudentName(student.getNickname());
                }
            }

            // 查询宿舍楼信息
            if (visitor.getBuildingId() != null) {
                Building building = buildingService.getById(visitor.getBuildingId());
                if (building != null) {
                    vo.setBuildingName(building.getBuildingName());
                }
            }

            return vo;
        });

        return voPage;
    }

    @Override
    public List<VisitorVO> getMyVisitorList() {
        // 获取当前登录用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, "未登录");
        }

        // 查询当前用户的访客记录
        LambdaQueryWrapper<Visitor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Visitor::getVisitedStudentId, userId)
               .orderByDesc(Visitor::getEnterTime);

        List<Visitor> visitorList = this.list(wrapper);

        // 转换为VO对象
        List<VisitorVO> voList = new ArrayList<>();
        for (Visitor visitor : visitorList) {
            VisitorVO vo = new VisitorVO();
            BeanUtils.copyProperties(visitor, vo);

            // 查询被访学生信息
            User student = userService.getById(visitor.getVisitedStudentId());
            if (student != null) {
                vo.setVisitedStudentNo(student.getStudentNo());
                vo.setVisitedStudentName(student.getNickname());
            }

            // 查询宿舍楼信息
            if (visitor.getBuildingId() != null) {
                Building building = buildingService.getById(visitor.getBuildingId());
                if (building != null) {
                    vo.setBuildingName(building.getBuildingName());
                }
            }

            voList.add(vo);
        }

        return voList;
    }

    @Override
    public void registerVisitor(Visitor visitor) {
        // 获取当前登录用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, "未登录");
        }

        // 查询用户的入住记录，获取宿舍楼ID
        LambdaQueryWrapper<CheckIn> checkInWrapper = new LambdaQueryWrapper<>();
        checkInWrapper.eq(CheckIn::getStudentId, userId)
                     .eq(CheckIn::getStatus, 1)
                     .orderByDesc(CheckIn::getCheckInDate)
                     .last("LIMIT 1");

        CheckIn checkIn = checkInService.getOne(checkInWrapper);
        if (checkIn == null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "您还未分配宿舍，无法登记访客");
        }

        // 通过房间ID查询楼栋ID
        Room room = roomService.getById(checkIn.getRoomId());
        if (room != null) {
            visitor.setBuildingId(room.getBuildingId());
        }
        
        // 设置访客信息
        visitor.setVisitedStudentId(userId);
        visitor.setRegistrarId(userId);
        visitor.setEnterTime(LocalDateTime.now());
        visitor.setStatus(1); // 1:在楼内
        visitor.setCreateTime(LocalDateTime.now());

        // 保存访客记录
        this.save(visitor);
    }
}
