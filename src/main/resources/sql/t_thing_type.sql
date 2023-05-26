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

 Date: 08/05/2023 19:58:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_thing_type
-- ----------------------------
DROP TABLE IF EXISTS `t_thing_type`;
CREATE TABLE `t_thing_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type_id` int(11) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_thing_type
-- ----------------------------
BEGIN;
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (1, 1, '电子产品');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (2, 2, '书籍杂志');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (3, 3, '衣物服饰');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (4, 4, '证件');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (5, 5, '钥匙');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (6, 6, '生活用品');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (7, 7, '运动器材');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (8, 8, '食品');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (9, 9, '办公用品');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (10, 10, '玩具');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
