package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Building;
import com.example.vo.BuildingVO;

/**
 * 宿舍楼Service
 */
public interface BuildingService extends IService<Building> {
    
    /**
     * 分页查询宿舍楼（包含宿管员姓名）
     */
    IPage<BuildingVO> pageWithDetails(Page<Building> page, String buildingNo, String buildingName, Integer genderType, Integer status);
    
    /**
     * 保存宿舍楼并自动生成房间
     */
    void saveWithRooms(Building building);
}
