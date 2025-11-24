package com.example.aspect;

import com.example.annotation.Log;
import com.example.entity.User;
import com.example.service.OperationLogService;
import com.example.utils.IpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;

/**
 * 操作日志切面
 * 自动记录带有@Log注解的方法的操作日志
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 定义切点：所有带有@Log注解的方法
     */
    @Pointcut("@annotation(com.example.annotation.Log)")
    public void logPointcut() {
    }

    /**
     * 环绕通知：记录操作日志
     */
    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        // 获取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);
        
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        
        // 获取当前用户信息（从session或token中获取）
        Long userId = null;
        String username = "system";
        if (request != null && request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            userId = user.getId();
            username = user.getUsername();
        }
        
        // 获取方法参数
        String params = "";
        try {
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                Object[] filteredArgs = new Object[args.length];
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof MultipartFile) {
                        MultipartFile file = (MultipartFile) arg;
                        filteredArgs[i] = "MultipartFile(name=" + file.getName() + ", size=" + file.getSize() + ")";
                    } else if (arg instanceof HttpServletRequest) {
                        filteredArgs[i] = "HttpServletRequest";
                    } else {
                        filteredArgs[i] = arg;
                    }
                }
                params = objectMapper.writeValueAsString(filteredArgs);
                // 限制参数长度
                if (params.length() > 2000) {
                    params = params.substring(0, 2000) + "...";
                }
            }
        } catch (Exception e) {
            params = "参数解析失败";
        }
        
        // 执行目标方法
        Object result = null;
        Integer status = 1;
        String errorMsg = null;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            status = 0;
            errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.length() > 500) {
                errorMsg = errorMsg.substring(0, 500) + "...";
            }
            throw e;
        } finally {
            // 计算执行时长
            long time = System.currentTimeMillis() - startTime;
            
            // 获取操作类型
            String operation = logAnnotation.value();
            if (operation.isEmpty()) {
                operation = logAnnotation.description();
            }
            
            // 获取方法全路径
            String methodName = joinPoint.getTarget().getClass().getName() + "." + method.getName() + "()";
            
            // 获取IP地址
            String ipAddress = IpUtils.getIpAddress();
            
            // 异步记录日志（避免影响主业务）
            try {
                operationLogService.recordOperationLog(
                    userId,
                    username,
                    operation,
                    methodName,
                    params,
                    time,
                    ipAddress,
                    status,
                    errorMsg
                );
            } catch (Exception e) {
                // 日志记录失败不影响主业务
                e.printStackTrace();
            }
        }
        
        return result;
    }
}
