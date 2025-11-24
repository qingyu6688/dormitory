package com.example.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 卫生检查视图对象
 */
@Data
public class HygieneVO implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 房间号
     */
    private String roomNo;

    /**
     * 检查人ID
     */
    private Long checkerId;

    /**
     * 检查人姓名
     */
    private String checkerName;

    /**
     * 得分
     */
    private Integer score;

    /**
     * 等级
     */
    private String grade;

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
     * 复查日期
     */
    private LocalDate recheckDate;

    /**
     * 复查人ID
     */
    private Long recheckerId;

    /**
     * 复查结果
     */
    private String recheckResult;
}
