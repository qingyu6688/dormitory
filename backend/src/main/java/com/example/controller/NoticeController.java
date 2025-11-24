package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.Log;
import com.example.common.Result;
import com.example.entity.Notice;
import com.example.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知公告控制器
 */
@Tag(name = "通知公告", description = "通知公告增删改查接口")
@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Operation(summary = "分页查询通知公告")
    @GetMapping("/page")
    public Result<IPage<Notice>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "标题") @RequestParam(required = false) String title,
            @Parameter(description = "通知类型") @RequestParam(required = false) String noticeType,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        Page<Notice> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(title)) {
            wrapper.like(Notice::getTitle, title);
        }
        if (StringUtils.hasText(noticeType)) {
            wrapper.eq(Notice::getNoticeType, noticeType);
        }
        if (status != null) {
            wrapper.eq(Notice::getStatus, status);
        }
        
        wrapper.orderByDesc(Notice::getIsTop, Notice::getPublishTime);
        IPage<Notice> result = noticeService.page(page, wrapper);
        return Result.success(result);
    }

    @Operation(summary = "查询所有通知公告")
    @GetMapping("/list")
    public Result<List<Notice>> list() {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getStatus, 2); // 只查询已发布的
        wrapper.orderByDesc(Notice::getIsTop, Notice::getPublishTime);
        List<Notice> list = noticeService.list(wrapper);
        return Result.success(list);
    }

    @Operation(summary = "根据ID查询通知公告")
    @GetMapping("/{id}")
    public Result<Notice> getById(@PathVariable Long id) {
        Notice notice = noticeService.getById(id);
        return Result.success(notice);
    }

    @Log("发布通知公告")
    @Operation(summary = "发布通知公告")
    @PostMapping
    public Result<Void> save(@RequestBody Notice notice) {
        noticeService.save(notice);
        return Result.success();
    }

    @Log("更新通知公告")
    @Operation(summary = "更新通知公告")
    @PutMapping
    public Result<Void> update(@RequestBody Notice notice) {
        noticeService.updateById(notice);
        return Result.success();
    }

    @Log("删除通知公告")
    @Operation(summary = "删除通知公告")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        noticeService.removeById(id);
        return Result.success();
    }
}
