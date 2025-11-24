package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.OperationLog;
import com.example.mapper.OperationLogMapper;
import com.example.service.OperationLogService;
import com.example.utils.IpUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 操作日志服务实现类
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Override
    public void recordOperationLog(Long userId, String username, String operation, String method, 
                                   String params, Long time, String ipAddress, Integer status, String errorMsg) {
        OperationLog operationLog = new OperationLog();
        operationLog.setUserId(userId);
        operationLog.setUsername(username);
        operationLog.setOperation(operation);
        operationLog.setMethod(method);
        operationLog.setParams(params);
        operationLog.setTime(time);
        operationLog.setIpAddress(ipAddress);
        operationLog.setLocation(IpUtils.getLocationByIp(ipAddress));
        operationLog.setStatus(status);
        operationLog.setErrorMsg(errorMsg);
        operationLog.setCreateTime(LocalDateTime.now());
        
        this.save(operationLog);
    }
}
