package com.example.controller;

import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.entity.Menu;
import com.example.entity.User;
import com.example.exception.ServiceException;
import com.example.service.LoginLogService;
import com.example.service.MenuService;
import com.example.service.UserService;
import com.example.util.JwtUtil;
import com.example.util.PasswordUtil;
import com.example.utils.IpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证控制器
 */
@Tag(name = "认证管理", description = "登录、注册、登出、获取用户信息接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private LoginLogService loginLogService;
    
    @Autowired
    private com.example.service.EmailService emailService;

    @Operation(summary = "用户登录", description = "用户名密码登录，返回用户信息、token和菜单")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginData, HttpServletRequest request) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        
        // 获取客户端IP地址
        String ipAddress = IpUtils.getIpAddress();
        
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "用户名和密码不能为空");
        }
        
        try {
            // 根据用户名查询用户
            User user = userService.getByUsername(username);
            if (user == null) {
                // 记录登录失败日志
                loginLogService.recordLoginLog(null, username, ipAddress, 0, "用户不存在");
                throw new ServiceException(ResultCode.USER_NOT_EXIST);
            }
            
            // 验证密码
            if (!PasswordUtil.verify(password, user.getPassword())) {
                // 记录登录失败日志
                loginLogService.recordLoginLog(user.getId(), username, ipAddress, 0, "密码错误");
                throw new ServiceException(ResultCode.PASSWORD_ERROR);
            }
            
            // 检查用户状态
            if (user.getStatus() == null || user.getStatus() != 1) {
                // 记录登录失败日志
                loginLogService.recordLoginLog(user.getId(), username, ipAddress, 0, "账号已被禁用");
                throw new ServiceException(ResultCode.USER_DISABLED);
            }
            
            // 更新最后登录时间和IP
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(ipAddress);
            userService.updateById(user);
            
            // 记录登录成功日志
            loginLogService.recordLoginLog(user.getId(), username, ipAddress, 1, "登录成功");
            
            // 获取用户菜单（根据角色ID）
            List<Menu> menus = null;
            if (user.getRoleId() != null) {
                menus = menuService.getMenusByRoleId(user.getRoleId());
            }
            
            // 清空密码
            user.setPassword(null);
            
            // 生成JWT Token
            String token = JwtUtil.generateToken(user.getId(), user.getUsername());
            
            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            data.put("token", token);
            data.put("menus", menus);
            
            return Result.success(data);
        } catch (ServiceException e) {
            // 重新抛出业务异常
            throw e;
        } catch (Exception e) {
            // 记录未知错误的登录失败日志
            loginLogService.recordLoginLog(null, username, ipAddress, 0, "系统错误: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 获取客户端真实IPv4地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // 对于通过多个代理的情况，第一个IP为客户端真实IP，多个IP按照','分割
        if (ip != null && ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }
        
        // 如果是IPv6的本地地址，转换为IPv4
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            ip = "127.0.0.1";
        }
        
        return ip;
    }

    @Operation(summary = "用户注册", description = "新用户注册")
    @PostMapping("/register")
    public Result<Void> register(@RequestBody User user) {
        // 验证必填字段
        if (!StringUtils.hasText(user.getUsername())) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "用户名不能为空");
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "密码不能为空");
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "邮箱不能为空");
        }
        
        // 检查用户名是否已存在
        User existUser = userService.getByUsername(user.getUsername());
        if (existUser != null) {
            throw new ServiceException(ResultCode.USER_ALREADY_EXIST);
        }
        
        // 设置默认角色为学生（roleId = 4）
        if (user.getRoleId() == null) {
            user.setRoleId(4L);
        }
        
        // 设置默认状态为启用
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        
        // 加密密码
        user.setPassword(PasswordUtil.encrypt(user.getPassword()));
        
        // 保存用户
        boolean success = userService.save(user);
        if (!success) {
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, "注册失败");
        }
        
        return Result.success();
    }

    @Operation(summary = "获取当前用户信息", description = "根据token获取用户信息和菜单")
    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo(@RequestHeader(value = "Authorization", required = false) String token) {
        if (!StringUtils.hasText(token)) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, "未登录");
        }
        
        // 移除Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        // 验证Token
        if (!JwtUtil.verifyToken(token)) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, "Token无效或已过期");
        }
        
        // 从Token中获取用户ID
        Long userId = JwtUtil.getUserId(token);
        if (userId == null) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, "Token解析失败");
        }
        
        User user = userService.getById(userId);
        if (user == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        
        // 获取用户菜单
        List<Menu> menus = null;
        if (user.getRoleId() != null) {
            menus = menuService.getMenusByRoleId(user.getRoleId());
        }
        
        // 清空密码
        user.setPassword(null);
        
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("menus", menus);
        
        return Result.success(data);
    }

    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result<Void> logout() {
        // 实际项目中可以在这里清除token、记录登出日志等
        return Result.success();
    }

    @Operation(summary = "发送密码重置验证码", description = "向用户邮箱发送6位数字验证码")
    @PostMapping("/send-reset-code")
    public Result<Void> sendResetCode(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        String email = data.get("email");
        
        if (!StringUtils.hasText(username)) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "用户名不能为空");
        }
        
        if (!StringUtils.hasText(email)) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "邮箱不能为空");
        }
        
        // 根据用户名查询用户
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "用户不存在");
        }
        
        // 验证邮箱是否匹配
        if (!email.equals(user.getEmail())) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "用户名与邮箱不匹配");
        }
        
        // 生成6位随机验证码
        String code = String.format("%06d", (int)(Math.random() * 1000000));
        
        // 将验证码存储到缓存中，使用username+email作为key
        // 实际项目中应该使用Redis等缓存，这里简化处理，存储在内存Map中
        String cacheKey = username + ":" + email;
        resetCodeCache.put(cacheKey, code);
        
        // 发送邮件
        try {
            emailService.sendVerificationCode(email, code);
            System.out.println("验证码已发送到邮箱: " + email + ", 用户名: " + username);
        } catch (Exception e) {
            System.err.println("邮件发送失败: " + e.getMessage());
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, "邮件发送失败，请稍后重试");
        }
        
        return Result.success();
    }

    @Operation(summary = "验证密码重置验证码")
    @PostMapping("/verify-reset-code")
    public Result<Void> verifyResetCode(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        String email = data.get("email");
        String code = data.get("code");
        
        if (!StringUtils.hasText(username) || !StringUtils.hasText(email) || !StringUtils.hasText(code)) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "用户名、邮箱和验证码不能为空");
        }
        
        // 验证验证码
        String cacheKey = username + ":" + email;
        String cachedCode = resetCodeCache.get(cacheKey);
        if (cachedCode == null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "验证码已过期，请重新获取");
        }
        
        if (!cachedCode.equals(code)) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "验证码错误");
        }
        
        return Result.success();
    }

    @Operation(summary = "重置密码")
    @PostMapping("/reset-password")
    public Result<Void> resetPassword(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        String email = data.get("email");
        String code = data.get("code");
        String newPassword = data.get("newPassword");
        
        if (!StringUtils.hasText(username) || !StringUtils.hasText(email) || 
            !StringUtils.hasText(code) || !StringUtils.hasText(newPassword)) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "参数不完整");
        }
        
        // 再次验证验证码
        String cacheKey = username + ":" + email;
        String cachedCode = resetCodeCache.get(cacheKey);
        if (cachedCode == null || !cachedCode.equals(code)) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "验证码错误或已过期");
        }
        
        // 查询用户
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "用户不存在");
        }
        
        // 再次验证邮箱是否匹配
        if (!email.equals(user.getEmail())) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "用户名与邮箱不匹配");
        }
        
        // 更新密码
        user.setPassword(PasswordUtil.encrypt(newPassword));
        boolean success = userService.updateById(user);
        if (!success) {
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, "密码重置失败");
        }
        
        // 清除验证码
        resetCodeCache.remove(cacheKey);
        
        return Result.success();
    }

    // 验证码缓存（实际项目中应该使用Redis）
    private static final Map<String, String> resetCodeCache = new java.util.concurrent.ConcurrentHashMap<>();
}
