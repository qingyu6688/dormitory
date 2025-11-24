package com.example.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 违纪记录视图对象
 */
@Data
public class ViolationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 学生ID列表（逗号分隔）
     */
    private String studentIds;

    /**
     * 学生姓名列表
     */
    private String studentNames;

    /**
     * 宿舍ID
     */
    private Long roomId;

    /**
     * 房间号
     */
    private String roomNo;

    /**
     * 违纪类型
     */
    private String violationType;

    /**
     * 违纪时间
     */
    private LocalDateTime violationTime;

    /**
     * 描述
     */
    private String description;

    /**
     * 处理方式
     */
    private String handleMethod;

    /**
     * 扣分
     */
    private Integer deductScore;

    /**
     * 处理状态 1:待处理 2:已处理
     */
    private Integer handleStatus;

    /**
     * 是否通知家长 1:是 2:否
     */
    private Integer notifyParent;

    /**
     * 备注
     */
    private String remark;
}
