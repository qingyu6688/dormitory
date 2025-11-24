package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.File;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件记录表 Mapper
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {

}
