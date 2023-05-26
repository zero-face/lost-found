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

 Date: 08/05/2023 19:57:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_message
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message` (
  `id` bigint(128) NOT NULL AUTO_INCREMENT,
  `chat_session_id` varchar(124) NOT NULL,
  `froms` bigint(64) DEFAULT NULL,
  `too` bigint(64) DEFAULT NULL,
  `send_text` varchar(512) DEFAULT NULL,
  `text_type` char(1) DEFAULT NULL,
  `msg_state` char(1) DEFAULT NULL,
  `type` char(1) DEFAULT '1' COMMENT '标志消息类型：0-系统消息-1-聊天-2-评论-3-点赞',
  `loss_id` int(32) DEFAULT NULL,
  `father_id` bigint(20) DEFAULT NULL,
  `status` bit(1) DEFAULT b'0' COMMENT '标志离线 还是线上',
  `gmt_modified` datetime DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=712 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_message
-- ----------------------------
BEGIN;
INSERT INTO `t_message` (`id`, `chat_session_id`, `froms`, `too`, `send_text`, `text_type`, `msg_state`, `type`, `loss_id`, `father_id`, `status`, `gmt_modified`, `gmt_create`) VALUES (697, 'c20ad4d76fe97759aa27a0c99bff6710', 2, 1, '你是谁', '0', '1', '1', NULL, NULL, b'0', '2023-05-07 23:14:55', '2023-05-07 23:12:38');
INSERT INTO `t_message` (`id`, `chat_session_id`, `froms`, `too`, `send_text`, `text_type`, `msg_state`, `type`, `loss_id`, `father_id`, `status`, `gmt_modified`, `gmt_create`) VALUES (698, 'c20ad4d76fe97759aa27a0c99bff6710', 2, 1, '{\"lossId\":\"18\",\"picUrl\":\"https://common-zero.oss-cn-beijing.aliyuncs.com/images/20230507/081210lrxUi.png\",\"title\":\"亲，这是我的失物哦～\",\"des\":\"567\",\"lossName\":\"7645\"}', '4', '1', '1', NULL, NULL, b'0', '2023-05-07 23:14:55', '2023-05-07 23:12:46');
INSERT INTO `t_message` (`id`, `chat_session_id`, `froms`, `too`, `send_text`, `text_type`, `msg_state`, `type`, `loss_id`, `father_id`, `status`, `gmt_modified`, `gmt_create`) VALUES (699, 'c20ad4d76fe97759aa27a0c99bff6710', 2, 1, '{\"lossId\":\"3\",\"picUrl\":\"https://tse3-mm.cn.bing.net/th/id/OIP-C.SGFn0dvByDXG8j8Dyk_6zwHaHa?w=177&h=180&c=7&o=5&dpr=1.12&pid=1.7\",\"title\":\"亲，这是我的失物哦～\",\"des\":\"一个红色的帽子\",\"lossName\":\"xxx的帽子\"}', '4', '1', '1', NULL, NULL, b'0', '2023-05-07 23:14:55', '2023-05-07 23:12:59');
INSERT INTO `t_message` (`id`, `chat_session_id`, `froms`, `too`, `send_text`, `text_type`, `msg_state`, `type`, `loss_id`, `father_id`, `status`, `gmt_modified`, `gmt_create`) VALUES (700, '9bf31c7ff062936a96d3c8bd1f8f2ff3', 5, 1, '{\"lossId\":\"3\",\"picUrl\":\"https://tse3-mm.cn.bing.net/th/id/OIP-C.SGFn0dvByDXG8j8Dyk_6zwHaHa?w=177&h=180&c=7&o=5&dpr=1.12&pid=1.7\",\"title\":\"亲，这是我的失物哦～\",\"des\":\"一个红色的帽子\",\"lossName\":\"xxx的帽子\"}', '4', '1', '1', NULL, NULL, b'0', '2023-05-07 23:32:12', '2023-05-07 23:28:34');
INSERT INTO `t_message` (`id`, `chat_session_id`, `froms`, `too`, `send_text`, `text_type`, `msg_state`, `type`, `loss_id`, `father_id`, `status`, `gmt_modified`, `gmt_create`) VALUES (701, 'c20ad4d76fe97759aa27a0c99bff6710', 2, 1, '等你啥课', '0', '1', '1', NULL, NULL, b'0', '2023-05-07 23:33:17', '2023-05-07 23:31:48');
INSERT INTO `t_message` (`id`, `chat_session_id`, `froms`, `too`, `send_text`, `text_type`, `msg_state`, `type`, `loss_id`, `father_id`, `status`, `gmt_modified`, `gmt_create`) VALUES (702, '9bf31c7ff062936a96d3c8bd1f8f2ff3', 1, 5, '你好牛啊', '0', '1', '1', NULL, NULL, b'0', '2023-05-07 23:32:34', '2023-05-07 23:32:17');
INSERT INTO `t_message` (`id`, `chat_session_id`, `froms`, `too`, `send_text`, `text_type`, `msg_state`, `type`, `loss_id`, `father_id`, `status`, `gmt_modified`, `gmt_create`) VALUES (703, '9bf31c7ff062936a96d3c8bd1f8f2ff3', 1, 5, '赵牛牛', '0', '1', '1', NULL, NULL, b'0', '2023-05-07 23:32:34', '2023-05-07 23:32:24');
INSERT INTO `t_message` (`id`, `chat_session_id`, `froms`, `too`, `send_text`, `text_type`, `msg_state`, `type`, `loss_id`, `father_id`, `status`, `gmt_modified`, `gmt_create`) VALUES (704, '9bf31c7ff062936a96d3c8bd1f8f2ff3', 1, 5, '赵', '0', '1', '1', NULL, NULL, b'0', '2023-05-07 23:34:38', '2023-05-07 23:32:37');
INSERT INTO `t_message` (`id`, `chat_session_id`, `froms`, `too`, `send_text`, `text_type`, `msg_state`, `type`, `loss_id`, `father_id`, `status`, `gmt_modified`, `gmt_create`) VALUES (705, '9bf31c7ff062936a96d3c8bd1f8f2ff3', 1, 5, '给我🔒一下', '0', '1', '1', NULL, NULL, b'0', '2023-05-07 23:34:38', '2023-05-07 23:33:06');
INSERT INTO `t_message` (`id`, `chat_session_id`, `froms`, `too`, `send_text`, `text_type`, `msg_state`, `type`, `loss_id`, `father_id`, `status`, `gmt_modified`, `gmt_create`) VALUES (706, '6512bd43d9caa6e02c990b0a82652dca', 1, 1, 'zeroface发布了一条失物招领信息', '0', '0', '4', NULL, NULL, b'0', NULL, '2023-05-08 12:54:09');
INSERT INTO `t_message` (`id`, `chat_session_id`, `froms`, `too`, `send_text`, `text_type`, `msg_state`, `type`, `loss_id`, `father_id`, `status`, `gmt_modified`, `gmt_create`) VALUES (707, 'c20ad4d76fe97759aa27a0c99bff6710', 1, 2, '{\"lossId\":\"1\",\"picUrl\":\"https://tse1-mm.cn.bing.net/th/id/OIP-C.nFG8XEG6IsGaK6d1Gm-EcQHaFh?w=250&h=186&c=7&o=5&dpr=1.12&pid=1.7\",\"title\":\"亲，这是我的失物哦～\",\"des\":\"一个黑色的钱包，里面有现金若干\",\"lossName\":\"褐色钱包\"}', '4', '0', '1', NULL, NULL, b'0', NULL, '2023-05-08 13:03:57');
INSERT INTO `t_message` (`id`, `chat_session_id`, `froms`, `too`, `send_text`, `text_type`, `msg_state`, `type`, `loss_id`, `father_id`, `status`, `gmt_modified`, `gmt_create`) VALUES (708, 'c20ad4d76fe97759aa27a0c99bff6710', 1, 2, '{\"lossId\":\"2\",\"picUrl\":\"https://tse3-mm.cn.bing.net/th/id/OIP-C.H5iTKMtnwQC0aX2gvudqbAHaE6?w=294&h=195&c=7&o=5&dpr=1.12&pid=1.7\",\"title\":\"亲，这是我的失物哦～\",\"des\":\"一张身份证，名字叫奥巴马，请奥巴马同学尽快认领\",\"lossName\":\"xxx的身份证\"}', '4', '0', '1', NULL, NULL, b'0', NULL, '2023-05-08 13:04:28');
INSERT INTO `t_message` (`id`, `chat_session_id`, `froms`, `too`, `send_text`, `text_type`, `msg_state`, `type`, `loss_id`, `father_id`, `status`, `gmt_modified`, `gmt_create`) VALUES (709, 'c20ad4d76fe97759aa27a0c99bff6710', 1, 2, '{\"lossId\":\"1\",\"picUrl\":\"https://tse1-mm.cn.bing.net/th/id/OIP-C.nFG8XEG6IsGaK6d1Gm-EcQHaFh?w=250&h=186&c=7&o=5&dpr=1.12&pid=1.7\",\"title\":\"亲，这是我的失物哦～\",\"des\":\"一个黑色的钱包，里面有现金若干\",\"lossName\":\"褐色钱包\"}', '4', '0', '1', NULL, NULL, b'0', NULL, '2023-05-08 13:04:50');
INSERT INTO `t_message` (`id`, `chat_session_id`, `froms`, `too`, `send_text`, `text_type`, `msg_state`, `type`, `loss_id`, `father_id`, `status`, `gmt_modified`, `gmt_create`) VALUES (710, '6512bd43d9caa6e02c990b0a82652dca', 1, 1, '审核驳回，原因：没有', '0', '1', '0', NULL, NULL, b'0', '2023-05-08 15:53:34', '2023-05-08 15:42:59');
INSERT INTO `t_message` (`id`, `chat_session_id`, `froms`, `too`, `send_text`, `text_type`, `msg_state`, `type`, `loss_id`, `father_id`, `status`, `gmt_modified`, `gmt_create`) VALUES (711, '6512bd43d9caa6e02c990b0a82652dca', 1, 1, 'zeroface发布了一条寻物启示信息', '0', '1', '4', NULL, NULL, b'0', NULL, '2023-05-08 16:04:48');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
