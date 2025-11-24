package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Violation;
import com.example.vo.ViolationVO;

/**
 * 违纪记录Service
 */
public interface ViolationService extends IService<Violation> {
    
    IPage<ViolationVO> pageWithDetails(Page<Violation> page, String studentNo, String roomNo, String violationType, Integer handleStatus);
}
