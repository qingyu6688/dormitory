package com.example.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 宿舍住宿人员视图对象
 */
@Data
public class RoomResidentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 入住记录ID
     */
    private Long checkInId;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 性别
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
     * 电话
     */
    private String phone;

    /**
     * 床位号
     */
    private Integer bedNo;

    /**
     * 入住日期
     */
    private LocalDate checkInDate;

    /**
     * 入住状态 1:已入住 2:已退宿
     */
    private Integer status;
}
