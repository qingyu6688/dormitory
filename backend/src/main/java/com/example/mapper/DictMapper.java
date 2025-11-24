package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Dict;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典表 Mapper
 */
@Mapper
public interface DictMapper extends BaseMapper<Dict> {

}
