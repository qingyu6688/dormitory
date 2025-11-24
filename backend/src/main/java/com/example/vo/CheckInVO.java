package com.example.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 入住记录视图对象
 */
@Data
public class CheckInVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 学生学号
     */
    private String studentNo;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 宿舍ID
     */
    private Long roomId;

    /**
     * 房间号
     */
    private String roomNo;

    /**
     * 床位号
     */
    private Integer bedNo;

    /**
     * 入住日期
     */
    private LocalDate checkInDate;

    /**
     * 计划退宿日期
     */
    private LocalDate planCheckOutDate;

    /**
     * 实际退宿日期
     */
    private LocalDate actualCheckOutDate;

    /**
     * 状态 1:已入住 2:已退宿
     */
    private Integer status;

    /**
     * 押金金额
     */
    private BigDecimal depositAmount;

    /**
     * 押金状态 1:未交 2:已交 3:已退
     */
    private Integer depositStatus;

    /**
     * 钥匙编号
     */
    private String keyNo;

    /**
     * 办理人ID
     */
    private Long operatorId;

    /**
     * 办理人姓名
     */
    private String operatorName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
