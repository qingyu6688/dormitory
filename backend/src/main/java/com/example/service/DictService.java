package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Dict;

import java.util.List;

/**
 * 字典服务接口
 */
public interface DictService extends IService<Dict> {

    /**
     * 根据字典类型获取字典列表
     */
    List<Dict> getByType(String type);
}
