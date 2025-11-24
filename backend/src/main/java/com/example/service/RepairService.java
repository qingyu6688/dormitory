package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Repair;
import com.example.vo.RepairVO;

import java.util.List;

/**
 * 报修管理Service
 */
public interface RepairService extends IService<Repair> {
    
    /**
     * 分页查询报修记录（包含关联信息）
     */
    IPage<RepairVO> pageWithDetails(Page<Repair> page, String reporterNo, String reporterName, String roomNo, String repairType, Integer status);
    
    /**
     * 获取当前用户的报修列表
     */
    List<RepairVO> getMyRepairList();
    
    /**
     * 学生提交报修
     */
    void submitRepair(Repair repair);
    
    /**
     * 获取报修类型列表
     */
    List<String> getRepairTypes();
    
    /**
     * 根据ID查询报修详情（包含关联信息）
     */
    RepairVO getRepairDetail(Long id);
}
