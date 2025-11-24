package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 调宿申请表
 */
@Data
@TableName("dorm_transfer")
public class Transfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 申请学生ID
     */
    private Long studentId;

    /**
     * 当前宿舍ID
     */
    private Long currentRoomId;

    /**
     * 目标宿舍ID
     */
    private Long targetRoomId;

    /**
     * 申请原因
     */
    private String reason;

    /**
     * 申请时间
     */
    private LocalDateTime applyTime;

    /**
     * 审批状态 1:待审批 2:已通过 3:已拒绝
     */
    private Integer approveStatus;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 审批意见
     */
    private String approveOpinion;

    /**
     * 审批时间
     */
    private LocalDateTime approveTime;

    /**
     * 执行状态 1:未执行 2:已执行
     */
    private Integer executeStatus;

    /**
     * 执行时间
     */
    private LocalDateTime executeTime;

    /**
     * 附件路径
     */
    private String attachment;
}
