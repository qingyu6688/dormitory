package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.Log;
import com.example.common.Result;
import com.example.entity.Hygiene;
import com.example.service.HygieneService;
import com.example.vo.HygieneVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 卫生检查控制器
 */
@Tag(name = "卫生检查", description = "卫生检查增删改查接口")
@RestController
@RequestMapping("/api/hygiene")
public class HygieneController {

    @Autowired
    private HygieneService hygieneService;

    @Operation(summary = "分页查询卫生检查记录")
    @GetMapping("/page")
    public Result<IPage<HygieneVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "房间号") @RequestParam(required = false) String roomNo,
            @Parameter(description = "检查类型") @RequestParam(required = false) String checkType,
            @Parameter(description = "复查状态") @RequestParam(required = false) Integer recheckStatus) {
        
        Page<Hygiene> page = new Page<>(pageNum, pageSize);
        IPage<HygieneVO> result = hygieneService.pageWithDetails(page, roomNo, checkType, recheckStatus);
        return Result.success(result);
    }

/**
 * 查询所有卫生检查记录的接口方法
 * 使用@GetMapping映射HTTP GET请求，"/list"为请求路径
 * @Operation注解用于API文档，表明这是一个查询所有卫生检查记录的操作
 *
 * @return 返回一个Result对象，包含所有卫生检查记录的列表
 */
    @Operation(summary = "查询所有卫生检查记录")
    @GetMapping("/list")
    public Result<List<Hygiene>> list() {
    // 调用hygieneService的list方法获取所有卫生检查记录
        List<Hygiene> list = hygieneService.list();
    // 将查询结果封装到Result对象中并返回
        return Result.success(list);
    }

/**
 * 根据ID查询卫生检查记录
 * 该接口用于通过指定的ID获取卫生检查记录信息
 *
 * @param id 卫生检查记录的唯一标识符
 * @return 返回一个Result对象，其中包含查询到的卫生检查记录数据
 */
    @Operation(summary = "根据ID查询卫生检查记录")  // API文档注解，描述接口功能
    @GetMapping("/{id}")  // HTTP GET请求映射，将请求映射到/{id}路径
    public Result<Hygiene> getById(@PathVariable Long id) {  // 方法定义，接收路径变量id
        Hygiene hygiene = hygieneService.getById(id);  // 调用服务层方法获取卫生检查记录
        return Result.success(hygiene);  // 返回成功响应，包含查询到的卫生检查记录
    }

/**
 * 新增卫生检查接口
 * 使用@Log注解记录操作日志，标记为"新增卫生检查"
 * 使用@Operation注解标记API操作，摘要为"新增卫生检查"
 *
 * @param hygiene 卫生检查实体对象，通过请求体传递
 * @return 返回操作结果，成功时返回success
 */
    @Log("新增卫生检查")
    @Operation(summary = "新增卫生检查")
    @PostMapping
    public Result<Void> save(@RequestBody Hygiene hygiene) {
    // 调用卫生检查服务层保存方法，传入卫生检查对象
        hygieneService.save(hygiene);
    // 返回成功操作结果
        return Result.success();
    }

/**
 * 更新卫生检查记录的接口方法
 * 使用@Log注解记录操作日志，使用@Operation注解记录接口描述信息
 *
 * @param hygiene 包含卫生检查记录信息的请求体对象
 * @return 返回操作结果，成功时返回成功状态
 */
    @Log("更新卫生检查记录")
    @Operation(summary = "更新卫生检查记录")
    @PutMapping
    public Result<Void> update(@RequestBody Hygiene hygiene) {
    // 调用服务层方法更新卫生检查记录
        hygieneService.updateById(hygiene);
    // 返回成功操作结果
        return Result.success();
    }

/**
 * 删除卫生检查记录接口
 * 使用@Log注解记录操作日志
 * 使用@Operation注解标注接口功能为删除卫生检查记录
 * 通过HTTP DELETE方法接收请求
 *
 * @param id 卫生检查记录的唯一标识ID
 * @return 返回操作结果，成功时返回success
 */
    @Log("删除卫生检查记录")    // 记录删除操作日志
    @Operation(summary = "删除卫生检查记录")  // 接口功能描述
    @DeleteMapping("/{id}")    // 定义DELETE请求映射，路径中包含id参数
    public Result<Void> delete(@PathVariable Long id) {  // 方法参数id从路径变量中获取
        hygieneService.removeById(id);  // 调用服务层方法根据ID删除记录
        return Result.success();  // 返回成功结果
    }

    @Operation(summary = "获取我的宿舍卫生评分")
    @GetMapping("/my-score")
    public Result<?> getMyScore() {
        return Result.success(hygieneService.getMyScore());
    }
}
