package com.example.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 报修管理视图对象
 */
@Data
public class RepairVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 报修人ID
     */
    private Long reporterId;

    /**
     * 报修人姓名
     */
    private String reporterName;

    /**
     * 报修人学号
     */
    private String reporterNo;

    /**
     * 宿舍ID
     */
    private Long roomId;

    /**
     * 房间号
     */
    private String roomNo;

    /**
     * 报修类型
     */
    private String repairType;

    /**
     * 报修内容描述
     */
    private String description;

    /**
     * 报修图片(JSON数组)
     */
    private String images;

    /**
     * 报修时间
     */
    private LocalDateTime reportTime;

    /**
     * 紧急程度 1:普通 2:紧急 3:特急
     */
    private Integer urgencyLevel;

    /**
     * 状态 1:待处理 2:处理中 3:已完成
     */
    private Integer status;

    /**
     * 指派维修人员ID
     */
    private Long assignedTo;

    /**
     * 维修人员姓名
     */
    private String assignedToName;

    /**
     * 预约上门时间
     */
    private LocalDateTime appointmentTime;

    /**
     * 实际处理时间
     */
    private LocalDateTime processTime;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 维修费用
     */
    private BigDecimal repairCost;

    /**
     * 费用承担方 1:学校 2:学生
     */
    private Integer costBearer;

    /**
     * 处理结果描述
     */
    private String resultDesc;

    /**
     * 满意度评价 1-5星
     */
    private Integer satisfaction;

    /**
     * 评价内容
     */
    private String comment;
}
