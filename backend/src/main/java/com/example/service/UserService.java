package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.User;
import com.example.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查询用户
     */
    User getByUsername(String username);

    /**
     * 根据邮箱查询用户
     */
    User getByEmail(String email);

    /**
     * 用户登录
     */
    User login(String username, String password);

    /**
     * 分页查询用户（包含宿舍信息）
     */
    IPage<UserVO> pageWithDetails(Page<User> page, String username, String studentNo, String college, Long roleId, Integer status);

    /**
     * 根据ID查询用户详情（包含宿舍信息）
     */
    UserVO getUserDetail(Long id);
}
