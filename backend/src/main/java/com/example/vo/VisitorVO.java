package com.example.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 访客登记视图对象
 */
@Data
public class VisitorVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 访客姓名
     */
    private String visitorName;

    /**
     * 访客性别
     */
    private Integer visitorGender;

    /**
     * 访客身份证号
     */
    private String visitorIdCard;

    /**
     * 访客电话
     */
    private String visitorPhone;

    /**
     * 被访学生ID
     */
    private Long visitedStudentId;

    /**
     * 被访学生学号
     */
    private String visitedStudentNo;

    /**
     * 被访学生姓名
     */
    private String visitedStudentName;

    /**
     * 宿舍楼ID
     */
    private Long buildingId;

    /**
     * 宿舍楼名称
     */
    private String buildingName;

    /**
     * 来访事由
     */
    private String visitReason;

    /**
     * 进入时间
     */
    private LocalDateTime enterTime;

    /**
     * 预计离开时间
     */
    private LocalDateTime planLeaveTime;

    /**
     * 实际离开时间
     */
    private LocalDateTime actualLeaveTime;

    /**
     * 登记人ID
     */
    private Long registrarId;

    /**
     * 状态 1:在楼内 2:已离开
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
