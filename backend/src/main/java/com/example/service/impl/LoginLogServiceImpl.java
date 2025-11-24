package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.LoginLog;
import com.example.mapper.LoginLogMapper;
import com.example.service.LoginLogService;
import com.example.utils.IpUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 登录日志服务实现类
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Override
    public void recordLoginLog(Long userId, String username, String ipAddress, Integer status, String message) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(userId);
        loginLog.setUsername(username);
        loginLog.setIpAddress(ipAddress);
        loginLog.setLoginLocation(IpUtils.getLocationByIp(ipAddress));
        loginLog.setBrowser(IpUtils.getBrowser());
        loginLog.setOs(IpUtils.getOperatingSystem());
        loginLog.setStatus(status);
        loginLog.setMessage(message);
        loginLog.setLoginTime(LocalDateTime.now());
        
        this.save(loginLog);
    }
}
