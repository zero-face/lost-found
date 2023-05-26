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

 Date: 08/05/2023 19:57:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_loss_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_loss_comment`;
CREATE TABLE `t_loss_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment` varchar(512) DEFAULT NULL,
  `time` bigint(255) DEFAULT NULL,
  `type` char(1) DEFAULT NULL,
  `is_delete` bit(1) DEFAULT b'0',
  `father_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `to_user_name` varchar(64) DEFAULT NULL,
  `lost_thing_id` int(11) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `father_id` (`father_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `lost_thing_id` (`lost_thing_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_loss_comment
-- ----------------------------
BEGIN;
INSERT INTO `t_loss_comment` (`id`, `comment`, `time`, `type`, `is_delete`, `father_id`, `user_id`, `to_user_name`, `lost_thing_id`, `gmt_create`, `gmt_modified`) VALUES (93, '胡辜负', NULL, '1', b'0', NULL, 2, 'zeroface', 13, '2023-05-07 19:20:31', NULL);
INSERT INTO `t_loss_comment` (`id`, `comment`, `time`, `type`, `is_delete`, `father_id`, `user_id`, `to_user_name`, `lost_thing_id`, `gmt_create`, `gmt_modified`) VALUES (94, '更好', NULL, '1', b'0', NULL, 2, 'zeroface', 13, '2023-05-07 19:20:50', NULL);
INSERT INTO `t_loss_comment` (`id`, `comment`, `time`, `type`, `is_delete`, `father_id`, `user_id`, `to_user_name`, `lost_thing_id`, `gmt_create`, `gmt_modified`) VALUES (95, '谁捡着这个系统了？', NULL, '1', b'0', NULL, 1, 'zeroface', 18, '2023-05-07 23:35:53', NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
