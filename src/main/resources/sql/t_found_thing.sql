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

 Date: 24/02/2023 00:11:53
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
  KEY `publish_user_id` (`publish_user_id`) USING BTREE,
  CONSTRAINT `t_found_thing_ibfk_1` FOREIGN KEY (`publish_user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_found_thing
-- ----------------------------
BEGIN;
INSERT INTO `t_found_thing` (`id`, `name`, `picture_url`, `address`, `lose_time`, `description`, `status`, `publish_time`, `is_delete`, `publish_user_id`, `type`, `gmt_create`, `gmt_modified`) VALUES (1, '褐色钱包', 'https://tse1-mm.cn.bing.net/th/id/OIP-C.nFG8XEG6IsGaK6d1Gm-EcQHaFh?w=250&h=186&c=7&o=5&dpr=1.12&pid=1.7', '生活区门口', NULL, '1', b'0', 0, b'0', 1, '3', '2023-02-22 22:54:10', '2023-02-23 22:54:15');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
