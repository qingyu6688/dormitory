package com.example.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * 用于标记需要记录操作日志的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    
    /**
     * 操作类型（如：新增、修改、删除、查询等）
     */
    String value() default "";
    
    /**
     * 操作描述
     */
    String description() default "";
}
