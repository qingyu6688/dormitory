package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.Log;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.entity.Dict;
import com.example.exception.ServiceException;
import com.example.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典管理控制器
 */
@RestController
@RequestMapping("/api/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    /**
     * 分页查询字典列表
     */
    @GetMapping("/page")
    public Result<IPage<Dict>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type) {
        
        Page<Dict> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Dict> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(name)) {
            wrapper.like(Dict::getName, name);
        }
        if (StringUtils.hasText(type)) {
            wrapper.eq(Dict::getType, type);
        }
        
        wrapper.orderByAsc(Dict::getSort);
        IPage<Dict> result = dictService.page(page, wrapper);
        
        return Result.success(result);
    }

    /**
     * 查询所有字典
     */
    @GetMapping("/list")
    public Result<List<Dict>> list() {
        LambdaQueryWrapper<Dict> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Dict::getSort);
        List<Dict> list = dictService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 根据类型查询字典
     */
    @GetMapping("/type/{type}")
    public Result<List<Dict>> getByType(@PathVariable String type) {
        List<Dict> list = dictService.getByType(type);
        return Result.success(list);
    }

    /**
     * 根据ID查询字典
     */
    @GetMapping("/{id}")
    public Result<Dict> getById(@PathVariable Long id) {
        Dict dict = dictService.getById(id);
        if (dict == null) {
            throw new ServiceException(ResultCode.DATA_NOT_EXIST, "字典不存在");
        }
        return Result.success(dict);
    }

    /**
     * 新增字典
     */
    @Log("新增字典")
    @PostMapping
    public Result<Void> add(@RequestBody Dict dict) {
        boolean success = dictService.save(dict);
        return success ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改字典
     */
    @Log("修改字典")
    @PutMapping
    public Result<Void> update(@RequestBody Dict dict) {
        if (dict.getId() == null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "字典ID不能为空");
        }
        
        Dict existDict = dictService.getById(dict.getId());
        if (existDict == null) {
            throw new ServiceException(ResultCode.DATA_NOT_EXIST, "字典不存在");
        }
        
        boolean success = dictService.updateById(dict);
        return success ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除字典
     */
    @Log("删除字典")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Dict dict = dictService.getById(id);
        if (dict == null) {
            throw new ServiceException(ResultCode.DATA_NOT_EXIST, "字典不存在");
        }
        
        boolean success = dictService.removeById(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    /**
     * 批量删除字典
     */
    @Log("批量删除字典")
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "请选择要删除的字典");
        }
        
        boolean success = dictService.removeByIds(ids);
        return success ? Result.success() : Result.error("批量删除失败");
    }
}
