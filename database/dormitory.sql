/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50711 (5.7.11)
 Source Host           : localhost:3306
 Source Schema         : dormitory

 Target Server Type    : MySQL
 Target Server Version : 50711 (5.7.11)
 File Encoding         : 65001

 Date: 24/11/2025 22:12:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dorm_building
-- ----------------------------
DROP TABLE IF EXISTS `dorm_building`;
CREATE TABLE `dorm_building`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `building_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '楼栋编号',
  `building_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '楼栋名称',
  `floor_count` int(11) NOT NULL COMMENT '楼层数',
  `room_per_floor` int(11) NOT NULL COMMENT '每层房间数',
  `gender_type` tinyint(1) NOT NULL COMMENT '性别限制 1:男 2:女',
  `manager_id` bigint(20) NULL DEFAULT NULL COMMENT '宿管员ID',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `build_year` int(11) NULL DEFAULT NULL COMMENT '建筑年份',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态 1:启用 2:停用 3:维修中',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_building_no`(`building_no`) USING BTREE,
  INDEX `idx_manager`(`manager_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '宿舍楼表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dorm_building
-- ----------------------------
INSERT INTO `dorm_building` VALUES (1, 'A1', 'A1号男生宿舍楼', 6, 20, 1, 2, '校园东区', 2021, 1, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_building` VALUES (2, 'A2', 'A2号男生宿舍楼', 6, 20, 1, 2, '校园东区', 2021, 1, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_building` VALUES (3, 'B1', '西1号女生宿舍楼', 6, 50, 2, 2, '校园西区', 2021, 1, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_building` VALUES (4, 'B2', '西2号女生宿舍楼', 6, 20, 2, 2, '校园西区', 2021, 1, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_building` VALUES (5, 'A3', 'A3男生宿舍楼', 6, 50, 1, NULL, '', 2021, 1, '', '2025-11-24 10:00:00', '2025-11-24 10:00:00');

-- ----------------------------
-- Table structure for dorm_check_in
-- ----------------------------
DROP TABLE IF EXISTS `dorm_check_in`;
CREATE TABLE `dorm_check_in`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `student_id` bigint(20) NOT NULL COMMENT '学生ID',
  `room_id` bigint(20) NOT NULL COMMENT '宿舍ID',
  `bed_no` int(11) NOT NULL COMMENT '床位号',
  `check_in_date` date NOT NULL COMMENT '入住日期',
  `plan_check_out_date` date NULL DEFAULT NULL COMMENT '计划退宿日期',
  `actual_check_out_date` date NULL DEFAULT NULL COMMENT '实际退宿日期',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '1:已入住 2:已退宿',
  `deposit_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '租金',
  `deposit_status` tinyint(1) NULL DEFAULT NULL COMMENT '1:未交 2:已交 3:已退',
  `key_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '钥匙编号',
  `operator_id` bigint(20) NULL DEFAULT NULL COMMENT '办理人ID',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_student`(`student_id`) USING BTREE,
  INDEX `idx_room`(`room_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '入住记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dorm_check_in
-- ----------------------------
INSERT INTO `dorm_check_in` VALUES (1, 4, 1, 1, '2021-09-01', '2025-07-01', NULL, 1, 200.00, 2, 'A1-101-1', 2, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_check_in` VALUES (2, 5, 1, 2, '2021-09-01', '2025-07-01', NULL, 1, 200.00, 2, 'A1-101-2', 2, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_check_in` VALUES (3, 6, 2, 1, '2021-09-01', '2025-07-01', NULL, 1, 200.00, 2, 'A1-102-1', 2, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_check_in` VALUES (4, 7, 2, 2, '2021-09-01', '2025-07-01', NULL, 1, 200.00, 2, 'A1-102-2', 2, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_check_in` VALUES (5, 8, 6, 1, '2021-09-01', '2025-07-01', NULL, 1, 200.00, 2, 'B1-101-1', 2, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_check_in` VALUES (6, 9, 6, 2, '2021-09-01', '2025-07-01', NULL, 1, 200.00, 2, 'B1-101-2', 2, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_check_in` VALUES (7, 10, 7, 1, '2021-09-01', '2025-07-01', NULL, 1, 200.00, 2, 'B1-102-1', 2, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_check_in` VALUES (8, 11, 7, 2, '2021-09-01', '2025-07-01', NULL, 1, 200.00, 2, 'B1-102-2', 2, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');

-- ----------------------------
-- Table structure for dorm_hygiene
-- ----------------------------
DROP TABLE IF EXISTS `dorm_hygiene`;
CREATE TABLE `dorm_hygiene`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `check_date` date NOT NULL COMMENT '检查日期',
  `check_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '检查类型',
  `room_id` bigint(20) NOT NULL COMMENT '宿舍ID',
  `checker_id` bigint(20) NOT NULL COMMENT '检查人ID',
  `score` int(11) NOT NULL COMMENT '0-100分',
  `grade` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优秀/良好/合格/不合格',
  `deduct_items` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '扣分项',
  `bonus_items` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '加分项',
  `images` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '检查照片',
  `problem_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '问题描述',
  `rectify_requirement` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '整改要求',
  `rectify_deadline` date NULL DEFAULT NULL COMMENT '整改期限',
  `recheck_status` tinyint(1) NULL DEFAULT 1 COMMENT '1:无需复查 2:待复查 3:已复查',
  `recheck_time` datetime NULL DEFAULT NULL COMMENT '复查时间',
  `recheck_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '复查结果',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_room`(`room_id`) USING BTREE,
  INDEX `idx_date`(`check_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '卫生检查表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dorm_hygiene
-- ----------------------------
INSERT INTO `dorm_hygiene` VALUES (1, '2025-11-18', '周检查', 1, 2, 85, '良好', NULL, NULL, NULL, '地面有少许灰尘', NULL, NULL, 1, NULL, NULL, '2025-11-24 10:00:00');
INSERT INTO `dorm_hygiene` VALUES (2, '2025-11-18', '周检查', 2, 2, 92, '优秀', NULL, NULL, NULL, '整体卫生良好', NULL, NULL, 1, NULL, NULL, '2025-11-24 10:00:00');
INSERT INTO `dorm_hygiene` VALUES (3, '2025-11-18', '周检查', 6, 2, 78, '合格', NULL, NULL, NULL, '物品摆放较乱', '请整理好物品', '2025-11-25', 2, NULL, NULL, '2025-11-24 10:00:00');
INSERT INTO `dorm_hygiene` VALUES (4, '2025-11-18', '周检查', 7, 2, 88, '良好', NULL, NULL, NULL, '卫生状况良好', NULL, NULL, 1, NULL, NULL, '2025-11-24 10:00:00');

-- ----------------------------
-- Table structure for dorm_notice
-- ----------------------------
DROP TABLE IF EXISTS `dorm_notice`;
CREATE TABLE `dorm_notice`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `notice_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `publisher_id` bigint(20) NOT NULL,
  `publish_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `effective_time` datetime NULL DEFAULT NULL,
  `expire_time` datetime NULL DEFAULT NULL,
  `target_scope` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `target_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `is_top` tinyint(1) NULL DEFAULT 0,
  `need_confirm` tinyint(1) NULL DEFAULT 0,
  `read_count` int(11) NULL DEFAULT 0,
  `attachment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '1:草稿 2:已发布 3:已撤回',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_publisher`(`publisher_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dorm_notice
-- ----------------------------
INSERT INTO `dorm_notice` VALUES (1, '关于加强宿舍安全管理的通知', '<p>各位同学：</p><p>为确保宿舍安全，请勿使用大功率电器，注意用电安全。</p>', '系统通知', 2, '2025-11-20 08:00:00', '2025-11-20 08:00:00', NULL, '全体', NULL, 1, 0, 0, NULL, 2);
INSERT INTO `dorm_notice` VALUES (2, '本周末停水通知', '<p>本周六上午8:00-12:00进行水管维修，期间停水，请提前做好准备。</p>', '停水停电', 2, '2025-11-22 08:00:00', '2025-11-22 08:00:00', '2025-11-23 12:00:00', '全体', NULL, 1, 0, 0, NULL, 2);
INSERT INTO `dorm_notice` VALUES (3, '宿舍卫生检查通知', '<p>本周五下午进行宿舍卫生检查，请各宿舍做好准备。</p>', '活动通知', 2, '2025-11-21 08:00:00', '2025-11-21 08:00:00', NULL, '全体', NULL, 0, 0, 0, NULL, 2);

-- ----------------------------
-- Table structure for dorm_notice_read
-- ----------------------------
DROP TABLE IF EXISTS `dorm_notice_read`;
CREATE TABLE `dorm_notice_read`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `notice_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `read_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_notice_user`(`notice_id`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知阅读记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dorm_notice_read
-- ----------------------------

-- ----------------------------
-- Table structure for dorm_repair
-- ----------------------------
DROP TABLE IF EXISTS `dorm_repair`;
CREATE TABLE `dorm_repair`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reporter_id` bigint(20) NOT NULL,
  `room_id` bigint(20) NOT NULL,
  `repair_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `images` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `report_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `urgency_level` tinyint(1) NULL DEFAULT 1 COMMENT '1:普通 2:紧急 3:特急',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '1:待处理 2:处理中 3:已完成',
  `assigned_to` bigint(20) NULL DEFAULT NULL,
  `appointment_time` datetime NULL DEFAULT NULL,
  `process_time` datetime NULL DEFAULT NULL,
  `complete_time` datetime NULL DEFAULT NULL,
  `repair_cost` decimal(10, 2) NULL DEFAULT NULL,
  `cost_bearer` tinyint(1) NULL DEFAULT NULL COMMENT '1:学校 2:学生',
  `result_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `satisfaction` int(11) NULL DEFAULT NULL COMMENT '1-5星',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_reporter`(`reporter_id`) USING BTREE,
  INDEX `idx_room`(`room_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报修管理表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dorm_repair
-- ----------------------------
INSERT INTO `dorm_repair` VALUES (1, 4, 1, '水电', '宿舍灯管坏了，需要更换', NULL, '2025-11-20 10:30:00', 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `dorm_repair` VALUES (2, 8, 6, '门窗', '宿舍门锁有点松动', NULL, '2025-11-21 14:20:00', 2, 2, 2, '2025-11-22 10:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for dorm_room
-- ----------------------------
DROP TABLE IF EXISTS `dorm_room`;
CREATE TABLE `dorm_room`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `building_id` bigint(20) NOT NULL COMMENT '宿舍楼ID',
  `room_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '房间号',
  `floor` int(11) NOT NULL COMMENT '楼层',
  `bed_count` int(11) NOT NULL COMMENT '床位数',
  `occupied_count` int(11) NULL DEFAULT 0 COMMENT '已入住人数',
  `room_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '房间类型',
  `room_status` tinyint(1) NULL DEFAULT 1 COMMENT '1:空闲 2:部分入住 3:已满 4:维修中',
  `direction` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '朝向',
  `area` decimal(10, 2) NULL DEFAULT NULL COMMENT '面积',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_building_room`(`building_id`, `room_no`) USING BTREE,
  INDEX `idx_status`(`room_status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '宿舍表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dorm_room
-- ----------------------------
INSERT INTO `dorm_room` VALUES (1, 1, '101', 1, 4, 2, '普通', 2, '南', 20.00, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_room` VALUES (2, 1, '102', 1, 4, 2, '普通', 2, '南', 20.00, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_room` VALUES (3, 1, '201', 2, 4, 0, '空调', 1, '南', 20.00, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_room` VALUES (4, 1, '202', 2, 4, 0, '空调', 1, '南', 20.00, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_room` VALUES (5, 2, '101', 1, 4, 0, '独卫', 1, '南', 22.00, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_room` VALUES (6, 3, '101', 1, 4, 2, '普通', 2, '南', 20.00, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_room` VALUES (7, 3, '102', 1, 4, 2, '普通', 2, '南', 20.00, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_room` VALUES (8, 3, '201', 2, 4, 0, '空调', 1, '南', 20.00, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');
INSERT INTO `dorm_room` VALUES (9, 4, '101', 1, 4, 0, '独卫', 1, '南', 22.00, NULL, '2025-11-24 10:00:00', '2025-11-24 10:00:00');

-- ----------------------------
-- Table structure for dorm_transfer
-- ----------------------------
DROP TABLE IF EXISTS `dorm_transfer`;
CREATE TABLE `dorm_transfer`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `student_id` bigint(20) NOT NULL,
  `current_room_id` bigint(20) NOT NULL,
  `target_room_id` bigint(20) NOT NULL,
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `apply_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `approve_status` tinyint(1) NULL DEFAULT 1 COMMENT '1:待审批 2:已通过 3:已拒绝',
  `approver_id` bigint(20) NULL DEFAULT NULL,
  `approve_opinion` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `approve_time` datetime NULL DEFAULT NULL,
  `execute_status` tinyint(1) NULL DEFAULT 1 COMMENT '1:未执行 2:已执行',
  `execute_time` datetime NULL DEFAULT NULL,
  `attachment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_student`(`student_id`) USING BTREE,
  INDEX `idx_status`(`approve_status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '调宿申请表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dorm_transfer
-- ----------------------------

-- ----------------------------
-- Table structure for dorm_utility_bill
-- ----------------------------
DROP TABLE IF EXISTS `dorm_utility_bill`;
CREATE TABLE `dorm_utility_bill`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `room_id` bigint(20) NOT NULL,
  `bill_month` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'YYYY-MM',
  `electric_start` decimal(10, 2) NULL DEFAULT NULL,
  `electric_end` decimal(10, 2) NULL DEFAULT NULL,
  `electric_usage` decimal(10, 2) NULL DEFAULT NULL,
  `electric_price` decimal(10, 2) NULL DEFAULT NULL,
  `electric_amount` decimal(10, 2) NULL DEFAULT NULL,
  `water_start` decimal(10, 2) NULL DEFAULT NULL,
  `water_end` decimal(10, 2) NULL DEFAULT NULL,
  `water_usage` decimal(10, 2) NULL DEFAULT NULL,
  `water_price` decimal(10, 2) NULL DEFAULT NULL,
  `water_amount` decimal(10, 2) NULL DEFAULT NULL,
  `total_amount` decimal(10, 2) NULL DEFAULT NULL,
  `free_quota_electric` decimal(10, 2) NULL DEFAULT NULL,
  `free_quota_water` decimal(10, 2) NULL DEFAULT NULL,
  `actual_amount` decimal(10, 2) NULL DEFAULT NULL,
  `payment_status` tinyint(1) NULL DEFAULT 1 COMMENT '1:未缴 2:已缴 3:逾期',
  `payment_time` datetime NULL DEFAULT NULL,
  `payment_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `recorder_id` bigint(20) NULL DEFAULT NULL,
  `meter_reading_time` datetime NULL DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_room_month`(`room_id`, `bill_month`) USING BTREE,
  INDEX `idx_status`(`payment_status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '水电费表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dorm_utility_bill
-- ----------------------------
INSERT INTO `dorm_utility_bill` VALUES (1, 1, '2025-10', 1000.00, 1045.00, 45.00, 0.60, 27.00, 50.00, 56.00, 6.00, 3.00, 18.00, 45.00, 30.00, 5.00, 12.00, 2, '2025-11-01 09:00:00', '支付宝', 2, '2025-11-01 09:00:00', NULL, '2025-11-24 10:00:00');
INSERT INTO `dorm_utility_bill` VALUES (2, 2, '2025-10', 980.00, 1020.00, 40.00, 0.60, 24.00, 48.00, 53.00, 5.00, 3.00, 15.00, 39.00, 30.00, 5.00, 6.00, 2, '2025-11-01 10:00:00', '微信', 2, '2025-11-01 09:10:00', NULL, '2025-11-24 10:00:00');
INSERT INTO `dorm_utility_bill` VALUES (3, 6, '2025-10', 0.00, 0.00, 38.00, 0.80, 22.80, 0.00, 0.00, 6.50, 3.00, 19.50, 42.30, 30.00, 5.00, 9.30, 3, NULL, NULL, 2, '2025-11-01 10:00:00', NULL, '2025-11-24 10:00:00');
INSERT INTO `dorm_utility_bill` VALUES (4, 7, '2025-10', 1150.00, 1185.00, 35.00, 0.60, 21.00, 55.00, 60.00, 5.00, 3.00, 15.00, 36.00, 30.00, 5.00, 3.00, 2, '2025-11-02 15:00:00', '支付宝', 2, '2025-11-01 10:10:00', NULL, '2025-11-24 10:00:00');

-- ----------------------------
-- Table structure for dorm_violation
-- ----------------------------
DROP TABLE IF EXISTS `dorm_violation`;
CREATE TABLE `dorm_violation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `student_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学生ID,逗号分隔',
  `room_id` bigint(20) NOT NULL COMMENT '宿舍ID',
  `violation_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '违纪类型',
  `violation_time` datetime NOT NULL COMMENT '违纪时间',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '违纪描述',
  `evidence` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '证据材料',
  `handle_method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理方式',
  `deduct_score` int(11) NULL DEFAULT NULL COMMENT '扣分',
  `recorder_id` bigint(20) NULL DEFAULT NULL COMMENT '记录人ID',
  `notify_parent` tinyint(1) NULL DEFAULT 0 COMMENT '是否通知家长',
  `notify_teacher` tinyint(1) NULL DEFAULT 0 COMMENT '是否通知辅导员',
  `handle_status` tinyint(1) NULL DEFAULT 1 COMMENT '1:待处理 2:已处理',
  `handle_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_room`(`room_id`) USING BTREE,
  INDEX `idx_time`(`violation_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '违纪记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dorm_violation
-- ----------------------------
INSERT INTO `dorm_violation` VALUES (1, '4', 1, '晚归', '2025-11-15 23:30:00', '23:30返回宿舍，超过门禁时间', NULL, '警告', 6, 2, 0, 0, 2, '', NULL, '2025-11-24 10:00:00');

-- ----------------------------
-- Table structure for dorm_visitor
-- ----------------------------
DROP TABLE IF EXISTS `dorm_visitor`;
CREATE TABLE `dorm_visitor`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `visitor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访客姓名',
  `visitor_gender` tinyint(1) NULL DEFAULT NULL COMMENT '访客性别',
  `visitor_id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访客身份证号',
  `visitor_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访客电话',
  `visited_student_id` bigint(20) NOT NULL COMMENT '被访学生ID',
  `building_id` bigint(20) NOT NULL COMMENT '宿舍楼ID',
  `visit_reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来访事由',
  `enter_time` datetime NOT NULL COMMENT '进入时间',
  `plan_leave_time` datetime NULL DEFAULT NULL COMMENT '预计离开时间',
  `actual_leave_time` datetime NULL DEFAULT NULL COMMENT '实际离开时间',
  `registrar_id` bigint(20) NULL DEFAULT NULL COMMENT '登记人ID',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '1:在楼内 2:已离开',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_student`(`visited_student_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '访客登记表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dorm_visitor
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称 (如: 性别)',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型 (如: sys_user_sex)',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典码 (如: 1)',
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典值 (如: 男)',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, '用户性别', 'sys_user_sex', '1', '男', 1, NULL);
INSERT INTO `sys_dict` VALUES (2, '用户性别', 'sys_user_sex', '2', '女', 2, NULL);
INSERT INTO `sys_dict` VALUES (3, '用户性别', 'sys_user_sex', '0', '未知', 3, NULL);

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `size` bigint(20) NULL DEFAULT NULL COMMENT '文件大小(kb)',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下载链接',
  `md5` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件MD5(防重复上传)',
  `is_delete` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  `enable` tinyint(1) NULL DEFAULT 1 COMMENT '是否启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_file
-- ----------------------------
INSERT INTO `sys_file` VALUES (10, '6161F95FFC6BA3DD8077BE1C84D089D4.jpg', 'jpg', 855, 'http://localhost:8080/91a7cba90f534bfe81abd12d5419331e.jpg', 'cbd9f9cbd4f1ca29b9c640bbf253b845', 0, 1, '2025-11-24 18:45:18');
INSERT INTO `sys_file` VALUES (11, '2.jpg', 'jpg', 99, 'http://localhost:8080/9e52972909304577a4e567fe30377ad6.jpg', 'd4f493c2235ea4868154dbad5fa3d794', 0, 1, '2025-11-24 18:45:21');
INSERT INTO `sys_file` VALUES (12, 'logo.PNG', 'PNG', 459, 'http://localhost:8080/8c6d083e4112492bbf66e72365816fae.PNG', 'e687c9de50f567096603361c8760dfa7', 0, 1, '2025-11-24 21:10:17');
INSERT INTO `sys_file` VALUES (13, '1.jpg', 'jpg', 139, 'http://localhost:8080/2451139a4c7d408c953b5a38ce2db559.jpg', '56ae1386d5bc645293d3a8fa88e07282', 0, 1, '2025-11-24 22:03:26');

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_id` bigint(20) NULL DEFAULT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户账号',
  `ip_address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `message` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_username`(`username`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_login_time`(`login_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统访问记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
INSERT INTO `sys_login_log` VALUES (6, 1, 'admin', '127.0.0.1', '本地', 'Chrome', 'Windows 10', '1', '登录成功', '2025-11-24 18:35:21');
INSERT INTO `sys_login_log` VALUES (7, 1, 'admin', '127.0.0.1', '本地', 'Chrome', 'Windows 10', '1', '登录成功', '2025-11-24 19:18:39');
INSERT INTO `sys_login_log` VALUES (8, 3, 'wang', '127.0.0.1', '本地', 'Chrome', 'Windows 10', '1', '登录成功', '2025-11-24 21:05:10');
INSERT INTO `sys_login_log` VALUES (9, 3, 'wang', '127.0.0.1', '本地', 'Chrome', 'Windows 10', '1', '登录成功', '2025-11-24 21:10:08');
INSERT INTO `sys_login_log` VALUES (10, 12, 'mrk', '127.0.0.1', '本地', 'Chrome', 'Windows 10', '1', '登录成功', '2025-11-24 21:52:06');
INSERT INTO `sys_login_log` VALUES (11, 13, 'mxx', '127.0.0.1', '本地', 'Chrome', 'Windows 10', '1', '登录成功', '2025-11-24 21:55:50');
INSERT INTO `sys_login_log` VALUES (12, 11, '2021008', '127.0.0.1', '本地', 'Chrome', 'Windows 10', '1', '登录成功', '2025-11-24 21:59:40');
INSERT INTO `sys_login_log` VALUES (13, 1, 'admin', '127.0.0.1', '本地', 'Chrome', 'Windows 10', '1', '登录成功', '2025-11-24 22:02:12');
INSERT INTO `sys_login_log` VALUES (14, 12, 'mrk', '127.0.0.1', '本地', 'Chrome', 'Windows 10', '1', '登录成功', '2025-11-24 22:03:15');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父菜单ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由名称 (Vue Route Name)',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单标题 (显示名称)',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由路径',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径 (views/...)',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标 (Element Plus Icon)',
  `sort_num` int(11) NULL DEFAULT 0 COMMENT '排序',
  `type` tinyint(1) NULL DEFAULT 1 COMMENT '类型 1:菜单 2:按钮',
  `perm_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识 (user:list)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, NULL, 'Dashboard', '首页', '/dashboard', 'dashboard/index', 'HomeFilled', 1, 1, NULL);
INSERT INTO `sys_menu` VALUES (2, NULL, 'System', '系统管理', '/system', 'Layout', 'Setting', 100, 1, NULL);
INSERT INTO `sys_menu` VALUES (3, NULL, 'Data', '数据管理', '/data', 'Layout', 'DataLine', 200, 1, NULL);
INSERT INTO `sys_menu` VALUES (4, 2, 'User', '用户管理', 'user', 'system/user/index', 'User', 101, 1, 'system:user:list');
INSERT INTO `sys_menu` VALUES (5, 2, 'Role', '角色管理', 'role', 'system/role/index', 'Avatar', 102, 1, 'system:role:list');
INSERT INTO `sys_menu` VALUES (6, 2, 'Menu', '菜单管理', 'menu', 'system/menu/index', 'Menu', 103, 1, 'system:menu:list');
INSERT INTO `sys_menu` VALUES (7, 4, NULL, '用户新增', NULL, NULL, NULL, 1, 2, 'system:user:add');
INSERT INTO `sys_menu` VALUES (8, 4, NULL, '用户修改', NULL, NULL, NULL, 2, 2, 'system:user:edit');
INSERT INTO `sys_menu` VALUES (9, 4, NULL, '用户删除', NULL, NULL, NULL, 3, 2, 'system:user:delete');
INSERT INTO `sys_menu` VALUES (10, 3, 'File', '文件管理', 'file', 'data/file/index', 'Folder', 201, 1, 'data:file:list');
INSERT INTO `sys_menu` VALUES (11, 3, 'Dict', '字典管理', 'dict', 'data/dict/index', 'Collection', 202, 1, 'data:dict:list');
INSERT INTO `sys_menu` VALUES (12, NULL, 'Log', '日志管理', '/log', 'Layout', 'Document', 50, 1, NULL);
INSERT INTO `sys_menu` VALUES (13, 12, 'LoginLog', '登录日志', 'login', 'system/log/LoginLog', 'UserFilled', 1, 1, 'log:login:list');
INSERT INTO `sys_menu` VALUES (14, 12, 'OperationLog', '操作日志', 'operation', 'system/log/OperationLog', 'Operation', 2, 1, 'log:operation:list');
INSERT INTO `sys_menu` VALUES (20, NULL, 'Dormitory', '宿舍管理', '/dormitory', 'Layout', 'School', 300, 1, NULL);
INSERT INTO `sys_menu` VALUES (21, 20, 'Building', '宿舍楼管理', 'building', 'dormitory/building/index', 'OfficeBuilding', 301, 1, 'dorm:building:list');
INSERT INTO `sys_menu` VALUES (22, 20, 'Room', '宿舍管理', 'room', 'dormitory/room/index', 'House', 302, 1, 'dorm:room:list');
INSERT INTO `sys_menu` VALUES (23, 20, 'CheckIn', '入住管理', 'checkin', 'dormitory/checkin/index', 'UserFilled', 303, 1, 'dorm:checkin:list');
INSERT INTO `sys_menu` VALUES (24, 20, 'Transfer', '调宿申请', 'transfer', 'dormitory/transfer/index', 'Switch', 304, 1, 'dorm:transfer:list');
INSERT INTO `sys_menu` VALUES (25, 20, 'Repair', '报修管理', 'repair', 'dormitory/repair/index', 'Tools', 305, 1, 'dorm:repair:list');
INSERT INTO `sys_menu` VALUES (26, 20, 'Visitor', '访客登记', 'visitor', 'dormitory/visitor/index', 'Postcard', 306, 1, 'dorm:visitor:list');
INSERT INTO `sys_menu` VALUES (27, 20, 'Hygiene', '卫生检查', 'hygiene', 'dormitory/hygiene/index', 'MagicStick', 307, 1, 'dorm:hygiene:list');
INSERT INTO `sys_menu` VALUES (28, 20, 'UtilityBill', '水电费管理', 'utilitybill', 'dormitory/utilitybill/index', 'Coin', 308, 1, 'dorm:bill:list');
INSERT INTO `sys_menu` VALUES (29, 20, 'Violation', '违纪记录', 'violation', 'dormitory/violation/index', 'Warning', 309, 1, 'dorm:violation:list');
INSERT INTO `sys_menu` VALUES (30, 20, 'Notice', '通知公告', 'notice', 'dormitory/notice/index', 'Bell', 310, 1, 'dorm:notice:list');
INSERT INTO `sys_menu` VALUES (31, 20, 'Statistics', '统计分析', 'statistics', 'dormitory/statistics/index', 'DataAnalysis', 311, 1, 'dorm:statistics:view');
INSERT INTO `sys_menu` VALUES (32, 21, NULL, '新增宿舍楼', NULL, NULL, NULL, 1, 2, 'dorm:building:add');
INSERT INTO `sys_menu` VALUES (33, 21, NULL, '编辑宿舍楼', NULL, NULL, NULL, 2, 2, 'dorm:building:edit');
INSERT INTO `sys_menu` VALUES (34, 21, NULL, '删除宿舍楼', NULL, NULL, NULL, 3, 2, 'dorm:building:delete');
INSERT INTO `sys_menu` VALUES (35, 22, NULL, '新增宿舍', NULL, NULL, NULL, 1, 2, 'dorm:room:add');
INSERT INTO `sys_menu` VALUES (36, 22, NULL, '编辑宿舍', NULL, NULL, NULL, 2, 2, 'dorm:room:edit');
INSERT INTO `sys_menu` VALUES (37, 22, NULL, '删除宿舍', NULL, NULL, NULL, 3, 2, 'dorm:room:delete');
INSERT INTO `sys_menu` VALUES (38, 22, NULL, '批量导入', NULL, NULL, NULL, 4, 2, 'dorm:room:import');
INSERT INTO `sys_menu` VALUES (39, 23, NULL, '办理入住', NULL, NULL, NULL, 1, 2, 'dorm:checkin:add');
INSERT INTO `sys_menu` VALUES (40, 23, NULL, '办理退宿', NULL, NULL, NULL, 2, 2, 'dorm:checkin:checkout');
INSERT INTO `sys_menu` VALUES (41, 23, NULL, '编辑入住信息', NULL, NULL, NULL, 3, 2, 'dorm:checkin:edit');
INSERT INTO `sys_menu` VALUES (42, 23, NULL, '删除入住记录', NULL, NULL, NULL, 4, 2, 'dorm:checkin:delete');
INSERT INTO `sys_menu` VALUES (43, 24, NULL, '提交申请', NULL, NULL, NULL, 1, 2, 'dorm:transfer:add');
INSERT INTO `sys_menu` VALUES (44, 24, NULL, '审批申请', NULL, NULL, NULL, 2, 2, 'dorm:transfer:approve');
INSERT INTO `sys_menu` VALUES (45, 24, NULL, '执行调宿', NULL, NULL, NULL, 3, 2, 'dorm:transfer:execute');
INSERT INTO `sys_menu` VALUES (46, 24, NULL, '撤销申请', NULL, NULL, NULL, 4, 2, 'dorm:transfer:cancel');
INSERT INTO `sys_menu` VALUES (47, 25, NULL, '提交报修', NULL, NULL, NULL, 1, 2, 'dorm:repair:add');
INSERT INTO `sys_menu` VALUES (48, 25, NULL, '处理报修', NULL, NULL, NULL, 2, 2, 'dorm:repair:handle');
INSERT INTO `sys_menu` VALUES (49, 25, NULL, '完成报修', NULL, NULL, NULL, 3, 2, 'dorm:repair:complete');
INSERT INTO `sys_menu` VALUES (50, 25, NULL, '删除报修', NULL, NULL, NULL, 4, 2, 'dorm:repair:delete');
INSERT INTO `sys_menu` VALUES (51, 26, NULL, '新增登记', NULL, NULL, NULL, 1, 2, 'dorm:visitor:add');
INSERT INTO `sys_menu` VALUES (52, 26, NULL, '登记离开', NULL, NULL, NULL, 2, 2, 'dorm:visitor:leave');
INSERT INTO `sys_menu` VALUES (53, 26, NULL, '编辑登记', NULL, NULL, NULL, 3, 2, 'dorm:visitor:edit');
INSERT INTO `sys_menu` VALUES (54, 26, NULL, '删除登记', NULL, NULL, NULL, 4, 2, 'dorm:visitor:delete');
INSERT INTO `sys_menu` VALUES (55, 27, NULL, '新增检查', NULL, NULL, NULL, 1, 2, 'dorm:hygiene:add');
INSERT INTO `sys_menu` VALUES (56, 27, NULL, '编辑检查', NULL, NULL, NULL, 2, 2, 'dorm:hygiene:edit');
INSERT INTO `sys_menu` VALUES (57, 27, NULL, '复查', NULL, NULL, NULL, 3, 2, 'dorm:hygiene:recheck');
INSERT INTO `sys_menu` VALUES (58, 27, NULL, '删除检查', NULL, NULL, NULL, 4, 2, 'dorm:hygiene:delete');
INSERT INTO `sys_menu` VALUES (59, 28, NULL, '录入账单', NULL, NULL, NULL, 1, 2, 'dorm:bill:add');
INSERT INTO `sys_menu` VALUES (60, 28, NULL, '编辑账单', NULL, NULL, NULL, 2, 2, 'dorm:bill:edit');
INSERT INTO `sys_menu` VALUES (61, 28, NULL, '缴费', NULL, NULL, NULL, 3, 2, 'dorm:bill:pay');
INSERT INTO `sys_menu` VALUES (62, 28, NULL, '删除账单', NULL, NULL, NULL, 4, 2, 'dorm:bill:delete');
INSERT INTO `sys_menu` VALUES (63, 28, NULL, '批量导入', NULL, NULL, NULL, 5, 2, 'dorm:bill:import');
INSERT INTO `sys_menu` VALUES (64, 29, NULL, '新增违纪', NULL, NULL, NULL, 1, 2, 'dorm:violation:add');
INSERT INTO `sys_menu` VALUES (65, 29, NULL, '编辑违纪', NULL, NULL, NULL, 2, 2, 'dorm:violation:edit');
INSERT INTO `sys_menu` VALUES (66, 29, NULL, '处理违纪', NULL, NULL, NULL, 3, 2, 'dorm:violation:handle');
INSERT INTO `sys_menu` VALUES (67, 29, NULL, '删除违纪', NULL, NULL, NULL, 4, 2, 'dorm:violation:delete');
INSERT INTO `sys_menu` VALUES (68, 30, NULL, '发布通知', NULL, NULL, NULL, 1, 2, 'dorm:notice:add');
INSERT INTO `sys_menu` VALUES (69, 30, NULL, '编辑通知', NULL, NULL, NULL, 2, 2, 'dorm:notice:edit');
INSERT INTO `sys_menu` VALUES (70, 30, NULL, '撤回通知', NULL, NULL, NULL, 3, 2, 'dorm:notice:revoke');
INSERT INTO `sys_menu` VALUES (71, 30, NULL, '删除通知', NULL, NULL, NULL, 4, 2, 'dorm:notice:delete');

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作用户名',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作类型（增删改查等）',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `time` bigint(20) NULL DEFAULT NULL COMMENT '执行时长(毫秒)',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作IP地址',
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作地点',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '操作状态 1:成功 0:失败',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '错误信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_username`(`username`) USING BTREE,
  INDEX `idx_operation`(`operation`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
INSERT INTO `sys_operation_log` VALUES (1, NULL, 'system', '上传文件', 'com.example.controller.FileController.upload()', '参数解析失败', 48, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:40:20');
INSERT INTO `sys_operation_log` VALUES (2, NULL, 'system', '上传文件', 'com.example.controller.FileController.upload()', '参数解析失败', 63, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:41:56');
INSERT INTO `sys_operation_log` VALUES (3, NULL, 'system', '上传文件', 'com.example.controller.FileController.upload()', '[\"MultipartFile(name=file, size=51347)\"]', 53, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:43:54');
INSERT INTO `sys_operation_log` VALUES (4, NULL, 'system', '上传文件', 'com.example.controller.FileController.upload()', '[\"MultipartFile(name=file, size=101828)\"]', 3, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:44:09');
INSERT INTO `sys_operation_log` VALUES (5, NULL, 'system', '上传文件', 'com.example.controller.FileController.upload()', '[\"MultipartFile(name=file, size=82444)\"]', 3, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:44:12');
INSERT INTO `sys_operation_log` VALUES (6, NULL, 'system', '上传文件', 'com.example.controller.FileController.upload()', '[\"MultipartFile(name=file, size=471007)\"]', 7, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:44:15');
INSERT INTO `sys_operation_log` VALUES (7, NULL, 'system', '上传文件', 'com.example.controller.FileController.upload()', '[\"MultipartFile(name=file, size=179300)\"]', 11, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:44:19');
INSERT INTO `sys_operation_log` VALUES (8, NULL, 'system', '上传文件', 'com.example.controller.FileController.upload()', '[\"MultipartFile(name=file, size=876087)\"]', 12, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:44:21');
INSERT INTO `sys_operation_log` VALUES (9, NULL, 'system', '上传文件', 'com.example.controller.FileController.upload()', '[\"MultipartFile(name=file, size=101828)\"]', 3, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:44:24');
INSERT INTO `sys_operation_log` VALUES (10, NULL, 'system', '上传文件', 'com.example.controller.FileController.upload()', '[\"MultipartFile(name=file, size=101828)\"]', 2, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:44:41');
INSERT INTO `sys_operation_log` VALUES (11, NULL, 'system', '修改用户', 'com.example.controller.UserController.update()', '[{\"id\":2,\"username\":\"dorm_manager\",\"password\":\"\",\"nickname\":\"李宿管\",\"roleId\":3,\"email\":\"manager@qq.com\",\"phone\":\"13900139001\",\"avatar\":\"http://localhost:8080/bda3a3d715f0443083a09995fbe30ebd.jpg\",\"createTime\":null,\"lastLoginTime\":null,\"lastLoginIp\":null,\"status\":1,\"studentNo\":null,\"gender\":null,\"college\":null,\"major\":null,\"className\":null,\"grade\":null,\"idCard\":null,\"parentPhone\":null,\"emergencyContact\":null,\"emergencyPhone\":null,\"studentStatus\":null}]', 7, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:44:42');
INSERT INTO `sys_operation_log` VALUES (12, NULL, 'system', '上传文件', 'com.example.controller.FileController.upload()', '[\"MultipartFile(name=file, size=392)\"]', 7, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:44:46');
INSERT INTO `sys_operation_log` VALUES (13, NULL, 'system', '修改用户', 'com.example.controller.UserController.update()', '[{\"id\":2,\"username\":\"dorm_manager\",\"password\":\"\",\"nickname\":\"李宿管\",\"roleId\":3,\"email\":\"manager@qq.com\",\"phone\":\"13900139001\",\"avatar\":\"http://localhost:8080/bc832e56248846d681beeebf913d8dd1.png\",\"createTime\":null,\"lastLoginTime\":null,\"lastLoginIp\":null,\"status\":1,\"studentNo\":null,\"gender\":null,\"college\":null,\"major\":null,\"className\":null,\"grade\":null,\"idCard\":null,\"parentPhone\":null,\"emergencyContact\":null,\"emergencyPhone\":null,\"studentStatus\":null}]', 6, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:44:46');
INSERT INTO `sys_operation_log` VALUES (14, NULL, 'system', '上传文件', 'com.example.controller.FileController.upload()', '[\"MultipartFile(name=file, size=826224)\"]', 9, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:44:52');
INSERT INTO `sys_operation_log` VALUES (15, NULL, 'system', '修改用户', 'com.example.controller.UserController.update()', '[{\"id\":2,\"username\":\"dorm_manager\",\"password\":\"\",\"nickname\":\"李宿管\",\"roleId\":3,\"email\":\"manager@qq.com\",\"phone\":\"13900139001\",\"avatar\":\"http://localhost:8080/ea48066d638042c0ba2c2820a6f465dd.PNG\",\"createTime\":null,\"lastLoginTime\":null,\"lastLoginIp\":null,\"status\":1,\"studentNo\":null,\"gender\":null,\"college\":null,\"major\":null,\"className\":null,\"grade\":null,\"idCard\":null,\"parentPhone\":null,\"emergencyContact\":null,\"emergencyPhone\":null,\"studentStatus\":null}]', 5, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:44:52');
INSERT INTO `sys_operation_log` VALUES (16, NULL, 'system', '上传文件', 'com.example.controller.FileController.upload()', '[\"MultipartFile(name=file, size=876087)\"]', 10, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:45:18');
INSERT INTO `sys_operation_log` VALUES (17, NULL, 'system', '上传文件', 'com.example.controller.FileController.upload()', '[\"MultipartFile(name=file, size=101828)\"]', 7, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:45:21');
INSERT INTO `sys_operation_log` VALUES (18, NULL, 'system', '分配角色权限', 'com.example.controller.RoleController.updateRoleMenus()', '[1,[20,21,32,33,34,22,35,36,37,38,23,39,40,41,42,24,43,44,45,46,25,47,48,49,50,26,51,52,53,54,27,55,56,57,58,28,59,60,61,62,63,29,64,65,66,67,30,68,69,70,71,31]]', 19, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:46:11');
INSERT INTO `sys_operation_log` VALUES (19, NULL, 'system', '分配角色权限', 'com.example.controller.RoleController.updateRoleMenus()', '[5,[20,21,32,33,34,22,35,36,37,38,23,39,40,41,42,24,43,44,45,46,25,47,48,49,50,26,51,52,53,54,27,55,56,57,58,28,59,60,61,62,63,29,64,65,66,67,30,68,69,70,71,31]]', 9, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:46:16');
INSERT INTO `sys_operation_log` VALUES (20, NULL, 'system', '分配角色权限', 'com.example.controller.RoleController.updateRoleMenus()', '[4,[]]', 5, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:46:21');
INSERT INTO `sys_operation_log` VALUES (21, NULL, 'system', '分配角色权限', 'com.example.controller.RoleController.updateRoleMenus()', '[1,[1,2,4,7,8,9,5,6,3,10,11,20,21,32,33,34,22,35,36,37,38,23,39,40,41,42,24,43,44,45,46,25,47,48,49,50,26,51,52,53,54,27,55,56,57,58,28,59,60,61,62,63,29,64,65,66,67,30,68,69,70,71,31]]', 16, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:50:12');
INSERT INTO `sys_operation_log` VALUES (22, NULL, 'system', '分配角色权限', 'com.example.controller.RoleController.updateRoleMenus()', '[1,[1,12,13,14,2,4,7,8,9,5,6,3,10,11,20,21,32,33,34,22,35,36,37,38,23,39,40,41,42,24,43,44,45,46,25,47,48,49,50,26,51,52,53,54,27,55,56,57,58,28,59,60,61,62,63,29,64,65,66,67,30,68,69,70,71,31]]', 8, '127.0.0.1', '本地', 1, NULL, '2025-11-24 18:59:36');
INSERT INTO `sys_operation_log` VALUES (23, NULL, 'system', '更新宿舍', 'com.example.controller.RoomController.update()', '[{\"id\":1,\"buildingId\":1,\"roomNo\":\"101\",\"floor\":1,\"bedCount\":4,\"occupiedCount\":2,\"roomType\":\"普通\",\"roomStatus\":2,\"direction\":\"南\",\"area\":20,\"remark\":null,\"createTime\":\"2025-11-24T10:00:00\",\"updateTime\":\"2025-11-24T10:00:00\"}]', 39, '127.0.0.1', '本地', 1, NULL, '2025-11-24 19:50:50');
INSERT INTO `sys_operation_log` VALUES (24, NULL, 'system', '更新宿舍楼', 'com.example.controller.BuildingController.update()', '[{\"id\":1,\"buildingNo\":\"A1\",\"buildingName\":\"A1号男生宿舍楼\",\"floorCount\":6,\"roomPerFloor\":20,\"genderType\":1,\"managerId\":2,\"address\":\"校园东区\",\"buildYear\":2015,\"status\":1,\"remark\":null,\"createTime\":\"2025-11-24T10:00:00\",\"updateTime\":\"2025-11-24T10:00:00\"}]', 8, '127.0.0.1', '本地', 1, NULL, '2025-11-24 19:52:43');
INSERT INTO `sys_operation_log` VALUES (25, NULL, 'system', '更新宿舍楼', 'com.example.controller.BuildingController.update()', '[{\"id\":2,\"buildingNo\":\"A2\",\"buildingName\":\"A2号男生宿舍楼\",\"floorCount\":6,\"roomPerFloor\":20,\"genderType\":1,\"managerId\":2,\"address\":\"校园东区\",\"buildYear\":2016,\"status\":1,\"remark\":null,\"createTime\":\"2025-11-24T10:00:00\",\"updateTime\":\"2025-11-24T10:00:00\"}]', 5, '127.0.0.1', '本地', 1, NULL, '2025-11-24 19:52:47');
INSERT INTO `sys_operation_log` VALUES (26, NULL, 'system', '新增宿舍楼', 'com.example.controller.BuildingController.save()', '[{\"id\":null,\"buildingNo\":\"A3\",\"buildingName\":\"A3男生宿舍楼\",\"floorCount\":6,\"roomPerFloor\":50,\"genderType\":1,\"managerId\":null,\"address\":\"\",\"buildYear\":2025,\"status\":1,\"remark\":\"\",\"createTime\":\"2025-11-24T10:00:00\",\"updateTime\":\"2025-11-24T10:00:00\"}]', 6, '127.0.0.1', '本地', 1, NULL, '2025-11-24 19:53:17');
INSERT INTO `sys_operation_log` VALUES (27, NULL, 'system', '更新宿舍楼', 'com.example.controller.BuildingController.update()', '[{\"id\":1,\"buildingNo\":\"A1\",\"buildingName\":\"A1号男生宿舍楼\",\"floorCount\":6,\"roomPerFloor\":20,\"genderType\":1,\"managerId\":2,\"address\":\"校园东区\",\"buildYear\":2021,\"status\":1,\"remark\":null,\"createTime\":\"2025-11-24T10:00:00\",\"updateTime\":\"2025-11-24T10:00:00\"}]', 4, '127.0.0.1', '本地', 1, NULL, '2025-11-24 19:54:19');
INSERT INTO `sys_operation_log` VALUES (28, NULL, 'system', '更新宿舍楼', 'com.example.controller.BuildingController.update()', '[{\"id\":2,\"buildingNo\":\"A2\",\"buildingName\":\"A2号男生宿舍楼\",\"floorCount\":6,\"roomPerFloor\":20,\"genderType\":1,\"managerId\":2,\"address\":\"校园东区\",\"buildYear\":2021,\"status\":1,\"remark\":null,\"createTime\":\"2025-11-24T10:00:00\",\"updateTime\":\"2025-11-24T10:00:00\"}]', 6, '127.0.0.1', '本地', 1, NULL, '2025-11-24 19:54:22');
INSERT INTO `sys_operation_log` VALUES (29, NULL, 'system', '更新宿舍楼', 'com.example.controller.BuildingController.update()', '[{\"id\":3,\"buildingNo\":\"B1\",\"buildingName\":\"1号女生宿舍楼\",\"floorCount\":6,\"roomPerFloor\":20,\"genderType\":2,\"managerId\":2,\"address\":\"校园西区\",\"buildYear\":2021,\"status\":1,\"remark\":null,\"createTime\":\"2025-11-24T10:00:00\",\"updateTime\":\"2025-11-24T10:00:00\"}]', 4, '127.0.0.1', '本地', 1, NULL, '2025-11-24 19:54:26');
INSERT INTO `sys_operation_log` VALUES (30, NULL, 'system', '更新宿舍楼', 'com.example.controller.BuildingController.update()', '[{\"id\":4,\"buildingNo\":\"B2\",\"buildingName\":\"2号女生宿舍楼\",\"floorCount\":6,\"roomPerFloor\":20,\"genderType\":2,\"managerId\":2,\"address\":\"校园西区\",\"buildYear\":2021,\"status\":1,\"remark\":null,\"createTime\":\"2025-11-24T10:00:00\",\"updateTime\":\"2025-11-24T10:00:00\"}]', 4, '127.0.0.1', '本地', 1, NULL, '2025-11-24 19:54:30');
INSERT INTO `sys_operation_log` VALUES (31, NULL, 'system', '更新宿舍楼', 'com.example.controller.BuildingController.update()', '[{\"id\":5,\"buildingNo\":\"A3\",\"buildingName\":\"A3男生宿舍楼\",\"floorCount\":6,\"roomPerFloor\":50,\"genderType\":1,\"managerId\":null,\"address\":\"\",\"buildYear\":2024,\"status\":1,\"remark\":\"\",\"createTime\":\"2025-11-24T10:00:00\",\"updateTime\":\"2025-11-24T10:00:00\"}]', 6, '127.0.0.1', '本地', 1, NULL, '2025-11-24 19:54:35');
INSERT INTO `sys_operation_log` VALUES (32, NULL, 'system', '更新宿舍楼', 'com.example.controller.BuildingController.update()', '[{\"id\":5,\"buildingNo\":\"A3\",\"buildingName\":\"A3男生宿舍楼\",\"floorCount\":6,\"roomPerFloor\":50,\"genderType\":1,\"managerId\":null,\"address\":\"\",\"buildYear\":2021,\"status\":1,\"remark\":\"\",\"createTime\":\"2025-11-24T10:00:00\",\"updateTime\":\"2025-11-24T10:00:00\"}]', 7, '127.0.0.1', '本地', 1, NULL, '2025-11-24 19:54:39');
INSERT INTO `sys_operation_log` VALUES (33, NULL, 'system', '更新宿舍楼', 'com.example.controller.BuildingController.update()', '[{\"id\":3,\"buildingNo\":\"B1\",\"buildingName\":\"西1号女生宿舍楼\",\"floorCount\":6,\"roomPerFloor\":50,\"genderType\":2,\"managerId\":2,\"address\":\"校园西区\",\"buildYear\":2021,\"status\":1,\"remark\":null,\"createTime\":\"2025-11-24T10:00:00\",\"updateTime\":\"2025-11-24T10:00:00\"}]', 5, '127.0.0.1', '本地', 1, NULL, '2025-11-24 19:55:02');
INSERT INTO `sys_operation_log` VALUES (34, NULL, 'system', '更新宿舍楼', 'com.example.controller.BuildingController.update()', '[{\"id\":4,\"buildingNo\":\"B2\",\"buildingName\":\"西2号女生宿舍楼\",\"floorCount\":6,\"roomPerFloor\":20,\"genderType\":2,\"managerId\":2,\"address\":\"校园西区\",\"buildYear\":2021,\"status\":1,\"remark\":null,\"createTime\":\"2025-11-24T10:00:00\",\"updateTime\":\"2025-11-24T10:00:00\"}]', 4, '127.0.0.1', '本地', 1, NULL, '2025-11-24 19:55:11');
INSERT INTO `sys_operation_log` VALUES (35, NULL, 'system', '更新宿舍楼', 'com.example.controller.BuildingController.update()', '[{\"id\":1,\"buildingNo\":\"A1\",\"buildingName\":\"A1号男生宿舍楼\",\"floorCount\":6,\"roomPerFloor\":20,\"genderType\":1,\"managerId\":2,\"address\":\"校园东区\",\"buildYear\":2021,\"status\":1,\"remark\":null,\"createTime\":\"2025-11-24T10:00:00\",\"updateTime\":\"2025-11-24T10:00:00\"}]', 39, '127.0.0.1', '本地', 1, NULL, '2025-11-24 19:58:51');
INSERT INTO `sys_operation_log` VALUES (36, NULL, 'system', '更新水电费记录', 'com.example.controller.UtilityBillController.update()', '[{\"id\":3,\"roomId\":6,\"billMonth\":\"2025-10\",\"electricStart\":0,\"electricEnd\":0,\"electricUsage\":38,\"electricPrice\":0.8,\"electricAmount\":22.8,\"waterStart\":0,\"waterEnd\":0,\"waterUsage\":6.5,\"waterPrice\":3,\"waterAmount\":19.5,\"totalAmount\":42.3,\"freeQuotaElectric\":null,\"freeQuotaWater\":null,\"actualAmount\":9.3,\"paymentStatus\":3,\"paymentTime\":null,\"paymentMethod\":null,\"recorderId\":null,\"meterReadingTime\":null,\"remark\":null,\"createTime\":null}]', 42, '127.0.0.1', '本地', 1, NULL, '2025-11-24 20:39:17');
INSERT INTO `sys_operation_log` VALUES (37, NULL, 'system', '更新违纪记录', 'com.example.controller.ViolationController.update()', '[{\"id\":1,\"studentIds\":\"4\",\"roomId\":1,\"violationType\":\"晚归\",\"violationTime\":\"2025-11-15T23:30:00\",\"description\":\"23:30返回宿舍，超过门禁时间\",\"evidence\":null,\"handleMethod\":\"警告\",\"deductScore\":6,\"recorderId\":null,\"notifyParent\":0,\"notifyTeacher\":0,\"handleStatus\":2,\"handleResult\":\"\",\"remark\":null,\"createTime\":null}]', 61, '127.0.0.1', '本地', 1, NULL, '2025-11-24 20:53:50');
INSERT INTO `sys_operation_log` VALUES (38, NULL, 'system', '上传文件', 'com.example.controller.FileController.upload()', '[\"MultipartFile(name=file, size=471007)\"]', 59, '127.0.0.1', '本地', 1, NULL, '2025-11-24 21:10:18');
INSERT INTO `sys_operation_log` VALUES (39, NULL, 'system', '上传文件', 'com.example.controller.FileController.upload()', '[\"MultipartFile(name=file, size=142484)\"]', 65, '127.0.0.1', '本地', 1, NULL, '2025-11-24 22:03:26');
INSERT INTO `sys_operation_log` VALUES (40, NULL, 'system', '修改用户', 'com.example.controller.UserController.update()', '[{\"id\":12,\"username\":null,\"password\":null,\"nickname\":\"mrk\",\"roleId\":null,\"email\":\"2661462026@qq.com\",\"phone\":\"18765432456\",\"avatar\":null,\"createTime\":null,\"lastLoginTime\":null,\"lastLoginIp\":null,\"status\":null,\"studentNo\":\"20403030331\",\"gender\":null,\"college\":null,\"major\":\"软件工程\",\"className\":\"前端B2001\",\"grade\":null,\"idCard\":null,\"parentPhone\":null,\"emergencyContact\":null,\"emergencyPhone\":null,\"studentStatus\":null}]', 8, '127.0.0.1', '本地', 1, NULL, '2025-11-24 22:03:58');
INSERT INTO `sys_operation_log` VALUES (41, NULL, 'system', '修改用户', 'com.example.controller.UserController.update()', '[{\"id\":12,\"username\":null,\"password\":null,\"nickname\":\"mrk\",\"roleId\":null,\"email\":\"2661462026@qq.com\",\"phone\":\"18765432456\",\"avatar\":null,\"createTime\":null,\"lastLoginTime\":null,\"lastLoginIp\":null,\"status\":null,\"studentNo\":\"20403030331\",\"gender\":1,\"college\":\"传媒技术学院\",\"major\":\"软件工程\",\"className\":\"前端B2001\",\"grade\":2020,\"idCard\":\"42900520010218098X\",\"parentPhone\":\"\",\"emergencyContact\":\"\",\"emergencyPhone\":\"\",\"studentStatus\":null}]', 11, '127.0.0.1', '本地', 1, NULL, '2025-11-24 22:08:03');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色唯一编码 (ROLE_ADMIN)',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'ROLE_ADMIN', '拥有所有权限');
INSERT INTO `sys_role` VALUES (3, '宿管员', 'ROLE_DORM_MANAGER', '管理宿舍日常事务');
INSERT INTO `sys_role` VALUES (4, '学生', 'ROLE_STUDENT', '普通学生用户');
INSERT INTO `sys_role` VALUES (5, '辅导员', 'ROLE_TEACHER', '查看和管理学生宿舍信息');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1);
INSERT INTO `sys_role_menu` VALUES (1, 2);
INSERT INTO `sys_role_menu` VALUES (1, 3);
INSERT INTO `sys_role_menu` VALUES (1, 4);
INSERT INTO `sys_role_menu` VALUES (1, 5);
INSERT INTO `sys_role_menu` VALUES (1, 6);
INSERT INTO `sys_role_menu` VALUES (1, 7);
INSERT INTO `sys_role_menu` VALUES (1, 8);
INSERT INTO `sys_role_menu` VALUES (1, 9);
INSERT INTO `sys_role_menu` VALUES (1, 10);
INSERT INTO `sys_role_menu` VALUES (1, 11);
INSERT INTO `sys_role_menu` VALUES (1, 12);
INSERT INTO `sys_role_menu` VALUES (1, 13);
INSERT INTO `sys_role_menu` VALUES (1, 14);
INSERT INTO `sys_role_menu` VALUES (1, 20);
INSERT INTO `sys_role_menu` VALUES (1, 21);
INSERT INTO `sys_role_menu` VALUES (1, 22);
INSERT INTO `sys_role_menu` VALUES (1, 23);
INSERT INTO `sys_role_menu` VALUES (1, 24);
INSERT INTO `sys_role_menu` VALUES (1, 25);
INSERT INTO `sys_role_menu` VALUES (1, 26);
INSERT INTO `sys_role_menu` VALUES (1, 27);
INSERT INTO `sys_role_menu` VALUES (1, 28);
INSERT INTO `sys_role_menu` VALUES (1, 29);
INSERT INTO `sys_role_menu` VALUES (1, 30);
INSERT INTO `sys_role_menu` VALUES (1, 31);
INSERT INTO `sys_role_menu` VALUES (1, 32);
INSERT INTO `sys_role_menu` VALUES (1, 33);
INSERT INTO `sys_role_menu` VALUES (1, 34);
INSERT INTO `sys_role_menu` VALUES (1, 35);
INSERT INTO `sys_role_menu` VALUES (1, 36);
INSERT INTO `sys_role_menu` VALUES (1, 37);
INSERT INTO `sys_role_menu` VALUES (1, 38);
INSERT INTO `sys_role_menu` VALUES (1, 39);
INSERT INTO `sys_role_menu` VALUES (1, 40);
INSERT INTO `sys_role_menu` VALUES (1, 41);
INSERT INTO `sys_role_menu` VALUES (1, 42);
INSERT INTO `sys_role_menu` VALUES (1, 43);
INSERT INTO `sys_role_menu` VALUES (1, 44);
INSERT INTO `sys_role_menu` VALUES (1, 45);
INSERT INTO `sys_role_menu` VALUES (1, 46);
INSERT INTO `sys_role_menu` VALUES (1, 47);
INSERT INTO `sys_role_menu` VALUES (1, 48);
INSERT INTO `sys_role_menu` VALUES (1, 49);
INSERT INTO `sys_role_menu` VALUES (1, 50);
INSERT INTO `sys_role_menu` VALUES (1, 51);
INSERT INTO `sys_role_menu` VALUES (1, 52);
INSERT INTO `sys_role_menu` VALUES (1, 53);
INSERT INTO `sys_role_menu` VALUES (1, 54);
INSERT INTO `sys_role_menu` VALUES (1, 55);
INSERT INTO `sys_role_menu` VALUES (1, 56);
INSERT INTO `sys_role_menu` VALUES (1, 57);
INSERT INTO `sys_role_menu` VALUES (1, 58);
INSERT INTO `sys_role_menu` VALUES (1, 59);
INSERT INTO `sys_role_menu` VALUES (1, 60);
INSERT INTO `sys_role_menu` VALUES (1, 61);
INSERT INTO `sys_role_menu` VALUES (1, 62);
INSERT INTO `sys_role_menu` VALUES (1, 63);
INSERT INTO `sys_role_menu` VALUES (1, 64);
INSERT INTO `sys_role_menu` VALUES (1, 65);
INSERT INTO `sys_role_menu` VALUES (1, 66);
INSERT INTO `sys_role_menu` VALUES (1, 67);
INSERT INTO `sys_role_menu` VALUES (1, 68);
INSERT INTO `sys_role_menu` VALUES (1, 69);
INSERT INTO `sys_role_menu` VALUES (1, 70);
INSERT INTO `sys_role_menu` VALUES (1, 71);
INSERT INTO `sys_role_menu` VALUES (3, 20);
INSERT INTO `sys_role_menu` VALUES (3, 21);
INSERT INTO `sys_role_menu` VALUES (3, 22);
INSERT INTO `sys_role_menu` VALUES (3, 23);
INSERT INTO `sys_role_menu` VALUES (3, 24);
INSERT INTO `sys_role_menu` VALUES (3, 25);
INSERT INTO `sys_role_menu` VALUES (3, 26);
INSERT INTO `sys_role_menu` VALUES (3, 27);
INSERT INTO `sys_role_menu` VALUES (3, 28);
INSERT INTO `sys_role_menu` VALUES (3, 29);
INSERT INTO `sys_role_menu` VALUES (3, 30);
INSERT INTO `sys_role_menu` VALUES (3, 31);
INSERT INTO `sys_role_menu` VALUES (3, 32);
INSERT INTO `sys_role_menu` VALUES (3, 33);
INSERT INTO `sys_role_menu` VALUES (3, 34);
INSERT INTO `sys_role_menu` VALUES (3, 35);
INSERT INTO `sys_role_menu` VALUES (3, 36);
INSERT INTO `sys_role_menu` VALUES (3, 37);
INSERT INTO `sys_role_menu` VALUES (3, 38);
INSERT INTO `sys_role_menu` VALUES (3, 39);
INSERT INTO `sys_role_menu` VALUES (3, 40);
INSERT INTO `sys_role_menu` VALUES (3, 41);
INSERT INTO `sys_role_menu` VALUES (3, 42);
INSERT INTO `sys_role_menu` VALUES (3, 44);
INSERT INTO `sys_role_menu` VALUES (3, 45);
INSERT INTO `sys_role_menu` VALUES (3, 48);
INSERT INTO `sys_role_menu` VALUES (3, 49);
INSERT INTO `sys_role_menu` VALUES (3, 51);
INSERT INTO `sys_role_menu` VALUES (3, 52);
INSERT INTO `sys_role_menu` VALUES (3, 53);
INSERT INTO `sys_role_menu` VALUES (3, 54);
INSERT INTO `sys_role_menu` VALUES (3, 55);
INSERT INTO `sys_role_menu` VALUES (3, 56);
INSERT INTO `sys_role_menu` VALUES (3, 57);
INSERT INTO `sys_role_menu` VALUES (3, 58);
INSERT INTO `sys_role_menu` VALUES (3, 59);
INSERT INTO `sys_role_menu` VALUES (3, 60);
INSERT INTO `sys_role_menu` VALUES (3, 61);
INSERT INTO `sys_role_menu` VALUES (3, 62);
INSERT INTO `sys_role_menu` VALUES (3, 63);
INSERT INTO `sys_role_menu` VALUES (3, 64);
INSERT INTO `sys_role_menu` VALUES (3, 65);
INSERT INTO `sys_role_menu` VALUES (3, 66);
INSERT INTO `sys_role_menu` VALUES (3, 67);
INSERT INTO `sys_role_menu` VALUES (3, 68);
INSERT INTO `sys_role_menu` VALUES (3, 69);
INSERT INTO `sys_role_menu` VALUES (3, 70);
INSERT INTO `sys_role_menu` VALUES (3, 71);
INSERT INTO `sys_role_menu` VALUES (5, 20);
INSERT INTO `sys_role_menu` VALUES (5, 21);
INSERT INTO `sys_role_menu` VALUES (5, 22);
INSERT INTO `sys_role_menu` VALUES (5, 23);
INSERT INTO `sys_role_menu` VALUES (5, 24);
INSERT INTO `sys_role_menu` VALUES (5, 25);
INSERT INTO `sys_role_menu` VALUES (5, 26);
INSERT INTO `sys_role_menu` VALUES (5, 27);
INSERT INTO `sys_role_menu` VALUES (5, 28);
INSERT INTO `sys_role_menu` VALUES (5, 29);
INSERT INTO `sys_role_menu` VALUES (5, 30);
INSERT INTO `sys_role_menu` VALUES (5, 31);
INSERT INTO `sys_role_menu` VALUES (5, 32);
INSERT INTO `sys_role_menu` VALUES (5, 33);
INSERT INTO `sys_role_menu` VALUES (5, 34);
INSERT INTO `sys_role_menu` VALUES (5, 35);
INSERT INTO `sys_role_menu` VALUES (5, 36);
INSERT INTO `sys_role_menu` VALUES (5, 37);
INSERT INTO `sys_role_menu` VALUES (5, 38);
INSERT INTO `sys_role_menu` VALUES (5, 39);
INSERT INTO `sys_role_menu` VALUES (5, 40);
INSERT INTO `sys_role_menu` VALUES (5, 41);
INSERT INTO `sys_role_menu` VALUES (5, 42);
INSERT INTO `sys_role_menu` VALUES (5, 43);
INSERT INTO `sys_role_menu` VALUES (5, 44);
INSERT INTO `sys_role_menu` VALUES (5, 45);
INSERT INTO `sys_role_menu` VALUES (5, 46);
INSERT INTO `sys_role_menu` VALUES (5, 47);
INSERT INTO `sys_role_menu` VALUES (5, 48);
INSERT INTO `sys_role_menu` VALUES (5, 49);
INSERT INTO `sys_role_menu` VALUES (5, 50);
INSERT INTO `sys_role_menu` VALUES (5, 51);
INSERT INTO `sys_role_menu` VALUES (5, 52);
INSERT INTO `sys_role_menu` VALUES (5, 53);
INSERT INTO `sys_role_menu` VALUES (5, 54);
INSERT INTO `sys_role_menu` VALUES (5, 55);
INSERT INTO `sys_role_menu` VALUES (5, 56);
INSERT INTO `sys_role_menu` VALUES (5, 57);
INSERT INTO `sys_role_menu` VALUES (5, 58);
INSERT INTO `sys_role_menu` VALUES (5, 59);
INSERT INTO `sys_role_menu` VALUES (5, 60);
INSERT INTO `sys_role_menu` VALUES (5, 61);
INSERT INTO `sys_role_menu` VALUES (5, 62);
INSERT INTO `sys_role_menu` VALUES (5, 63);
INSERT INTO `sys_role_menu` VALUES (5, 64);
INSERT INTO `sys_role_menu` VALUES (5, 65);
INSERT INTO `sys_role_menu` VALUES (5, 66);
INSERT INTO `sys_role_menu` VALUES (5, 67);
INSERT INTO `sys_role_menu` VALUES (5, 68);
INSERT INTO `sys_role_menu` VALUES (5, 69);
INSERT INTO `sys_role_menu` VALUES (5, 70);
INSERT INTO `sys_role_menu` VALUES (5, 71);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名/学号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称/姓名',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态 1:启用 0:禁用',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `student_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学号',
  `gender` tinyint(1) NULL DEFAULT NULL COMMENT '性别 1:男 2:女',
  `college` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学院',
  `major` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '专业',
  `class_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班级',
  `grade` int(11) NULL DEFAULT NULL COMMENT '年级',
  `id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `parent_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '家长电话',
  `emergency_contact` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '紧急联系人',
  `emergency_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '紧急联系电话',
  `student_status` tinyint(1) NULL DEFAULT 1 COMMENT '学生状态 1:在校 2:休学 3:毕业 4:退学',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE,
  UNIQUE INDEX `uk_student_no`(`student_no`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `idx_college`(`college`) USING BTREE,
  INDEX `idx_grade`(`grade`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表（含学生信息）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$1taPYWP0GrOcBLrQqHWvY.ldsgx3U.QqU.OZWqJvkSJ9vhWKLNS06', '超级管理员', 1, 'admin@example.com', '17666540336', 'http://localhost:8080/9e52972909304577a4e567fe30377ad6.jpg', '2025-11-20 16:00:35', 1, '2025-11-24 22:02:12', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `sys_user` VALUES (2, 'dorm_manager', '$2a$10$1taPYWP0GrOcBLrQqHWvY.ldsgx3U.QqU.OZWqJvkSJ9vhWKLNS06', '李宿管', 3, 'manager@qq.com', '13900139001', 'http://localhost:8080/ea48066d638042c0ba2c2820a6f465dd.PNG', '2025-11-24 10:00:00', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `sys_user` VALUES (3, 'wang', '$2a$10$KkTkTGf.qxiL1iHSwXK2SuZU.spBVHozVJ4lBXKnkGbdQg1rnO4rO', '王辅导员', 5, 'teacher@qq.com', '13900139002', 'http://localhost:8080/8c6d083e4112492bbf66e72365816fae.PNG', '2025-11-24 10:00:00', 1, '2025-11-24 21:10:08', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `sys_user` VALUES (4, '2021001', '$2a$10$KkTkTGf.qxiL1iHSwXK2SuZU.spBVHozVJ4lBXKnkGbdQg1rnO4rO', '张三', 4, 'zhangsan@qq.com', '13800138001', NULL, '2025-09-01 08:00:00', 1, NULL, NULL, '2021001', 1, '计算机学院', '软件工程', '软工2101', 2021, '110101200101011001', '13900139011', '张父', '13900139011', 1);
INSERT INTO `sys_user` VALUES (5, '2021002', '$2a$10$KkTkTGf.qxiL1iHSwXK2SuZU.spBVHozVJ4lBXKnkGbdQg1rnO4rO', '李四', 4, 'lisi@qq.com', '13800138002', NULL, '2025-09-01 08:00:00', 1, NULL, NULL, '2021002', 1, '计算机学院', '软件工程', '软工2101', 2021, '110101200101011002', '13900139012', '李父', '13900139012', 1);
INSERT INTO `sys_user` VALUES (6, '2021003', '$2a$10$KkTkTGf.qxiL1iHSwXK2SuZU.spBVHozVJ4lBXKnkGbdQg1rnO4rO', '王五', 4, 'wangwu@qq.com', '13800138003', NULL, '2025-09-01 08:00:00', 1, NULL, NULL, '2021003', 1, '计算机学院', '计算机科学', '计科2101', 2021, '110101200101011003', '13900139013', '王父', '13900139013', 1);
INSERT INTO `sys_user` VALUES (7, '2021004', '$2a$10$KkTkTGf.qxiL1iHSwXK2SuZU.spBVHozVJ4lBXKnkGbdQg1rnO4rO', '赵六', 4, 'zhaoliu@qq.com', '13800138004', NULL, '2025-09-01 08:00:00', 1, NULL, NULL, '2021004', 1, '信息学院', '信息管理', '信管2101', 2021, '110101200101011004', '13900139014', '赵父', '13900139014', 1);
INSERT INTO `sys_user` VALUES (8, '2021005', '$2a$10$KkTkTGf.qxiL1iHSwXK2SuZU.spBVHozVJ4lBXKnkGbdQg1rnO4rO', '小红', 4, 'xiaohong@qq.com', '13800138005', NULL, '2025-09-01 08:00:00', 1, NULL, NULL, '2021005', 2, '计算机学院', '软件工程', '软工2102', 2021, '110101200101012001', '13900139015', '小红母', '13900139015', 1);
INSERT INTO `sys_user` VALUES (9, '2021006', '$2a$10$KkTkTGf.qxiL1iHSwXK2SuZU.spBVHozVJ4lBXKnkGbdQg1rnO4rO', '小芳', 4, 'xiaofang@qq.com', '13800138006', NULL, '2025-09-01 08:00:00', 1, NULL, NULL, '2021006', 2, '计算机学院', '软件工程', '软工2102', 2021, '110101200101012002', '13900139016', '小芳母', '13900139016', 1);
INSERT INTO `sys_user` VALUES (10, '2021007', '$2a$10$KkTkTGf.qxiL1iHSwXK2SuZU.spBVHozVJ4lBXKnkGbdQg1rnO4rO', '小丽', 4, 'xiaoli@qq.com', '13800138007', NULL, '2025-09-01 08:00:00', 1, NULL, NULL, '2021007', 2, '信息学院', '信息管理', '信管2101', 2021, '110101200101012003', '13900139017', '小丽母', '13900139017', 1);
INSERT INTO `sys_user` VALUES (11, '2021008', '$2a$10$KkTkTGf.qxiL1iHSwXK2SuZU.spBVHozVJ4lBXKnkGbdQg1rnO4rO', '小美', 4, 'xiaomei@qq.com', '13800138008', NULL, '2025-09-01 08:00:00', 1, '2025-11-24 21:59:40', '127.0.0.1', '2021008', 2, '信息学院', '信息管理', '信管2101', 2021, '110101200101012004', '13900139018', '小美母', '13900139018', 1);
INSERT INTO `sys_user` VALUES (12, 'mrk', '$2a$10$UqrfXPu76jI0NU9H3ETgWOTWVSGt28Yl3rz/LfX/oORhPNO5ReiCK', 'mrk', 4, '2661462026@qq.com', '18765432456', 'http://localhost:8080/2451139a4c7d408c953b5a38ce2db559.jpg', '2025-11-24 21:52:04', 1, '2025-11-24 22:03:15', '127.0.0.1', '20403030331', 1, '传媒技术学院', '软件工程', '前端B2001', 2020, '42900520010218098X', '', '', '', 1);
INSERT INTO `sys_user` VALUES (13, 'mxx', '$2a$10$BOuhhOL9RGA6VLFq.pT0oOhDu8bdnVRC9oUZaLR74835xkdAFJNTu', NULL, 4, '2724547364@qq.com', NULL, NULL, '2025-11-24 21:55:48', 1, '2025-11-24 21:55:50', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

SET FOREIGN_KEY_CHECKS = 1;
