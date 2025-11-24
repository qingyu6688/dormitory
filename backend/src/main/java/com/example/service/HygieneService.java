package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Hygiene;
import com.example.vo.HygieneVO;

/**
 * 卫生检查Service
 */
public interface HygieneService extends IService<Hygiene> {
    
    IPage<HygieneVO> pageWithDetails(Page<Hygiene> page, String roomNo, String checkType, Integer recheckStatus);
    
    /**
     * 获取当前用户宿舍的卫生评分
     */
    Object getMyScore();
}
