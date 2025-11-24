package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 访客登记表
 */
@Data
@TableName("dorm_visitor")
public class Visitor implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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
     * 宿舍楼ID
     */
    private Long buildingId;

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
