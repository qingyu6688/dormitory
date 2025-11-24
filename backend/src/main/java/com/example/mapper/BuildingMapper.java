package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Building;
import org.apache.ibatis.annotations.Mapper;

/**
 * 宿舍楼Mapper
 */
@Mapper
public interface BuildingMapper extends BaseMapper<Building> {
}
