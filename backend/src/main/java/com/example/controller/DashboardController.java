package com.example.controller;

import com.example.common.Result;
import com.example.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 仪表盘控制器
 */
@Tag(name = "仪表盘", description = "首页统计数据接口")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 获取统计数据
     */
    @Operation(summary = "获取统计数据")
    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();

        // 用户总数
        long userCount = userService.count();
        stats.put("userCount", userCount);

        // 文件数量
        long fileCount = fileService.count();
        stats.put("fileCount", fileCount);

        // 登录日志数量（今日）
        long todayLoginCount = loginLogService.count();
        stats.put("loginCount", todayLoginCount);

        // 操作日志数量（今日）
        long todayOperationCount = operationLogService.count();
        stats.put("operationCount", todayOperationCount);

        // 系统运行天数（假设系统上线日期）
        stats.put("runningDays", calculateRunningDays());

        // 服务器时间
        stats.put("serverTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return Result.success(stats);
    }

    /**
     * 获取最近登录记录
     */
    @Operation(summary = "获取最近登录记录")
    @GetMapping("/recent-logins")
    public Result<Map<String, Object>> getRecentLogins() {
        // 可以返回最近5条登录记录
        Map<String, Object> data = new HashMap<>();
        // 这里可以调用 loginLogService 获取最近的登录记录
        return Result.success(data);
    }

    /**
     * 获取系统信息
     */
    @Operation(summary = "获取系统信息")
    @GetMapping("/system-info")
    public Result<Map<String, Object>> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();

        // JVM信息
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory() / 1024 / 1024; // MB
        long freeMemory = runtime.freeMemory() / 1024 / 1024; // MB
        long usedMemory = totalMemory - freeMemory;

        info.put("jvmTotalMemory", totalMemory + "MB");
        info.put("jvmUsedMemory", usedMemory + "MB");
        info.put("jvmFreeMemory", freeMemory + "MB");
        info.put("jvmMemoryUsage", String.format("%.2f%%", (double) usedMemory / totalMemory * 100));

        // 系统信息
        info.put("osName", System.getProperty("os.name"));
        info.put("osArch", System.getProperty("os.arch"));
        info.put("osVersion", System.getProperty("os.version"));
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("javaVendor", System.getProperty("java.vendor"));

        return Result.success(info);
    }

    /**
     * 计算系统运行天数
     */
    private long calculateRunningDays() {
        // 假设系统上线日期为 2025-11-01
        LocalDateTime startDate = LocalDateTime.of(2025, 11, 1, 0, 0);
        LocalDateTime now = LocalDateTime.now();
        return java.time.Duration.between(startDate, now).toDays();
    }
}
