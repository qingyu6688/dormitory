package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Repair;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报修管理Mapper
 */
@Mapper
public interface RepairMapper extends BaseMapper<Repair> {
}
