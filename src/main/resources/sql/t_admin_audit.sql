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

 Date: 08/05/2023 19:56:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_admin_audit
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_audit`;
CREATE TABLE `t_admin_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `found_id` int(11) DEFAULT NULL,
  `loss_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `status` char(1) DEFAULT '0' COMMENT '0-待审核 1-通过 2-驳回',
  `des` varchar(255) DEFAULT NULL,
  `admin_id` int(11) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `is_delete` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_admin_audit
-- ----------------------------
BEGIN;
INSERT INTO `t_admin_audit` (`id`, `found_id`, `loss_id`, `user_id`, `status`, `des`, `admin_id`, `gmt_create`, `gmt_modified`, `is_delete`) VALUES (8, NULL, 19, 1, '2', '没有', 1, '2023-05-08 12:54:08', '2023-05-08 15:42:59', b'0');
INSERT INTO `t_admin_audit` (`id`, `found_id`, `loss_id`, `user_id`, `status`, `des`, `admin_id`, `gmt_create`, `gmt_modified`, `is_delete`) VALUES (9, 6, NULL, 1, '0', NULL, NULL, '2023-05-08 16:04:47', NULL, b'0');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
