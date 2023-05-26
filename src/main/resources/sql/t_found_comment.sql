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

 Date: 08/05/2023 19:56:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_found_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_found_comment`;
CREATE TABLE `t_found_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commont` varchar(512) DEFAULT NULL,
  `found_time` bigint(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `is_delete` bit(1) DEFAULT b'0',
  `father_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `found_thing_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `father_id` (`father_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `found_thing_id` (`found_thing_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_found_comment
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
