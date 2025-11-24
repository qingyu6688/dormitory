package com.example.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 室友信息视图对象
 */
@Data
public class RoommateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学生ID
     */
    private Long id;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 昵称/姓名
     */
    private String nickname;

    /**
     * 性别 1:男 2:女
     */
    private Integer gender;

    /**
     * 学院
     */
    private String college;

    /**
     * 专业
     */
    private String major;

    /**
     * 班级
     */
    private String className;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 床位号
     */
    private Integer bedNo;
}
