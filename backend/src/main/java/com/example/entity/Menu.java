package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单权限表
 */
@Data
@TableName("sys_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 路由名称 (Vue Route Name)
     */
    private String name;

    /**
     * 菜单标题 (显示名称)
     */
    private String title;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 组件路径 (views/...)
     */
    private String component;

    /**
     * 图标 (Element Plus Icon)
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sortNum;

    /**
     * 类型 1:菜单 2:按钮
     */
    private Integer type;

    /**
     * 权限标识 (user:list)
     */
    private String permKey;

    /**
     * 子菜单列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<Menu> children;
}
