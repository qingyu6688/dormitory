package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Transfer;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调宿申请Mapper
 */
@Mapper
public interface TransferMapper extends BaseMapper<Transfer> {
}
