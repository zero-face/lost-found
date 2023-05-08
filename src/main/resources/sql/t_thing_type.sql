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

 Date: 24/02/2023 00:12:59
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_thing_type
-- ----------------------------
BEGIN;
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (1, 1, '钱包');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (2, 2, '一卡通');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (3, 3, '学生证');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (4, 4, '银行卡');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (5, 5, '眼镜');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (6, 6, '帽子');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (7, 7, '身份证');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (8, 8, '衣服');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (9, 9, '羽毛球拍');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (10, 10, '篮球');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (11, 11, '手机');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (12, 12, '电脑');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (13, 13, '平板');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (14, 14, '钥匙');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (15, 15, '乒乓球拍');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (16, 16, '排球');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (17, 17, '海报');
INSERT INTO `t_thing_type` (`id`, `type_id`, `type`) VALUES (18, 18, '其他');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
