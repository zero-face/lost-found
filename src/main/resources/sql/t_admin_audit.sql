/*
 Navicat Premium Data Transfer

 Source Server         : 腾讯云
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : 82.157.191.65:3600
 Source Schema         : lost_found

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 27/08/2021 20:28:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_admin_audit
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_audit`;
CREATE TABLE `t_admin_audit`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loss_id` int(11) NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `status` bit(1) NULL DEFAULT b'0',
  `submit_time` bigint(20) NULL DEFAULT NULL,
  `audit_time` bigint(20) NULL DEFAULT 0,
  `admin_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_admin_audit
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
