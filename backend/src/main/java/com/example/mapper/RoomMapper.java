package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Room;
import org.apache.ibatis.annotations.Mapper;

/**
 * 宿舍Mapper
 */
@Mapper
public interface RoomMapper extends BaseMapper<Room> {
}
