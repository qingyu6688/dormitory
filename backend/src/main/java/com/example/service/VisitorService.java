package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Visitor;
import com.example.vo.VisitorVO;

import java.util.List;

/**
 * 访客登记Service
 */
public interface VisitorService extends IService<Visitor> {
    
    /**
     * 分页查询访客记录（包含关联信息）
     */
    IPage<VisitorVO> pageWithDetails(Page<Visitor> page, Long visitedStudentId, Long buildingId, Integer status);
    
    /**
     * 获取当前用户的访客记录
     */
    List<VisitorVO> getMyVisitorList();
    
    /**
     * 学生登记访客
     */
    void registerVisitor(Visitor visitor);
}
