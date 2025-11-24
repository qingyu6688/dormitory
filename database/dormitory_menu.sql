/*
 宿舍管理系统菜单权限配置
 Date: 24/11/2025
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 宿舍管理菜单数据
-- ----------------------------

-- 一级菜单：宿舍管理
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `path`, `component`, `icon`, `sort_num`, `type`, `perm_key`) 
VALUES (20, NULL, 'Dormitory', '宿舍管理', '/dormitory', 'Layout', 'School', 300, 1, NULL);

-- 二级菜单：基础管理
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `path`, `component`, `icon`, `sort_num`, `type`, `perm_key`) 
VALUES 
(21, 20, 'Building', '宿舍楼管理', 'building', 'dormitory/building/index', 'OfficeBuilding', 301, 1, 'dorm:building:list'),
(22, 20, 'Room', '宿舍管理', 'room', 'dormitory/room/index', 'House', 302, 1, 'dorm:room:list'),
(23, 20, 'CheckIn', '入住管理', 'checkin', 'dormitory/checkin/index', 'UserFilled', 303, 1, 'dorm:checkin:list');

-- 二级菜单：业务管理
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `path`, `component`, `icon`, `sort_num`, `type`, `perm_key`) 
VALUES 
(24, 20, 'Transfer', '调宿申请', 'transfer', 'dormitory/transfer/index', 'Switch', 304, 1, 'dorm:transfer:list'),
(25, 20, 'Repair', '报修管理', 'repair', 'dormitory/repair/index', 'Tools', 305, 1, 'dorm:repair:list'),
(26, 20, 'Visitor', '访客登记', 'visitor', 'dormitory/visitor/index', 'Postcard', 306, 1, 'dorm:visitor:list'),
(27, 20, 'Hygiene', '卫生检查', 'hygiene', 'dormitory/hygiene/index', 'MagicStick', 307, 1, 'dorm:hygiene:list'),
(28, 20, 'UtilityBill', '水电费管理', 'utilitybill', 'dormitory/utilitybill/index', 'Coin', 308, 1, 'dorm:bill:list'),
(29, 20, 'Violation', '违纪记录', 'violation', 'dormitory/violation/index', 'Warning', 309, 1, 'dorm:violation:list'),
(30, 20, 'Notice', '通知公告', 'notice', 'dormitory/notice/index', 'Bell', 310, 1, 'dorm:notice:list');

-- 二级菜单：统计分析
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `path`, `component`, `icon`, `sort_num`, `type`, `perm_key`) 
VALUES (31, 20, 'Statistics', '统计分析', 'statistics', 'dormitory/statistics/index', 'DataAnalysis', 311, 1, 'dorm:statistics:view');

-- ----------------------------
-- 三级按钮权限
-- ----------------------------

-- 宿舍楼管理按钮
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `path`, `component`, `icon`, `sort_num`, `type`, `perm_key`) 
VALUES 
(32, 21, NULL, '新增宿舍楼', NULL, NULL, NULL, 1, 2, 'dorm:building:add'),
(33, 21, NULL, '编辑宿舍楼', NULL, NULL, NULL, 2, 2, 'dorm:building:edit'),
(34, 21, NULL, '删除宿舍楼', NULL, NULL, NULL, 3, 2, 'dorm:building:delete');

-- 宿舍管理按钮
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `path`, `component`, `icon`, `sort_num`, `type`, `perm_key`) 
VALUES 
(35, 22, NULL, '新增宿舍', NULL, NULL, NULL, 1, 2, 'dorm:room:add'),
(36, 22, NULL, '编辑宿舍', NULL, NULL, NULL, 2, 2, 'dorm:room:edit'),
(37, 22, NULL, '删除宿舍', NULL, NULL, NULL, 3, 2, 'dorm:room:delete'),
(38, 22, NULL, '批量导入', NULL, NULL, NULL, 4, 2, 'dorm:room:import');

-- 入住管理按钮
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `path`, `component`, `icon`, `sort_num`, `type`, `perm_key`) 
VALUES 
(39, 23, NULL, '办理入住', NULL, NULL, NULL, 1, 2, 'dorm:checkin:add'),
(40, 23, NULL, '办理退宿', NULL, NULL, NULL, 2, 2, 'dorm:checkin:checkout'),
(41, 23, NULL, '编辑入住信息', NULL, NULL, NULL, 3, 2, 'dorm:checkin:edit'),
(42, 23, NULL, '删除入住记录', NULL, NULL, NULL, 4, 2, 'dorm:checkin:delete');

-- 调宿申请按钮
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `path`, `component`, `icon`, `sort_num`, `type`, `perm_key`) 
VALUES 
(43, 24, NULL, '提交申请', NULL, NULL, NULL, 1, 2, 'dorm:transfer:add'),
(44, 24, NULL, '审批申请', NULL, NULL, NULL, 2, 2, 'dorm:transfer:approve'),
(45, 24, NULL, '执行调宿', NULL, NULL, NULL, 3, 2, 'dorm:transfer:execute'),
(46, 24, NULL, '撤销申请', NULL, NULL, NULL, 4, 2, 'dorm:transfer:cancel');

-- 报修管理按钮
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `path`, `component`, `icon`, `sort_num`, `type`, `perm_key`) 
VALUES 
(47, 25, NULL, '提交报修', NULL, NULL, NULL, 1, 2, 'dorm:repair:add'),
(48, 25, NULL, '处理报修', NULL, NULL, NULL, 2, 2, 'dorm:repair:handle'),
(49, 25, NULL, '完成报修', NULL, NULL, NULL, 3, 2, 'dorm:repair:complete'),
(50, 25, NULL, '删除报修', NULL, NULL, NULL, 4, 2, 'dorm:repair:delete');

-- 访客登记按钮
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `path`, `component`, `icon`, `sort_num`, `type`, `perm_key`) 
VALUES 
(51, 26, NULL, '新增登记', NULL, NULL, NULL, 1, 2, 'dorm:visitor:add'),
(52, 26, NULL, '登记离开', NULL, NULL, NULL, 2, 2, 'dorm:visitor:leave'),
(53, 26, NULL, '编辑登记', NULL, NULL, NULL, 3, 2, 'dorm:visitor:edit'),
(54, 26, NULL, '删除登记', NULL, NULL, NULL, 4, 2, 'dorm:visitor:delete');

-- 卫生检查按钮
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `path`, `component`, `icon`, `sort_num`, `type`, `perm_key`) 
VALUES 
(55, 27, NULL, '新增检查', NULL, NULL, NULL, 1, 2, 'dorm:hygiene:add'),
(56, 27, NULL, '编辑检查', NULL, NULL, NULL, 2, 2, 'dorm:hygiene:edit'),
(57, 27, NULL, '复查', NULL, NULL, NULL, 3, 2, 'dorm:hygiene:recheck'),
(58, 27, NULL, '删除检查', NULL, NULL, NULL, 4, 2, 'dorm:hygiene:delete');

-- 水电费管理按钮
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `path`, `component`, `icon`, `sort_num`, `type`, `perm_key`) 
VALUES 
(59, 28, NULL, '录入账单', NULL, NULL, NULL, 1, 2, 'dorm:bill:add'),
(60, 28, NULL, '编辑账单', NULL, NULL, NULL, 2, 2, 'dorm:bill:edit'),
(61, 28, NULL, '缴费', NULL, NULL, NULL, 3, 2, 'dorm:bill:pay'),
(62, 28, NULL, '删除账单', NULL, NULL, NULL, 4, 2, 'dorm:bill:delete'),
(63, 28, NULL, '批量导入', NULL, NULL, NULL, 5, 2, 'dorm:bill:import');

-- 违纪记录按钮
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `path`, `component`, `icon`, `sort_num`, `type`, `perm_key`) 
VALUES 
(64, 29, NULL, '新增违纪', NULL, NULL, NULL, 1, 2, 'dorm:violation:add'),
(65, 29, NULL, '编辑违纪', NULL, NULL, NULL, 2, 2, 'dorm:violation:edit'),
(66, 29, NULL, '处理违纪', NULL, NULL, NULL, 3, 2, 'dorm:violation:handle'),
(67, 29, NULL, '删除违纪', NULL, NULL, NULL, 4, 2, 'dorm:violation:delete');

-- 通知公告按钮
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `title`, `path`, `component`, `icon`, `sort_num`, `type`, `perm_key`) 
VALUES 
(68, 30, NULL, '发布通知', NULL, NULL, NULL, 1, 2, 'dorm:notice:add'),
(69, 30, NULL, '编辑通知', NULL, NULL, NULL, 2, 2, 'dorm:notice:edit'),
(70, 30, NULL, '撤回通知', NULL, NULL, NULL, 3, 2, 'dorm:notice:revoke'),
(71, 30, NULL, '删除通知', NULL, NULL, NULL, 4, 2, 'dorm:notice:delete');

-- ----------------------------
-- 角色菜单关联（超级管理员拥有所有权限）
-- ----------------------------
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) 
SELECT 1, id FROM `sys_menu` WHERE id >= 20 AND id <= 71;

-- ----------------------------
-- 宿管员权限配置（role_id=3）
-- ----------------------------
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
-- 宿舍管理主菜单
(3, 20),
-- 基础管理
(3, 21), (3, 32), (3, 33), (3, 34),  -- 宿舍楼管理（全部权限）
(3, 22), (3, 35), (3, 36), (3, 37), (3, 38),  -- 宿舍管理（全部权限）
(3, 23), (3, 39), (3, 40), (3, 41), (3, 42),  -- 入住管理（全部权限）
-- 业务管理
(3, 24), (3, 44), (3, 45),  -- 调宿申请（审批、执行）
(3, 25), (3, 48), (3, 49),  -- 报修管理（处理、完成）
(3, 26), (3, 51), (3, 52), (3, 53), (3, 54),  -- 访客登记（全部权限）
(3, 27), (3, 55), (3, 56), (3, 57), (3, 58),  -- 卫生检查（全部权限）
(3, 28), (3, 59), (3, 60), (3, 61), (3, 62), (3, 63),  -- 水电费管理（全部权限）
(3, 29), (3, 64), (3, 65), (3, 66), (3, 67),  -- 违纪记录（全部权限）
(3, 30), (3, 68), (3, 69), (3, 70), (3, 71),  -- 通知公告（全部权限）
-- 统计分析
(3, 31);

-- ----------------------------
-- 学生权限配置（role_id=4）
-- ----------------------------
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
-- 宿舍管理主菜单
(4, 20),
-- 基础管理（仅查看）
(4, 22),  -- 宿舍管理（查看）
(4, 23),  -- 入住管理（查看个人）
-- 业务管理
(4, 24), (4, 43), (4, 46),  -- 调宿申请（提交、撤销）
(4, 25), (4, 47),  -- 报修管理（提交）
(4, 28),  -- 水电费管理（查看）
(4, 29),  -- 违纪记录（查看个人）
(4, 30);  -- 通知公告（查看）

-- ----------------------------
-- 辅导员权限配置（role_id=5）
-- ----------------------------
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
-- 宿舍管理主菜单
(5, 20),
-- 基础管理（查看）
(5, 21),  -- 宿舍楼管理（查看）
(5, 22),  -- 宿舍管理（查看）
(5, 23),  -- 入住管理（查看）
-- 业务管理
(5, 24), (5, 44),  -- 调宿申请（审批）
(5, 25),  -- 报修管理（查看）
(5, 26),  -- 访客登记（查看）
(5, 27),  -- 卫生检查（查看）
(5, 28),  -- 水电费管理（查看）
(5, 29), (5, 64), (5, 65), (5, 66),  -- 违纪记录（管理）
(5, 30),  -- 通知公告（查看）
-- 统计分析
(5, 31);

SET FOREIGN_KEY_CHECKS = 1;
