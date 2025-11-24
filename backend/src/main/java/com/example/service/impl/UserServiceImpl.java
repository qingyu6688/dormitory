package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.ResultCode;
import com.example.entity.*;
import com.example.exception.ServiceException;
import com.example.mapper.UserMapper;
import com.example.service.*;
import com.example.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RoleService roleService;

    @Autowired
    @Lazy
    private CheckInService checkInService;

    @Autowired
    @Lazy
    private RoomService roomService;

    @Autowired
    @Lazy
    private BuildingService buildingService;

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return this.getOne(wrapper);
    }

    @Override
    public User getByEmail(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        return this.getOne(wrapper);
    }

    @Override
    public User login(String username, String password) {
        User user = getByUsername(username);
        if (user == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        if (!password.equals(user.getPassword())) {
            throw new ServiceException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }
        if (user.getStatus() == 0) {
            throw new ServiceException(ResultCode.USER_ACCOUNT_DISABLED);
        }
        return user;
    }

    @Override
    public IPage<UserVO> pageWithDetails(Page<User> page, String username, String studentNo, String college, Long roleId, Integer status) {
        // 构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(username)) {
            wrapper.like(User::getUsername, username);
        }
        if (StringUtils.hasText(studentNo)) {
            wrapper.like(User::getStudentNo, studentNo);
        }
        if (StringUtils.hasText(college)) {
            wrapper.like(User::getCollege, college);
        }
        if (roleId != null) {
            wrapper.eq(User::getRoleId, roleId);
        }
        if (status != null) {
            wrapper.eq(User::getStatus, status);
        }
        wrapper.orderByDesc(User::getCreateTime);

        // 查询分页数据
        IPage<User> userPage = this.page(page, wrapper);

        // 转换为VO对象
        IPage<UserVO> voPage = userPage.convert(this::convertToVO);

        return voPage;
    }

    @Override
    public UserVO getUserDetail(Long id) {
        User user = this.getById(id);
        if (user == null) {
            return null;
        }
        return convertToVO(user);
    }

    /**
     * 将User转换为UserVO，包含宿舍信息
     */
    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);

        // 查询角色信息
        if (user.getRoleId() != null) {
            Role role = roleService.getById(user.getRoleId());
            if (role != null) {
                vo.setRoleName(role.getName());
            }
        }

        // 性别文本
        if (user.getGender() != null) {
            vo.setGenderText(user.getGender() == 1 ? "男" : "女");
        }

        // 学生状态文本
        if (user.getStudentStatus() != null) {
            String[] statusTexts = {"", "在校", "休学", "毕业", "退学"};
            if (user.getStudentStatus() > 0 && user.getStudentStatus() < statusTexts.length) {
                vo.setStudentStatusText(statusTexts[user.getStudentStatus()]);
            }
        }

        // 查询宿舍信息
        LambdaQueryWrapper<CheckIn> checkInWrapper = new LambdaQueryWrapper<>();
        checkInWrapper.eq(CheckIn::getStudentId, user.getId())
                     .eq(CheckIn::getStatus, 1)
                     .orderByDesc(CheckIn::getCheckInDate)
                     .last("LIMIT 1");
        CheckIn checkIn = checkInService.getOne(checkInWrapper);

        if (checkIn != null) {
            vo.setHasRoom(true);
            vo.setRoomId(checkIn.getRoomId());
            vo.setBedNo(checkIn.getBedNo());
            // 将LocalDate转换为LocalDateTime
            if (checkIn.getCheckInDate() != null) {
                vo.setCheckInDate(checkIn.getCheckInDate().atStartOfDay());
            }
            vo.setCheckInStatus(checkIn.getStatus());
            vo.setDepositStatus(checkIn.getDepositStatus());
            vo.setDepositAmount(checkIn.getDepositAmount());

            // 查询房间信息
            Room room = roomService.getById(checkIn.getRoomId());
            if (room != null) {
                vo.setRoomNo(room.getRoomNo());
                vo.setFloor(room.getFloor());

                // 查询楼栋信息
                Building building = buildingService.getById(room.getBuildingId());
                if (building != null) {
                    vo.setBuildingId(building.getId());
                    vo.setBuildingName(building.getBuildingName());
                    vo.setBuildingNo(building.getBuildingNo());
                }

                // 查询室友数量
                LambdaQueryWrapper<CheckIn> roommateWrapper = new LambdaQueryWrapper<>();
                roommateWrapper.eq(CheckIn::getRoomId, checkIn.getRoomId())
                              .eq(CheckIn::getStatus, 1)
                              .ne(CheckIn::getStudentId, user.getId());
                long roommateCount = checkInService.count(roommateWrapper);
                vo.setRoommateCount((int) roommateCount);
            }
        } else {
            vo.setHasRoom(false);
        }

        return vo;
    }
}
