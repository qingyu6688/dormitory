package com.example.util;

import cn.hutool.crypto.digest.BCrypt;

/**
 * 密码加密工具类
 */
public class PasswordUtil {
    
    /**
     * 加密密码
     * @param password 明文密码
     * @return 加密后的密码
     */
    public static String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    
    /**
     * 验证密码
     * @param password 明文密码
     * @param hashed 加密后的密码
     * @return 是否匹配
     */
    public static boolean verify(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
