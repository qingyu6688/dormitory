package com.example.common;

import lombok.Getter;

/**
 * 统一返回状态码
 */
@Getter
public enum ResultCode {

    /* 成功状态码 */
    SUCCESS(200, "操作成功"),
    
    /* 客户端错误: 400-499 */
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权，请先登录"),
    FORBIDDEN(403, "没有权限访问"),
    NOT_FOUND(404, "请求的资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),
    REQUEST_TIMEOUT(408, "请求超时"),
    CONFLICT(409, "数据冲突"),
    
    /* 业务错误: 1000-1999 */
    USER_NOT_EXIST(1001, "用户不存在"),
    USER_ALREADY_EXIST(1002, "用户已存在"),
    USERNAME_OR_PASSWORD_ERROR(1003, "用户名或密码错误"),
    PASSWORD_ERROR(1004, "密码错误"),
    USER_DISABLED(1005, "账号已被禁用"),
    USER_ACCOUNT_DISABLED(1006, "账号已被禁用"),
    USER_ACCOUNT_LOCKED(1007, "账号已被锁定"),
    OLD_PASSWORD_ERROR(1008, "原密码错误"),
    
    ROLE_NOT_EXIST(1101, "角色不存在"),
    ROLE_ALREADY_EXIST(1102, "角色已存在"),
    ROLE_IN_USE(1103, "角色使用中，无法删除"),
    
    PERMISSION_DENIED(1201, "权限不足"),
    PERMISSION_NOT_EXIST(1202, "权限不存在"),
    
    TOKEN_INVALID(1301, "Token无效"),
    TOKEN_EXPIRED(1302, "Token已过期"),
    TOKEN_MISSING(1303, "Token缺失"),
    
    VALIDATE_FAILED(1401, "参数校验失败"),
    UPLOAD_FILE_SIZE_LIMIT(1402, "上传文件大小超出限制"),
    UPLOAD_FILE_TYPE_ERROR(1403, "上传文件类型错误"),
    
    DATA_NOT_EXIST(1501, "数据不存在"),
    DATA_ALREADY_EXIST(1502, "数据已存在"),
    DATA_IN_USE(1503, "数据使用中，无法删除"),
    
    /* 服务器错误: 500-599 */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂时不可用"),
    DATABASE_ERROR(5001, "数据库操作失败"),
    REDIS_ERROR(5002, "缓存操作失败"),
    FILE_OPERATION_ERROR(5003, "文件操作失败"),
    NETWORK_ERROR(5004, "网络请求失败"),
    
    /* 其他错误 */
    UNKNOWN_ERROR(9999, "未知错误");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
