package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Hygiene;
import org.apache.ibatis.annotations.Mapper;

/**
 * 卫生检查Mapper
 */
@Mapper
public interface HygieneMapper extends BaseMapper<Hygiene> {
}
