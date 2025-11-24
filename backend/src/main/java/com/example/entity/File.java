package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件记录表
 */
@Data
@TableName("sys_file")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件大小(kb)
     */
    private Long size;

    /**
     * 下载链接
     */
    private String url;

    /**
     * 文件MD5(防重复上传)
     */
    private String md5;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 是否启用
     */
    private Integer enable;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
