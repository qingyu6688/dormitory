/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50711 (5.7.11)
 Source Host           : localhost:3306
 Source Schema         : admin_system

 Target Server Type    : MySQL
 Target Server Version : 50711 (5.7.11)
 File Encoding         : 65001

 Date: 26/10/2025 14:09:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(4) NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', '性别男', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', '性别女', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', '性别未知', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', '显示菜单', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', '隐藏菜单', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', '正常状态', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', '停用状态', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (8, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', '系统默认是', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (9, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', '系统默认否', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (10, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', '通知', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (11, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', '公告', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (12, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', '正常状态', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (13, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', '关闭状态', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (14, 1, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', '其他操作', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (15, 2, '新增', '1', 'sys_oper_type', '', 'success', 'N', '0', '新增操作', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (16, 3, '修改', '2', 'sys_oper_type', '', 'primary', 'N', '0', '修改操作', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (17, 4, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', '删除操作', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (18, 5, '查询', '4', 'sys_oper_type', '', '', 'N', '0', '查询操作', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (19, 6, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', '导出操作', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_data` VALUES (20, 7, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', '导入操作', '2025-10-26 13:57:03', '2025-10-26 13:57:03');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', '用户性别列表', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', '菜单状态列表', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', '系统开关列表', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', '任务状态列表', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_type` VALUES (5, '系统是否', 'sys_yes_no', '0', '系统是否列表', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_type` VALUES (6, '通知类型', 'sys_notice_type', '0', '通知类型列表', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_type` VALUES (7, '通知状态', 'sys_notice_status', '0', '通知状态列表', '2025-10-26 13:57:03', '2025-10-26 13:57:03');
INSERT INTO `sys_dict_type` VALUES (8, '操作类型', 'sys_oper_type', '0', '操作类型列表', '2025-10-26 13:57:03', '2025-10-26 13:57:03');

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名称',
  `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原始文件名',
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件路径',
  `file_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件URL',
  `file_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `file_size` bigint(20) NULL DEFAULT 0 COMMENT '文件大小（字节）',
  `file_ext` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件扩展名',
  `upload_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上传用户',
  `upload_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_file_type`(`file_type`) USING BTREE,
  INDEX `idx_upload_user`(`upload_user`) USING BTREE,
  INDEX `idx_upload_time`(`upload_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file
-- ----------------------------
INSERT INTO `sys_file` VALUES (1, '1e5be0bf3ba74341be31167a3ffec9a2.png', 'Snipaste_2024-10-17_22-21-19.png', 'F:/桌面/SpringBoot+Vue/uploads/2025/10/26\\1e5be0bf3ba74341be31167a3ffec9a2.png', '/uploads/2025/10/26/1e5be0bf3ba74341be31167a3ffec9a2.png', 'image', 90498, 'png', 'admin', '2025-10-26 14:01:37', NULL);
INSERT INTO `sys_file` VALUES (2, '55d24a1117dd4b6dba72178eeb77133a.png', 'Snipaste_2024-10-28_22-24-11.png', 'F:/桌面/SpringBoot+Vue/uploads/2025/10/26\\55d24a1117dd4b6dba72178eeb77133a.png', '/uploads/2025/10/26/55d24a1117dd4b6dba72178eeb77133a.png', 'image', 2565786, 'png', 'admin', '2025-10-26 14:01:41', NULL);
INSERT INTO `sys_file` VALUES (3, 'b96d049c34cc497caf6acc1021c5569a.png', 'Snipaste_2024-10-18_00-17-37.png', 'F:/桌面/SpringBoot+Vue/uploads/2025/10/26\\b96d049c34cc497caf6acc1021c5569a.png', '/uploads/2025/10/26/b96d049c34cc497caf6acc1021c5569a.png', 'image', 9430, 'png', 'admin', '2025-10-26 14:02:09', NULL);
INSERT INTO `sys_file` VALUES (4, '843622b1ea2942258d75f6a65877e66d.jpg', '6161F95FFC6BA3DD8077BE1C84D089D4.jpg', 'F:/桌面/SpringBoot+Vue/uploads/2025/10/26\\843622b1ea2942258d75f6a65877e66d.jpg', '/uploads/2025/10/26/843622b1ea2942258d75f6a65877e66d.jpg', 'image', 876087, 'jpg', 'admin', '2025-10-26 14:02:17', NULL);

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户账号',
  `ip_address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '登录状态（1成功 0失败）',
  `message` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_username`(`username`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_login_time`(`login_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
INSERT INTO `sys_login_log` VALUES (1, 1, 'admin', '127.0.0.1', '本地', 'Chrome', 'Windows 10', 1, '登录成功', '2025-10-26 14:00:34');
INSERT INTO `sys_login_log` VALUES (2, 1, 'admin', '127.0.0.1', '本地', 'Chrome', 'Windows 10', 1, '登录成功', '2025-10-26 14:02:27');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(4) NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `is_frame` int(1) NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int(1) NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1016 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, '/system', NULL, 1, 0, 'M', '0', '0', '', 'Setting', '系统管理目录', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 2, '/monitor', NULL, 1, 0, 'M', '0', '0', '', 'Monitor', '系统监控目录', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, '/system/user', '/admin/user', 1, 0, 'C', '0', '0', 'system:user:list', 'User', '用户管理菜单', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, '/system/role', '/admin/role', 1, 0, 'C', '0', '0', 'system:role:list', 'UserFilled', '角色管理菜单', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, '/system/menu', '/admin/menu', 1, 0, 'C', '0', '0', 'system:menu:list', 'Menu', '菜单管理菜单', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1005, '重置密码', 100, 6, '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1006, '角色查询', 101, 1, '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1007, '角色新增', 101, 2, '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1008, '角色修改', 101, 3, '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1009, '角色删除', 101, 4, '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1010, '角色导出', 101, 5, '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1011, '分配权限', 101, 6, '', '', 1, 0, 'F', '0', '0', 'system:role:auth', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', '', '2025-10-26 13:56:20', '2025-10-26 13:56:20');

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作用户名',
  `operation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作描述',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '方法名称',
  `params` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求参数',
  `time` bigint(20) NULL DEFAULT 0 COMMENT '耗时(毫秒)',
  `ip_address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'IP地址',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作地点',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '操作状态（1成功 0失败）',
  `error_msg` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '错误消息',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_username`(`username`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
INSERT INTO `sys_operation_log` VALUES (1, 1, 'admin', '用户管理-修改', 'com.example.admin.controller.UserController.update()', '{\"id\":1}', 17, '127.0.0.1', '本地', 1, '', '2025-10-26 14:02:10');
INSERT INTO `sys_operation_log` VALUES (2, 1, 'admin', '用户管理-修改', 'com.example.admin.controller.UserController.update()', '{\"id\":1}', 4, '127.0.0.1', '本地', 1, '', '2025-10-26 14:02:18');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色标识',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `status` int(1) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_key`(`role_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', 'admin', '系统管理员，拥有所有权限', 1, '2025-10-26 13:56:03', '2025-10-26 13:56:03');
INSERT INTO `sys_role` VALUES (2, '普通用户', 'user', '普通用户，只能访问前台页面', 1, '2025-10-26 13:56:03', '2025-10-26 13:56:03');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1);
INSERT INTO `sys_role_menu` VALUES (1, 2);
INSERT INTO `sys_role_menu` VALUES (1, 100);
INSERT INTO `sys_role_menu` VALUES (1, 101);
INSERT INTO `sys_role_menu` VALUES (1, 102);
INSERT INTO `sys_role_menu` VALUES (1, 1000);
INSERT INTO `sys_role_menu` VALUES (1, 1001);
INSERT INTO `sys_role_menu` VALUES (1, 1002);
INSERT INTO `sys_role_menu` VALUES (1, 1003);
INSERT INTO `sys_role_menu` VALUES (1, 1004);
INSERT INTO `sys_role_menu` VALUES (1, 1005);
INSERT INTO `sys_role_menu` VALUES (1, 1006);
INSERT INTO `sys_role_menu` VALUES (1, 1007);
INSERT INTO `sys_role_menu` VALUES (1, 1008);
INSERT INTO `sys_role_menu` VALUES (1, 1009);
INSERT INTO `sys_role_menu` VALUES (1, 1010);
INSERT INTO `sys_role_menu` VALUES (1, 1011);
INSERT INTO `sys_role_menu` VALUES (1, 1012);
INSERT INTO `sys_role_menu` VALUES (1, 1013);
INSERT INTO `sys_role_menu` VALUES (1, 1014);
INSERT INTO `sys_role_menu` VALUES (1, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 1000);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `avatar` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '头像(Base64或URL)',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `status` int(1) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `deleted` int(1) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 'admin@example.com', '13800138000', '/uploads/2025/10/26/843622b1ea2942258d75f6a65877e66d.jpg', 1, 1, 0, '2025-10-26 13:56:03', '2025-10-26 14:02:18');
INSERT INTO `sys_user` VALUES (2, 'user1', 'e10adc3949ba59abbe56e057f20f883e', '用户1', 'user1@example.com', '13800138001', NULL, 2, 1, 0, '2025-10-26 13:56:03', '2025-10-26 13:56:03');
INSERT INTO `sys_user` VALUES (3, 'user2', 'e10adc3949ba59abbe56e057f20f883e', '用户2', 'user2@example.com', '13800138002', NULL, 2, 1, 0, '2025-10-26 13:56:03', '2025-10-26 13:56:03');

SET FOREIGN_KEY_CHECKS = 1;
