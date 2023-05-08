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

 Date: 24/02/2023 00:13:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nick_name` varchar(32) DEFAULT NULL,
  `true_name` varchar(64) DEFAULT NULL,
  `password` varchar(256) DEFAULT NULL,
  `sex` varchar(1) DEFAULT NULL,
  `tel` varchar(11) DEFAULT NULL,
  `qq` varchar(12) DEFAULT NULL,
  `collage` varchar(128) DEFAULT NULL,
  `clazz` varchar(128) DEFAULT NULL,
  `number` varchar(128) DEFAULT NULL,
  `address_url` varchar(512) DEFAULT NULL,
  `last_login_time` bigint(255) DEFAULT NULL,
  `enable` bit(1) DEFAULT b'1',
  `is_delete` bit(1) DEFAULT b'0',
  `open_id` varchar(64) DEFAULT NULL,
  `is_true` bit(1) DEFAULT b'0',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniquename` (`nick_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_user
-- ----------------------------
BEGIN;
INSERT INTO `t_user` (`id`, `nick_name`, `true_name`, `password`, `sex`, `tel`, `qq`, `collage`, `clazz`, `number`, `address_url`, `last_login_time`, `enable`, `is_delete`, `open_id`, `is_true`, `gmt_create`, `gmt_modified`) VALUES (1, 'test', 'zero', '$2a$10$OTKm1JRyj2Ogeoyrt1IE6e36VD99u6oDEX7tAan/Q6ln6XCwFQjEq', '男', '19802980992', '1444171773', '电智学院', '计科192', '201907020412', 'https://tse4-mm.cn.bing.net/th/id/OIP-C.l5oteGIsGxT9JvLDC2rC2gHaKL?w=200&h=275&c=7&o=5&dpr=1.12&pid=1.7', 1629732084383, b'1', b'0', NULL, b'1', NULL, NULL);
INSERT INTO `t_user` (`id`, `nick_name`, `true_name`, `password`, `sex`, `tel`, `qq`, `collage`, `clazz`, `number`, `address_url`, `last_login_time`, `enable`, `is_delete`, `open_id`, `is_true`, `gmt_create`, `gmt_modified`) VALUES (3, 'admin', NULL, '$2a$10$TO3HESZ/tiCiJ6TRiHuWx.PLy3.2Q09.K6rwueKkq9sshiKPCeehq', NULL, NULL, NULL, NULL, NULL, NULL, 'https://tse1-mm.cn.bing.net/th/id/OIP-C.R3kekblFhbI4FU_Br-rDwwHaJQ?w=200&h=250&c=7&o=5&dpr=1.12&pid=1.7', NULL, b'1', b'0', NULL, b'0', NULL, NULL);
INSERT INTO `t_user` (`id`, `nick_name`, `true_name`, `password`, `sex`, `tel`, `qq`, `collage`, `clazz`, `number`, `address_url`, `last_login_time`, `enable`, `is_delete`, `open_id`, `is_true`, `gmt_create`, `gmt_modified`) VALUES (4, 'zero', NULL, '$2a$10$8SN0pPFJ.A524L.91k/8A.izl6TQbbQhilxIJRTp1r/o5ZETj690O', NULL, NULL, '1097566086', NULL, NULL, NULL, 'https://tse3-mm.cn.bing.net/th/id/OIP-C.Ygidl3RQEGt8klccg_wBgwHaLH?w=200&h=300&c=7&o=5&dpr=1.12&pid=1.7', 1628843113219, b'1', b'0', NULL, b'0', NULL, NULL);
INSERT INTO `t_user` (`id`, `nick_name`, `true_name`, `password`, `sex`, `tel`, `qq`, `collage`, `clazz`, `number`, `address_url`, `last_login_time`, `enable`, `is_delete`, `open_id`, `is_true`, `gmt_create`, `gmt_modified`) VALUES (5, '花花世界besos', '程立', NULL, '1', '19802980992', NULL, '电智学院', '计科192', '201907020412', 'https://thirdwx.qlogo.cn/mmopen/vi_32/9FRfvCn4kjsEKHyzbLU2xicdhyt1mDReib8zd1ZFHHQvNMasUFJxD8fCvB7VH783sib7AcVcmheJGdTEabE8rI3tw/132', 1629982443449, b'1', b'0', 'o7ji04mTTVLYFGRSKXGNmaLjodn4', b'1', NULL, '2023-02-22 01:18:52');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
