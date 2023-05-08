/*
 Navicat Premium Data Transfer

 Source Server         : aliyun
 Source Server Type    : MySQL
 Source Server Version : 50741
 Source Host           : 39.107.238.203:3600
 Source Schema         : lost_found

 Target Server Type    : MySQL
 Target Server Version : 50741
 File Encoding         : 65001

 Date: 24/02/2023 00:10:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_admin_audit
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_audit`;
CREATE TABLE `t_admin_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loss_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `status` bit(1) DEFAULT b'0',
  `submit_time` bigint(20) DEFAULT NULL,
  `audit_time` bigint(20) DEFAULT '0',
  `admin_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_admin_audit
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
