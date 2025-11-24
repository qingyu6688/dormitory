package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.File;

/**
 * 文件服务接口
 */
public interface FileService extends IService<File> {

    /**
     * 根据MD5查询文件
     */
    File getByMd5(String md5);
}
