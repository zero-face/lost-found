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

 Date: 27/08/2021 20:28:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_found_commont
-- ----------------------------
DROP TABLE IF EXISTS `t_found_commont`;
CREATE TABLE `t_found_commont`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commont` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `found_time` bigint(255) NULL DEFAULT NULL,
  `type` int(11) NULL DEFAULT NULL,
  `is_delete` bit(1) NULL DEFAULT b'0',
  `father_id` int(11) NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `found_thing_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `father_id`(`father_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `found_thing_id`(`found_thing_id`) USING BTREE,
  CONSTRAINT `t_found_commont_ibfk_1` FOREIGN KEY (`father_id`) REFERENCES `t_loss_commont` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_found_commont_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_found_commont_ibfk_3` FOREIGN KEY (`found_thing_id`) REFERENCES `t_loss_thing` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_found_commont
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
