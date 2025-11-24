package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.CheckIn;
import org.apache.ibatis.annotations.Mapper;

/**
 * 入住记录Mapper
 */
@Mapper
public interface CheckInMapper extends BaseMapper<CheckIn> {
}
