package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.annotation.Log;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.entity.User;
import com.example.exception.ServiceException;
import com.example.service.UserService;
import com.example.util.PasswordUtil;
import com.example.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 */
@Tag(name = "用户管理", description = "用户增删改查接口")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "分页查询用户列表（包含宿舍信息）", description = "支持用户名、学号、学院、角色、状态筛选")
    @GetMapping("/page")
    public Result<IPage<UserVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "用户名（模糊查询）") @RequestParam(required = false) String username,
            @Parameter(description = "学号（模糊查询）") @RequestParam(required = false) String studentNo,
            @Parameter(description = "学院（模糊查询）") @RequestParam(required = false) String college,
            @Parameter(description = "角色ID") @RequestParam(required = false) Long roleId,
            @Parameter(description = "状态 1:启用 0:禁用") @RequestParam(required = false) Integer status) {
        
        Page<User> page = new Page<>(pageNum, pageSize);
        IPage<UserVO> result = userService.pageWithDetails(page, username, studentNo, college, roleId, status);
        
        return Result.success(result);
    }

    @Operation(summary = "查询所有用户")
    @GetMapping("/list")
    public Result<List<User>> list() {
        List<User> list = userService.list();
        // 清空密码字段
        list.forEach(user -> user.setPassword(null));
        return Result.success(list);
    }

    @Operation(summary = "根据ID查询用户详情（包含宿舍信息）")
    @GetMapping("/{id}")
    public Result<UserVO> getById(@Parameter(description = "用户ID") @PathVariable Long id) {
        UserVO userVO = userService.getUserDetail(id);
        if (userVO == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        return Result.success(userVO);
    }

    @Log("新增用户")
    @Operation(summary = "新增用户")
    @PostMapping
    public Result<Void> add(@RequestBody User user) {
        // 检查用户名是否已存在
        User existUser = userService.getByUsername(user.getUsername());
        if (existUser != null) {
            throw new ServiceException(ResultCode.USER_ALREADY_EXIST);
        }
        
        // 加密密码（如果有密码）
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(PasswordUtil.encrypt(user.getPassword()));
        } else {
            // 默认密码123456
            user.setPassword(PasswordUtil.encrypt("123456"));
        }
        
        boolean success = userService.save(user);
        return success ? Result.success() : Result.error("新增失败");
    }

    @Log("修改用户")
    @Operation(summary = "修改用户")
    @PutMapping
    public Result<Void> update(@RequestBody User user) {
        if (user.getId() == null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "用户ID不能为空");
        }
        
        User existUser = userService.getById(user.getId());
        if (existUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        
        // 如果修改了用户名，检查新用户名是否已存在
        if (!existUser.getUsername().equals(user.getUsername())) {
            User checkUser = userService.getByUsername(user.getUsername());
            if (checkUser != null) {
                throw new ServiceException(ResultCode.USER_ALREADY_EXIST);
            }
        }
        
        boolean success = userService.updateById(user);
        return success ? Result.success() : Result.error("修改失败");
    }

    @Log("删除用户")
    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "用户ID") @PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        
        boolean success = userService.removeById(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    @Log("批量删除用户")
    @Operation(summary = "批量删除用户")
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "请选择要删除的用户");
        }
        
        boolean success = userService.removeByIds(ids);
        return success ? Result.success() : Result.error("批量删除失败");
    }

    @Log("修改用户状态")
    @Operation(summary = "修改用户状态")
    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "状态 1:启用 0:禁用") @RequestParam Integer status) {
        User user = userService.getById(id);
        if (user == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        
        user.setStatus(status);
        boolean success = userService.updateById(user);
        return success ? Result.success() : Result.error("状态修改失败");
    }

    @Operation(summary = "重置密码", description = "将用户密码重置为默认密码123456")
    @PutMapping("/resetPassword/{id}")
    public Result<Void> resetPassword(@Parameter(description = "用户ID") @PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        
        // 重置为默认密码（加密）
        user.setPassword(PasswordUtil.encrypt("123456"));
        boolean success = userService.updateById(user);
        return success ? Result.success() : Result.error("密码重置失败");
    }

    /**
     * 更新用户头像
     */
    @Operation(summary = "更新用户头像")
    @PutMapping("/avatar/{id}")
    public Result<Void> updateAvatar(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "头像URL") @RequestParam String avatar) {
        User user = userService.getById(id);
        if (user == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        
        user.setAvatar(avatar);
        boolean success = userService.updateById(user);
        return success ? Result.success() : Result.error("头像更新失败");
    }

    /**
     * 修改密码
     */
    @Operation(summary = "修改密码")
    @PutMapping("/password/{id}")
    public Result<Void> updatePassword(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "原密码") @RequestParam String oldPassword,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        User user = userService.getById(id);
        if (user == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        
        // 验证原密码
        if (!PasswordUtil.verify(oldPassword, user.getPassword())) {
            throw new ServiceException(ResultCode.PASSWORD_ERROR, "原密码错误");
        }
        
        // 加密新密码并更新
        user.setPassword(PasswordUtil.encrypt(newPassword));
        boolean success = userService.updateById(user);
        return success ? Result.success() : Result.error("密码修改失败");
    }
}
