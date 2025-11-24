package com.example.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 我的宿舍视图对象
 */
@Data
public class MyRoomVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 宿舍ID
     */
    private Long id;

    /**
     * 楼栋ID
     */
    private Long buildingId;

    /**
     * 楼栋名称
     */
    private String buildingName;

    /**
     * 楼栋编号
     */
    private String buildingNo;

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
    private Integer capacity;

    /**
     * 已入住人数
     */
    private Integer currentCount;

    /**
     * 我的床位号
     */
    private Integer bedNo;

    /**
     * 入住日期
     */
    private LocalDate checkInDate;

    /**
     * 房间类型
     */
    private String roomType;

    /**
     * 朝向
     */
    private String direction;

    /**
     * 房间状态 1:空闲 2:部分入住 3:已满 4:维修中
     */
    private Integer roomStatus;
}
