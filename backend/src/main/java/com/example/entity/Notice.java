package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知公告表
 */
@Data
@TableName("dorm_notice")
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容(富文本)
     */
    private String content;

    /**
     * 通知类型
     */
    private String noticeType;

    /**
     * 发布人ID
     */
    private Long publisherId;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 生效时间
     */
    private LocalDateTime effectiveTime;

    /**
     * 失效时间
     */
    private LocalDateTime expireTime;

    /**
     * 目标范围
     */
    private String targetScope;

    /**
     * 目标ID列表(JSON数组)
     */
    private String targetIds;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 是否需要确认阅读
     */
    private Integer needConfirm;

    /**
     * 已读人数
     */
    private Integer readCount;

    /**
     * 附件
     */
    private String attachment;

    /**
     * 状态 1:草稿 2:已发布 3:已撤回
     */
    private Integer status;
}
