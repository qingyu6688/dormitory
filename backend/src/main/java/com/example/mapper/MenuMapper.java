package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单权限表 Mapper
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

}
