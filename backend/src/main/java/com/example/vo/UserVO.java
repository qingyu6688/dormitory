package com.example.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户VO - 包含关联信息
 */
@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP地址
     */
    private String lastLoginIp;

    /**
     * 状态 1:启用 0:禁用
     */
    private Integer status;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 性别 1:男 2:女
     */
    private Integer gender;

    /**
     * 性别文本
     */
    private String genderText;

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
     * 年级
     */
    private Integer grade;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 家长电话
     */
    private String parentPhone;

    /**
     * 紧急联系人
     */
    private String emergencyContact;

    /**
     * 紧急联系电话
     */
    private String emergencyPhone;

    /**
     * 学生状态 1:在校 2:休学 3:毕业 4:退学
     */
    private Integer studentStatus;

    /**
     * 学生状态文本
     */
    private String studentStatusText;

    // ========== 宿舍相关信息 ==========

    /**
     * 是否已分配宿舍
     */
    private Boolean hasRoom;

    /**
     * 宿舍楼ID
     */
    private Long buildingId;

    /**
     * 宿舍楼名称
     */
    private String buildingName;

    /**
     * 宿舍楼编号
     */
    private String buildingNo;

    /**
     * 房间ID
     */
    private Long roomId;

    /**
     * 房间号
     */
    private String roomNo;

    /**
     * 楼层
     */
    private Integer floor;

    /**
     * 床位号
     */
    private Integer bedNo;

    /**
     * 入住时间
     */
    private LocalDateTime checkInDate;

    /**
     * 入住状态 1:已入住 2:已退宿
     */
    private Integer checkInStatus;

    /**
     * 押金状态 1:未交 2:已交 3:已退
     */
    private Integer depositStatus;

    /**
     * 押金金额
     */
    private java.math.BigDecimal depositAmount;

    /**
     * 室友数量
     */
    private Integer roommateCount;
}
