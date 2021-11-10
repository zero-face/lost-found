/*
 Navicat Premium Data Transfer

 Source Server         : 腾讯云
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : 82.157.191.65:3600
 Source Schema         : lost_found

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 27/08/2021 20:28:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_loss_thing
-- ----------------------------
DROP TABLE IF EXISTS `t_loss_thing`;
CREATE TABLE `t_loss_thing`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `picture_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` bit(1) NULL DEFAULT b'0',
  `loss_time` bigint(20) NULL DEFAULT NULL,
  `is_delete` bit(1) NULL DEFAULT b'0',
  `loss_user_id` int(11) NULL DEFAULT NULL,
  `type` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `loss_user_id`(`loss_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_loss_thing
-- ----------------------------
INSERT INTO `t_loss_thing` VALUES (1, '褐色钱包', 'https://tse1-mm.cn.bing.net/th/id/OIP-C.nFG8XEG6IsGaK6d1Gm-EcQHaFh?w=250&h=186&c=7&o=5&dpr=1.12&pid=1.7', '生活区门口', '一个黑色的钱包，里面有现金若干', b'1', 1629504000000, b'0', 1, '1');
INSERT INTO `t_loss_thing` VALUES (2, 'xxx的身份证', 'https://tse3-mm.cn.bing.net/th/id/OIP-C.H5iTKMtnwQC0aX2gvudqbAHaE6?w=294&h=195&c=7&o=5&dpr=1.12&pid=1.7', '中午一点在教学区F楼1楼拾得', '一张身份证，名字叫奥巴马，请奥巴马同学尽快认领', b'0', 1629504000000, b'0', 3, '7');
INSERT INTO `t_loss_thing` VALUES (3, 'xxx的帽子', 'https://tse3-mm.cn.bing.net/th/id/OIP-C.SGFn0dvByDXG8j8Dyk_6zwHaHa?w=177&h=180&c=7&o=5&dpr=1.12&pid=1.7', '一餐厅门口', '一个红色的帽子', b'0', 1629504000000, b'0', 4, '6');
INSERT INTO `t_loss_thing` VALUES (4, '一卡通', 'https://tse1-mm.cn.bing.net/th/id/OIP-C.7kD4NoQrBusP1tZIikdzgwAAAA?w=196&h=152&c=7&o=5&dpr=1.12&pid=1.7', '二餐厅门口', 'xxx的一卡通', b'0', 1629504000000, b'0', 3, '2');
INSERT INTO `t_loss_thing` VALUES (9, '黑色边框眼镜', 'https://tse2-mm.cn.bing.net/th/id/OIP-C.lM_yQDZcmxLa5EMwsJGpVgHaFJ?w=292&h=203&c=7&o=5&dpr=1.12&pid=1.7', '馨园餐厅', '中午在馨园餐厅门口第二排捡到', b'0', 1629504000000, b'0', 3, '5');
INSERT INTO `t_loss_thing` VALUES (10, '黑白格子衬衫', 'https://tse1-mm.cn.bing.net/th/id/R-C.b0e35dd43008ac172e8dbe370c475958?rik=8Ox1qs8J2H1ibg&riu=http%3a%2f%2fpic1.ymatou.com%2fG03%2fshangou%2fM09%2f4B%2fE9%2fCgzUIF-6UxqAMOFtAAgFdfbWFFY696_500_667_n_w_o.jpg&ehk=tUMYDzDPvRSHJotf6h3fyTVmDno1SHLSBjC6Y8RhVCE%3d&risl=&pid=ImgRaw&r=0', '运动场', '下午体育课在篮球场捡到', b'0', 1629504000000, b'0', 4, '8');
INSERT INTO `t_loss_thing` VALUES (11, '一对羽毛球拍', 'https://tse3-mm.cn.bing.net/th/id/OIP-C.BpTJB0kG4ArQOxAWxit5xAHaFG?w=274&h=187&c=7&o=5&dpr=1.12&pid=1.7', '运动场', '下午第二节体育课捡到', b'0', 1629504000000, b'0', 1, '9');

SET FOREIGN_KEY_CHECKS = 1;
