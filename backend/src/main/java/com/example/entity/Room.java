package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 宿舍表
 */
@Data
@TableName("dorm_room")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 宿舍楼ID
     */
    private Long buildingId;

    /**
     * 房间号
     */
    private String roomNo;

    /**
     * 楼层
     */
    private Integer floor;

    /**
     * 床位数
     */
    private Integer bedCount;

    /**
     * 已入住人数
     */
    private Integer occupiedCount;

    /**
     * 房间类型
     */
    private String roomType;

    /**
     * 房间状态 1:空闲 2:部分入住 3:已满 4:维修中
     */
    private Integer roomStatus;

    /**
     * 朝向
     */
    private String direction;

    /**
     * 面积
     */
    private BigDecimal area;

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
