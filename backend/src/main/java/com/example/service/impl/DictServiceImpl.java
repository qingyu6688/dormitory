package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Dict;
import com.example.mapper.DictMapper;
import com.example.service.DictService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典服务实现类
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    public List<Dict> getByType(String type) {
        LambdaQueryWrapper<Dict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dict::getType, type);
        wrapper.orderByAsc(Dict::getSort);
        return this.list(wrapper);
    }
}
