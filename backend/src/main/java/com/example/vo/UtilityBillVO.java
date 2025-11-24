package com.example.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 水电费账单视图对象
 */
@Data
public class UtilityBillVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long roomId;
    private String roomNo;
    private String billMonth;
    private BigDecimal electricUsage;
    private BigDecimal waterUsage;
    private BigDecimal electricAmount;
    private BigDecimal waterAmount;
    private BigDecimal totalAmount;
    private BigDecimal subsidy;
    private BigDecimal actualAmount;
    private Integer paymentStatus;
    private LocalDateTime paymentTime;
    private String paymentMethod;
    private String remark;
}
