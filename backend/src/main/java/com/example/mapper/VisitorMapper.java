package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Visitor;
import org.apache.ibatis.annotations.Mapper;

/**
 * 访客登记Mapper
 */
@Mapper
public interface VisitorMapper extends BaseMapper<Visitor> {
}
