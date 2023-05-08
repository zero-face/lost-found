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

 Date: 24/02/2023 00:12:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_loss_thing
-- ----------------------------
DROP TABLE IF EXISTS `t_loss_thing`;
CREATE TABLE `t_loss_thing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `picture_url` varchar(512) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `status` bit(1) DEFAULT b'0',
  `loss_time` datetime DEFAULT NULL,
  `is_delete` bit(1) DEFAULT b'0',
  `loss_user_id` int(11) DEFAULT NULL,
  `type` char(11) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `loss_user_id` (`loss_user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_loss_thing
-- ----------------------------
BEGIN;
INSERT INTO `t_loss_thing` (`id`, `name`, `picture_url`, `address`, `description`, `status`, `loss_time`, `is_delete`, `loss_user_id`, `type`, `gmt_create`, `gmt_modified`) VALUES (1, '褐色钱包', 'https://tse1-mm.cn.bing.net/th/id/OIP-C.nFG8XEG6IsGaK6d1Gm-EcQHaFh?w=250&h=186&c=7&o=5&dpr=1.12&pid=1.7', '生活区门口', '一个黑色的钱包，里面有现金若干', b'1', NULL, b'0', 1, '1', '2023-02-21 23:45:23', '2023-02-21 23:47:06');
INSERT INTO `t_loss_thing` (`id`, `name`, `picture_url`, `address`, `description`, `status`, `loss_time`, `is_delete`, `loss_user_id`, `type`, `gmt_create`, `gmt_modified`) VALUES (2, 'xxx的身份证', 'https://tse3-mm.cn.bing.net/th/id/OIP-C.H5iTKMtnwQC0aX2gvudqbAHaE6?w=294&h=195&c=7&o=5&dpr=1.12&pid=1.7', '中午一点在教学区F楼1楼拾得', '一张身份证，名字叫奥巴马，请奥巴马同学尽快认领', b'0', NULL, b'0', 3, '7', '2023-01-11 23:45:28', '2023-02-02 23:47:11');
INSERT INTO `t_loss_thing` (`id`, `name`, `picture_url`, `address`, `description`, `status`, `loss_time`, `is_delete`, `loss_user_id`, `type`, `gmt_create`, `gmt_modified`) VALUES (3, 'xxx的帽子', 'https://tse3-mm.cn.bing.net/th/id/OIP-C.SGFn0dvByDXG8j8Dyk_6zwHaHa?w=177&h=180&c=7&o=5&dpr=1.12&pid=1.7', '一餐厅门口', '一个红色的帽子', b'0', NULL, b'0', 4, '6', '2023-02-26 23:45:41', '2023-02-17 23:47:15');
INSERT INTO `t_loss_thing` (`id`, `name`, `picture_url`, `address`, `description`, `status`, `loss_time`, `is_delete`, `loss_user_id`, `type`, `gmt_create`, `gmt_modified`) VALUES (4, '一卡通', 'https://tse1-mm.cn.bing.net/th/id/OIP-C.7kD4NoQrBusP1tZIikdzgwAAAA?w=196&h=152&c=7&o=5&dpr=1.12&pid=1.7', '二餐厅门口', 'xxx的一卡通', b'0', NULL, b'0', 3, '2', '2023-02-07 23:45:36', '2023-01-12 23:47:23');
INSERT INTO `t_loss_thing` (`id`, `name`, `picture_url`, `address`, `description`, `status`, `loss_time`, `is_delete`, `loss_user_id`, `type`, `gmt_create`, `gmt_modified`) VALUES (9, '黑色边框眼镜', 'https://tse2-mm.cn.bing.net/th/id/OIP-C.lM_yQDZcmxLa5EMwsJGpVgHaFJ?w=292&h=203&c=7&o=5&dpr=1.12&pid=1.7', '馨园餐厅', '中午在馨园餐厅门口第二排捡到', b'0', NULL, b'0', 3, '5', '2023-01-12 23:45:46', '2023-02-11 23:47:31');
INSERT INTO `t_loss_thing` (`id`, `name`, `picture_url`, `address`, `description`, `status`, `loss_time`, `is_delete`, `loss_user_id`, `type`, `gmt_create`, `gmt_modified`) VALUES (10, '黑白格子衬衫', 'https://tse1-mm.cn.bing.net/th/id/R-C.b0e35dd43008ac172e8dbe370c475958?rik=8Ox1qs8J2H1ibg&riu=http%3a%2f%2fpic1.ymatou.com%2fG03%2fshangou%2fM09%2f4B%2fE9%2fCgzUIF-6UxqAMOFtAAgFdfbWFFY696_500_667_n_w_o.jpg&ehk=tUMYDzDPvRSHJotf6h3fyTVmDno1SHLSBjC6Y8RhVCE%3d&risl=&pid=ImgRaw&r=0', '运动场', '下午体育课在篮球场捡到', b'0', NULL, b'0', 4, '8', '2023-02-08 23:45:53', '2023-02-17 23:47:36');
INSERT INTO `t_loss_thing` (`id`, `name`, `picture_url`, `address`, `description`, `status`, `loss_time`, `is_delete`, `loss_user_id`, `type`, `gmt_create`, `gmt_modified`) VALUES (11, '一对羽毛球拍', 'https://tse3-mm.cn.bing.net/th/id/OIP-C.BpTJB0kG4ArQOxAWxit5xAHaFG?w=274&h=187&c=7&o=5&dpr=1.12&pid=1.7', '运动场', '下午第二节体育课捡到', b'0', NULL, b'0', 1, '9', '2023-02-17 23:45:57', '2023-02-09 23:47:40');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
