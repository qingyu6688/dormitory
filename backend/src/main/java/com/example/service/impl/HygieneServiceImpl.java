package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.entity.CheckIn;
import com.example.entity.Hygiene;
import com.example.entity.Room;
import com.example.entity.User;
import com.example.exception.ServiceException;
import com.example.mapper.HygieneMapper;
import com.example.service.CheckInService;
import com.example.service.HygieneService;
import com.example.service.RoomService;
import com.example.service.UserService;
import com.example.util.JwtUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.example.vo.HygieneVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 卫生检查Service实现
 */
@Service
public class HygieneServiceImpl extends ServiceImpl<HygieneMapper, Hygiene> implements HygieneService {

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
    public IPage<HygieneVO> pageWithDetails(Page<Hygiene> page, String roomNo, String checkType, Integer recheckStatus) {
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
        LambdaQueryWrapper<Hygiene> wrapper = new LambdaQueryWrapper<>();
        if (roomIds != null) {
            wrapper.in(Hygiene::getRoomId, roomIds);
        }
        if (StringUtils.hasText(checkType)) {
            wrapper.like(Hygiene::getCheckType, checkType);
        }
        if (recheckStatus != null) {
            wrapper.eq(Hygiene::getRecheckStatus, recheckStatus);
        }
        wrapper.orderByDesc(Hygiene::getCheckDate);

        // 查询分页数据
        IPage<Hygiene> hygienePage = this.page(page, wrapper);

        // 转换为VO对象
        IPage<HygieneVO> voPage = hygienePage.convert(hygiene -> {
            HygieneVO vo = new HygieneVO();
            BeanUtils.copyProperties(hygiene, vo);

            // 查询房间信息
            if (hygiene.getRoomId() != null) {
                Room room = roomService.getById(hygiene.getRoomId());
                if (room != null) {
                    vo.setRoomNo(room.getRoomNo());
                }
            }

            // 查询检查人信息
            if (hygiene.getCheckerId() != null) {
                User checker = userService.getById(hygiene.getCheckerId());
                if (checker != null) {
                    vo.setCheckerName(checker.getNickname());
                }
            }

            return vo;
        });

        return voPage;
    }

    @Override
    public Object getMyScore() {
        // 获取当前登录用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, "未登录");
        }

        // 查询用户的入住记录
        LambdaQueryWrapper<CheckIn> checkInWrapper = new LambdaQueryWrapper<>();
        checkInWrapper.eq(CheckIn::getStudentId, userId)
                     .eq(CheckIn::getStatus, 1) // 1:已入住
                     .orderByDesc(CheckIn::getCheckInDate)
                     .last("LIMIT 1");

        CheckIn checkIn = checkInService.getOne(checkInWrapper);
        if (checkIn == null) {
            // 未分配宿舍，返回默认评分
            Map<String, Object> result = new HashMap<>();
            result.put("score", 0);
            result.put("message", "未分配宿舍");
            return result;
        }

        // 查询该宿舍最近的卫生检查记录
        LambdaQueryWrapper<Hygiene> hygieneWrapper = new LambdaQueryWrapper<>();
        hygieneWrapper.eq(Hygiene::getRoomId, checkIn.getRoomId())
                     .orderByDesc(Hygiene::getCheckDate)
                     .last("LIMIT 1");

        Hygiene hygiene = this.getOne(hygieneWrapper);
        
        Map<String, Object> result = new HashMap<>();
        if (hygiene != null) {
            result.put("score", hygiene.getScore());
            result.put("checkDate", hygiene.getCheckDate());
            result.put("checkType", hygiene.getCheckType());
            result.put("grade", hygiene.getGrade());
            result.put("problemDesc", hygiene.getProblemDesc());
            result.put("deductItems", hygiene.getDeductItems());
        } else {
            // 没有检查记录，返回默认评分
            result.put("score", 85);
            result.put("message", "暂无检查记录");
        }
        
        return result;
    }
}
