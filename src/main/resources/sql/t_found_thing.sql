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

 Date: 08/05/2023 19:57:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_found_thing
-- ----------------------------
DROP TABLE IF EXISTS `t_found_thing`;
CREATE TABLE `t_found_thing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `picture_url` varchar(512) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `lose_time` bigint(255) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `status` bit(1) DEFAULT b'0',
  `publish_time` bigint(255) DEFAULT NULL,
  `is_delete` bit(1) DEFAULT b'0',
  `publish_user_id` int(11) DEFAULT NULL,
  `type` char(11) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `publish_user_id` (`publish_user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_found_thing
-- ----------------------------
BEGIN;
INSERT INTO `t_found_thing` (`id`, `name`, `picture_url`, `address`, `lose_time`, `description`, `status`, `publish_time`, `is_delete`, `publish_user_id`, `type`, `gmt_create`, `gmt_modified`) VALUES (5, '证件照', '', '不知道在哪丢了', 1683532432336, '蓝底，红底，白底的都有', b'0', NULL, b'0', 1, '4', '2023-05-08 15:54:33', NULL);
INSERT INTO `t_found_thing` (`id`, `name`, `picture_url`, `address`, `lose_time`, `description`, `status`, `publish_time`, `is_delete`, `publish_user_id`, `type`, `gmt_create`, `gmt_modified`) VALUES (6, 'hahha', '', 'ffdaf', 1683533066880, 'fdsa', b'1', NULL, b'0', 1, '2', '2023-05-08 16:04:47', NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
