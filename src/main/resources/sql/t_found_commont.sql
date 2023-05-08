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

 Date: 24/02/2023 00:11:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_found_commont
-- ----------------------------
DROP TABLE IF EXISTS `t_found_commont`;
CREATE TABLE `t_found_commont` (
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
  KEY `found_thing_id` (`found_thing_id`) USING BTREE,
  CONSTRAINT `t_found_commont_ibfk_1` FOREIGN KEY (`father_id`) REFERENCES `t_loss_commont` (`id`),
  CONSTRAINT `t_found_commont_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`),
  CONSTRAINT `t_found_commont_ibfk_3` FOREIGN KEY (`found_thing_id`) REFERENCES `t_loss_thing` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_found_commont
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
