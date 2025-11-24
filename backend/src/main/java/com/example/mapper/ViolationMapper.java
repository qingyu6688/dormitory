package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Violation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 违纪记录Mapper
 */
@Mapper
public interface ViolationMapper extends BaseMapper<Violation> {
}
