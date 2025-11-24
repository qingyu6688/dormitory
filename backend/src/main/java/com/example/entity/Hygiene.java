package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 卫生检查表
 */
@Data
@TableName("dorm_hygiene")
public class Hygiene implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 检查日期
     */
    private LocalDate checkDate;

    /**
     * 检查类型
     */
    private String checkType;

    /**
     * 宿舍ID
     */
    private Long roomId;

    /**
     * 检查人ID
     */
    private Long checkerId;

    /**
     * 卫生评分 0-100分
     */
    private Integer score;

    /**
     * 评分等级
     */
    private String grade;

    /**
     * 扣分项(JSON)
     */
    private String deductItems;

    /**
     * 加分项(JSON)
     */
    private String bonusItems;

    /**
     * 检查照片(JSON数组)
     */
    private String images;

    /**
     * 问题描述
     */
    private String problemDesc;

    /**
     * 整改要求
     */
    private String rectifyRequirement;

    /**
     * 整改期限
     */
    private LocalDate rectifyDeadline;

    /**
     * 复查状态 1:无需复查 2:待复查 3:已复查
     */
    private Integer recheckStatus;

    /**
     * 复查时间
     */
    private LocalDateTime recheckTime;

    /**
     * 复查结果
     */
    private String recheckResult;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
