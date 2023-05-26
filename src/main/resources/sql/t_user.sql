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

 Date: 08/05/2023 19:58:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `open_id` varchar(1024) DEFAULT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  `true_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `sex` char(1) DEFAULT '0',
  `tel` varchar(11) DEFAULT NULL,
  `mail` varchar(32) DEFAULT NULL,
  `qq` varchar(255) DEFAULT NULL,
  `collage` varchar(255) DEFAULT NULL,
  `clazz` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `address_url` varchar(1024) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `is_delete` bit(1) DEFAULT b'0',
  `is_true` bit(1) DEFAULT b'0',
  `enable` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_user
-- ----------------------------
BEGIN;
INSERT INTO `t_user` (`id`, `open_id`, `nick_name`, `true_name`, `password`, `sex`, `tel`, `mail`, `qq`, `collage`, `clazz`, `number`, `address_url`, `gmt_create`, `gmt_modified`, `is_delete`, `is_true`, `enable`) VALUES (1, 'o7ji04mTTVLYFGRSKXGNmaLjodn4', 'zeroface', '程立', NULL, '0', '17323734303', 'clzeroface@163.com', '1444171773', '电智学院', '计算机192', '201907020412', 'https://tse2-mm.cn.bing.net/th/id/OIP-C.nKKDD9WSPuMMZNKpXLgnuQHaKm?w=196&h=282&c=7&r=0&o=5&dpr=2&pid=1.7', '2023-02-26 17:03:59', '2023-05-08 15:25:56', b'0', b'1', b'0');
INSERT INTO `t_user` (`id`, `open_id`, `nick_name`, `true_name`, `password`, `sex`, `tel`, `mail`, `qq`, `collage`, `clazz`, `number`, `address_url`, `gmt_create`, `gmt_modified`, `is_delete`, `is_true`, `enable`) VALUES (2, 'o7ji04lxCYaoTMYayhqKpxNVnBh0', '小红', 'gtt', NULL, '0', 'gy7', NULL, NULL, 'yu7', 'ui', 'y66', 'https://th.bing.com/th/id/OIP.yUGkgMr1drqeYuvryYuhBwHaE8?w=276&h=184&c=7&r=0&o=5&dpr=2&pid=1.7', '2023-03-02 23:21:55', '2023-05-07 23:21:28', b'0', b'1', b'1');
INSERT INTO `t_user` (`id`, `open_id`, `nick_name`, `true_name`, `password`, `sex`, `tel`, `mail`, `qq`, `collage`, `clazz`, `number`, `address_url`, `gmt_create`, `gmt_modified`, `is_delete`, `is_true`, `enable`) VALUES (3, 'o7ji04lsF1HoDEyAEa9Gg7ZvfVD4', '微信用户', NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, 'https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132', '2023-05-07 23:18:34', '2023-05-07 23:25:49', b'0', b'0', b'1');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
