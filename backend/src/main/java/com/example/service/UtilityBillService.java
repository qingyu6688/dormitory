package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.UtilityBill;
import com.example.vo.UtilityBillVO;

import java.util.List;

/**
 * 水电费Service
 */
public interface UtilityBillService extends IService<UtilityBill> {
    
    IPage<UtilityBillVO> pageWithDetails(Page<UtilityBill> page, String roomNo, String billMonth, Integer paymentStatus);
    
    /**
     * 获取当前用户的水电费账单
     */
    List<UtilityBillVO> getMyBills();
    
    /**
     * 学生缴费
     */
    void payBill(Long id, String paymentMethod);
}
