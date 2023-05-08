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

 Date: 24/02/2023 00:12:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_message
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message` (
  `id` varchar(255) NOT NULL,
  `froms` bigint(64) DEFAULT NULL,
  `too` bigint(64) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `send_time` bigint(30) DEFAULT NULL,
  `flag` bit(1) DEFAULT b'1' COMMENT '定向广播标志',
  `status` bit(1) DEFAULT b'0' COMMENT '标志离线 还是线上',
  `type` char(1) DEFAULT NULL COMMENT '标志消息类型：0-系统消息-1-聊天-2-评论-3-点赞',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_message
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
