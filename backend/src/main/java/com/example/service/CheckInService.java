package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.CheckIn;
import com.example.vo.CheckInVO;

/**
 * 入住记录Service
 */
public interface CheckInService extends IService<CheckIn> {
    
    /**
     * 分页查询入住记录（包含关联信息）
     */
    IPage<CheckInVO> pageWithDetails(Page<CheckIn> page, String studentNo, String studentName, String roomNo, Integer status);
    
    /**
     * 管理员分配宿舍
     */
    void assignRoom(Long studentId, Long roomId, Integer bedNo);
}
