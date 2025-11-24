package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 违纪记录表
 */
@Data
@TableName("dorm_violation")
public class Violation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 学生ID,逗号分隔
     */
    private String studentIds;

    /**
     * 宿舍ID
     */
    private Long roomId;

    /**
     * 违纪类型
     */
    private String violationType;

    /**
     * 违纪时间
     */
    private LocalDateTime violationTime;

    /**
     * 违纪描述
     */
    private String description;

    /**
     * 证据材料(JSON数组)
     */
    private String evidence;

    /**
     * 处理方式
     */
    private String handleMethod;

    /**
     * 扣分
     */
    private Integer deductScore;

    /**
     * 记录人ID
     */
    private Long recorderId;

    /**
     * 是否通知家长
     */
    private Integer notifyParent;

    /**
     * 是否通知辅导员
     */
    private Integer notifyTeacher;

    /**
     * 处理状态 1:待处理 2:已处理
     */
    private Integer handleStatus;

    /**
     * 处理结果
     */
    private String handleResult;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
