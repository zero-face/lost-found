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

 Date: 27/08/2021 20:29:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_message
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `froms` bigint(64) NULL DEFAULT NULL,
  `too` bigint(64) NULL DEFAULT NULL,
  `message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `send_time` bigint(30) NULL DEFAULT NULL,
  `flag` bit(1) NULL DEFAULT b'1' COMMENT '定向广播标志',
  `status` bit(1) NULL DEFAULT b'0' COMMENT '标志离线 还是线上',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标志消息类型：0-系统消息-1-聊天-2-评论-3-点赞',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_message
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
