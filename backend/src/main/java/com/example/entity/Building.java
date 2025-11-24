package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 宿舍楼表
 */
@Data
@TableName("dorm_building")
public class Building implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 楼栋编号
     */
    private String buildingNo;

    /**
     * 楼栋名称
     */
    private String buildingName;

    /**
     * 楼层数
     */
    private Integer floorCount;

    /**
     * 每层房间数
     */
    private Integer roomPerFloor;

    /**
     * 性别限制 1:男 2:女
     */
    private Integer genderType;

    /**
     * 宿管员ID
     */
    private Long managerId;

    /**
     * 地址
     */
    private String address;

    /**
     * 建筑年份
     */
    private Integer buildYear;

    /**
     * 状态 1:启用 2:停用 3:维修中
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

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
