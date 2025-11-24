package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.entity.CheckIn;
import com.example.entity.Room;
import com.example.entity.UtilityBill;
import com.example.exception.ServiceException;
import com.example.mapper.UtilityBillMapper;
import com.example.service.CheckInService;
import com.example.service.RoomService;
import com.example.service.UtilityBillService;
import com.example.util.JwtUtil;
import com.example.vo.UtilityBillVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 水电费Service实现
 */
@Service
public class UtilityBillServiceImpl extends ServiceImpl<UtilityBillMapper, UtilityBill> implements UtilityBillService {

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
        
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        return JwtUtil.getUserId(token);
    }

    @Override
    public IPage<UtilityBillVO> pageWithDetails(Page<UtilityBill> page, String roomNo, String billMonth, Integer paymentStatus) {
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

        LambdaQueryWrapper<UtilityBill> wrapper = new LambdaQueryWrapper<>();
        if (roomIds != null) {
            wrapper.in(UtilityBill::getRoomId, roomIds);
        }
        if (StringUtils.hasText(billMonth)) {
            wrapper.like(UtilityBill::getBillMonth, billMonth);
        }
        if (paymentStatus != null) {
            wrapper.eq(UtilityBill::getPaymentStatus, paymentStatus);
        }
        wrapper.orderByDesc(UtilityBill::getBillMonth);

        IPage<UtilityBill> billPage = this.page(page, wrapper);

        IPage<UtilityBillVO> voPage = billPage.convert(bill -> {
            UtilityBillVO vo = new UtilityBillVO();
            BeanUtils.copyProperties(bill, vo);
            if (bill.getRoomId() != null) {
                Room room = roomService.getById(bill.getRoomId());
                if (room != null) {
                    vo.setRoomNo(room.getRoomNo());
                }
            }
            return vo;
        });

        return voPage;
    }

    @Override
    public List<UtilityBillVO> getMyBills() {
        // 获取当前登录用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, "未登录");
        }

        // 查询用户的入住记录，获取宿舍ID
        LambdaQueryWrapper<CheckIn> checkInWrapper = new LambdaQueryWrapper<>();
        checkInWrapper.eq(CheckIn::getStudentId, userId)
                     .eq(CheckIn::getStatus, 1)
                     .orderByDesc(CheckIn::getCheckInDate)
                     .last("LIMIT 1");

        CheckIn checkIn = checkInService.getOne(checkInWrapper);
        if (checkIn == null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "您还未分配宿舍，无法查询费用");
        }

        // 查询该宿舍的所有账单
        LambdaQueryWrapper<UtilityBill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UtilityBill::getRoomId, checkIn.getRoomId())
               .orderByDesc(UtilityBill::getBillMonth);

        List<UtilityBill> billList = this.list(wrapper);

        // 转换为VO对象
        List<UtilityBillVO> voList = new ArrayList<>();
        for (UtilityBill bill : billList) {
            UtilityBillVO vo = new UtilityBillVO();
            BeanUtils.copyProperties(bill, vo);

            // 查询房间信息
            Room room = roomService.getById(bill.getRoomId());
            if (room != null) {
                vo.setRoomNo(room.getRoomNo());
            }

            voList.add(vo);
        }

        return voList;
    }

    @Override
    public void payBill(Long id, String paymentMethod) {
        // 获取当前登录用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, "未登录");
        }

        // 查询账单
        UtilityBill bill = this.getById(id);
        if (bill == null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "账单不存在");
        }

        // 检查账单是否已缴费
        if (bill.getPaymentStatus() == 2) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "该账单已缴费");
        }

        // 更新缴费状态
        bill.setPaymentStatus(2); // 已缴
        bill.setPaymentTime(LocalDateTime.now());
        bill.setPaymentMethod(paymentMethod);
        this.updateById(bill);
    }
}
