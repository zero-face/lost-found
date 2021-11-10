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

 Date: 27/08/2021 20:28:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_found_thing
-- ----------------------------
DROP TABLE IF EXISTS `t_found_thing`;
CREATE TABLE `t_found_thing`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `picture_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `lose_time` bigint(255) NULL DEFAULT NULL,
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` bit(1) NULL DEFAULT b'0',
  `publish_time` bigint(255) NULL DEFAULT NULL,
  `is_delete` bit(1) NULL DEFAULT b'0',
  `publish_user_id` int(11) NULL DEFAULT NULL,
  `type` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `publish_user_id`(`publish_user_id`) USING BTREE,
  CONSTRAINT `t_found_thing_ibfk_1` FOREIGN KEY (`publish_user_id`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_found_thing
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
