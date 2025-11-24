package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.OperationLog;

/**
 * 操作日志服务接口
 */
public interface OperationLogService extends IService<OperationLog> {

    /**
     * 记录操作日志
     * @param userId 用户ID
     * @param username 用户名
     * @param operation 操作类型
     * @param method 请求方法
     * @param params 请求参数
     * @param time 执行时长
     * @param ipAddress IP地址
     * @param status 操作状态
     * @param errorMsg 错误信息
     */
    void recordOperationLog(Long userId, String username, String operation, String method, 
                           String params, Long time, String ipAddress, Integer status, String errorMsg);
}
