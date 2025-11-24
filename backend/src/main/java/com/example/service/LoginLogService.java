package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.LoginLog;

/**
 * 登录日志服务接口
 */
public interface LoginLogService extends IService<LoginLog> {

    /**
     * 记录登录日志
     * @param userId 用户ID
     * @param username 用户名
     * @param ipAddress IP地址
     * @param status 登录状态 1:成功 0:失败
     * @param message 提示消息
     */
    void recordLoginLog(Long userId, String username, String ipAddress, Integer status, String message);
}
