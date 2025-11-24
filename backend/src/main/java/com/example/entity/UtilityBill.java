package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 水电费表
 */
@Data
@TableName("dorm_utility_bill")
public class UtilityBill implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 宿舍ID
     */
    private Long roomId;

    /**
     * 账单月份 YYYY-MM
     */
    private String billMonth;

    /**
     * 电表起始读数
     */
    private BigDecimal electricStart;

    /**
     * 电表结束读数
     */
    private BigDecimal electricEnd;

    /**
     * 用电量
     */
    private BigDecimal electricUsage;

    /**
     * 电费单价
     */
    private BigDecimal electricPrice;

    /**
     * 电费金额
     */
    private BigDecimal electricAmount;

    /**
     * 水表起始读数
     */
    private BigDecimal waterStart;

    /**
     * 水表结束读数
     */
    private BigDecimal waterEnd;

    /**
     * 用水量
     */
    private BigDecimal waterUsage;

    /**
     * 水费单价
     */
    private BigDecimal waterPrice;

    /**
     * 水费金额
     */
    private BigDecimal waterAmount;

    /**
     * 总费用
     */
    private BigDecimal totalAmount;

    /**
     * 免费电量额度
     */
    private BigDecimal freeQuotaElectric;

    /**
     * 免费水量额度
     */
    private BigDecimal freeQuotaWater;

    /**
     * 实际应缴费用
     */
    private BigDecimal actualAmount;

    /**
     * 缴费状态 1:未缴 2:已缴 3:逾期
     */
    private Integer paymentStatus;

    /**
     * 缴费时间
     */
    private LocalDateTime paymentTime;

    /**
     * 缴费方式
     */
    private String paymentMethod;

    /**
     * 录入人ID
     */
    private Long recorderId;

    /**
     * 抄表时间
     */
    private LocalDateTime meterReadingTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
