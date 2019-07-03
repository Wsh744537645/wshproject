/*
Navicat MySQL Data Transfer

Source Server         : 192.168.22.249_3306
Source Server Version : 50628
Source Host           : 192.168.22.249:3306
Source Database       : h_pool_account

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2018-12-04 12:42:35
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account_blockchain
-- ----------------------------
DROP TABLE IF EXISTS `account_blockchain`;
CREATE TABLE `account_blockchain` (
  `hour` int(11) NOT NULL,
  `blocks_size` bigint(20) DEFAULT NULL,
  `difficulty` bigint(20) DEFAULT NULL,
  `estimated_btc_sent` bigint(20) DEFAULT NULL,
  `estimated_transaction_volume_usd` decimal(35,10) DEFAULT NULL,
  `hash_rate` decimal(35,10) DEFAULT NULL,
  `market_price_usd` decimal(35,10) DEFAULT NULL,
  `miners_revenue_btc` bigint(20) DEFAULT NULL,
  `miners_revenue_usd` decimal(19,2) DEFAULT NULL,
  `minutes_between_blocks` decimal(19,2) DEFAULT NULL,
  `n_blocks_mined` bigint(20) DEFAULT NULL,
  `n_blocks_total` bigint(20) DEFAULT NULL,
  `n_btc_mined` bigint(20) DEFAULT NULL,
  `n_tx` bigint(20) DEFAULT NULL,
  `nextretarget` bigint(20) DEFAULT NULL,
  `timestamp` decimal(35,10) DEFAULT NULL,
  `total_btc_sent` bigint(20) DEFAULT NULL,
  `total_fees_btc` bigint(20) DEFAULT NULL,
  `totalbc` bigint(20) DEFAULT NULL,
  `trade_volume_btc` decimal(35,10) DEFAULT NULL,
  `trade_volume_usd` decimal(35,10) DEFAULT NULL,
  PRIMARY KEY (`hour`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_currency
-- ----------------------------
DROP TABLE IF EXISTS `account_currency`;
CREATE TABLE `account_currency` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `enable` bit(1) NOT NULL,
  `type` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_currency_mini_pay
-- ----------------------------
DROP TABLE IF EXISTS `account_currency_mini_pay`;
CREATE TABLE `account_currency_mini_pay` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `currency_id` int(11) DEFAULT NULL,
  `mini_pay` decimal(19,6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_email_log
-- ----------------------------
DROP TABLE IF EXISTS `account_email_log`;
CREATE TABLE `account_email_log` (
  `email_id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `destination_email` varchar(100) DEFAULT NULL,
  `domain` varchar(32) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `email_code` varchar(12) DEFAULT NULL,
  `emali_content` varchar(255) DEFAULT NULL,
  `user_id` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`email_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_language
-- ----------------------------
DROP TABLE IF EXISTS `account_language`;
CREATE TABLE `account_language` (
  `lang_id` int(11) NOT NULL AUTO_INCREMENT,
  `lang_key` varchar(32) DEFAULT NULL,
  `lang_type` varchar(32) DEFAULT NULL,
  `lang_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`lang_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_menu
-- ----------------------------
DROP TABLE IF EXISTS `account_menu`;
CREATE TABLE `account_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT,
  `heading` bit(1) DEFAULT NULL,
  `icon` varchar(32) DEFAULT NULL,
  `order_num` int(11) DEFAULT '0',
  `parent_id` int(11) DEFAULT NULL,
  `perms` varchar(1024) DEFAULT NULL,
  `sref` varchar(32) DEFAULT NULL,
  `text` varchar(32) DEFAULT NULL,
  `translate` varchar(100) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_region
-- ----------------------------
DROP TABLE IF EXISTS `account_region`;
CREATE TABLE `account_region` (
  `region_id` int(11) NOT NULL AUTO_INCREMENT,
  `region_desc` varchar(255) DEFAULT NULL,
  `region_name` varchar(32) DEFAULT NULL,
  `status` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`region_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_resource
-- ----------------------------
DROP TABLE IF EXISTS `account_resource`;
CREATE TABLE `account_resource` (
  `res_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(100) DEFAULT NULL,
  `locale` varchar(10) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`res_id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_role
-- ----------------------------
DROP TABLE IF EXISTS `account_role`;
CREATE TABLE `account_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `role_flag` varchar(32) DEFAULT NULL,
  `role_name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `account_role_menu`;
CREATE TABLE `account_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_sms_log
-- ----------------------------
DROP TABLE IF EXISTS `account_sms_log`;
CREATE TABLE `account_sms_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `access_key_id` varchar(32) DEFAULT NULL,
  `domain` varchar(32) DEFAULT NULL,
  `out_id` varchar(64) DEFAULT NULL,
  `phone_number` varchar(11) DEFAULT NULL,
  `template_code` varchar(64) DEFAULT NULL,
  `template_param` varchar(64) DEFAULT NULL,
  `biz_id` varchar(64) DEFAULT NULL,
  `code` varchar(6) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `message` varchar(64) DEFAULT NULL,
  `request_id` varchar(64) DEFAULT NULL,
  `response_code` varchar(64) DEFAULT NULL,
  `response_out_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_user
-- ----------------------------
DROP TABLE IF EXISTS `account_user`;
CREATE TABLE `account_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `lang_type` varchar(8) DEFAULT NULL,
  `last_ip` varchar(20) DEFAULT NULL,
  `last_time` datetime DEFAULT NULL,
  `login_secret` varchar(6) DEFAULT NULL,
  `master_user_id` bigint(20) DEFAULT NULL,
  `nick_name` varchar(32) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `user_email` varchar(32) DEFAULT NULL,
  `user_from` varchar(255) DEFAULT NULL,
  `user_phone` varchar(11) DEFAULT NULL,
  `user_photo` varchar(255) DEFAULT NULL,
  `user_sex` varchar(2) DEFAULT NULL,
  `user_type` varchar(11) DEFAULT NULL,
  `username` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  KEY `master_user_id` (`master_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2010 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_user_login_log
-- ----------------------------
DROP TABLE IF EXISTS `account_user_login_log`;
CREATE TABLE `account_user_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `login_time` datetime NOT NULL,
  `login_ip` varchar(40) NOT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `os` varchar(64) DEFAULT NULL,
  `browser` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for account_user_open_resource
-- ----------------------------
DROP TABLE IF EXISTS `account_user_open_resource`;
CREATE TABLE `account_user_open_resource` (
  `user_id` bigint(20) NOT NULL,
  `res_id` varchar(64) NOT NULL,
  `create_time` datetime NOT NULL,
  `expiry_time` datetime NOT NULL,
  PRIMARY KEY (`res_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户分享 表';

-- ----------------------------
-- Table structure for account_user_region
-- ----------------------------
DROP TABLE IF EXISTS `account_user_region`;
CREATE TABLE `account_user_region` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `region_id` int(11) DEFAULT NULL COMMENT '区域id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2005 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_user_role
-- ----------------------------
DROP TABLE IF EXISTS `account_user_role`;
CREATE TABLE `account_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` int(11) DEFAULT NULL COMMENT '角色Id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2008 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_user_wallet
-- ----------------------------
DROP TABLE IF EXISTS `account_user_wallet`;
CREATE TABLE `account_user_wallet` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pay_type` varchar(10) DEFAULT NULL COMMENT '支付类型',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `wallet_id` varchar(64) DEFAULT NULL COMMENT '钱包id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2003 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_user_wallet_pay_type
-- ----------------------------
DROP TABLE IF EXISTS `account_user_wallet_pay_type`;
CREATE TABLE `account_user_wallet_pay_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `enable` bit(1) NOT NULL COMMENT '是否启用',
  `type` varchar(10) DEFAULT NULL COMMENT '钱包支付类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_wallet
-- ----------------------------
DROP TABLE IF EXISTS `account_wallet`;
CREATE TABLE `account_wallet` (
  `wallet_id` varchar(64) NOT NULL COMMENT '钱包id',
  `mini_pay` decimal(19,6) DEFAULT NULL COMMENT '最小支付',
  `user_id` bigint(20) DEFAULT NULL COMMENT '所属用户',
  `wallet_addr` varchar(100) DEFAULT NULL COMMENT '钱包地址',
  `wallet_type` varchar(10) DEFAULT NULL COMMENT '钱包类型',
  `status` int(2) DEFAULT NULL COMMENT '状态 = 0有效',
  PRIMARY KEY (`wallet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_worker
-- ----------------------------
DROP TABLE IF EXISTS `account_worker`;
CREATE TABLE `account_worker` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `worker_id` bigint(20) NOT NULL COMMENT '矿工id',
  `user_id` bigint(20) NOT NULL COMMENT '用户iD',
  `group_id` bigint(20) DEFAULT NULL COMMENT '矿工组 默认 0 = 未分组',
  `region_id` int(11) NOT NULL COMMENT '区域Id',
  `worker_name` varchar(20) NOT NULL COMMENT '矿工名称',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `worker_id` (`worker_id`,`user_id`,`region_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=40085 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for account_worker_group
-- ----------------------------
DROP TABLE IF EXISTS `account_worker_group`;
CREATE TABLE `account_worker_group` (
  `group_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '组id',
  `group_name` varchar(32) DEFAULT NULL COMMENT '组名',
  `group_seq` int(11) DEFAULT '0' COMMENT '组序号',
  `region_id` int(11) DEFAULT NULL COMMENT '区域iD',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`group_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for btc_user_pay
-- ----------------------------
DROP TABLE IF EXISTS `btc_user_pay`;
CREATE TABLE `btc_user_pay` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `puid` int(11) NOT NULL,
  `btc_wallet_address` char(100) DEFAULT NULL,
  `total_due` bigint(20) NOT NULL DEFAULT '0',
  `total_paid` bigint(20) NOT NULL DEFAULT '0',
  `pay_model` int(8) NOT NULL DEFAULT '1',
  `pplns_rate` int(8) NOT NULL DEFAULT '15',
  `ppsplus_rate` int(8) NOT NULL DEFAULT '20',
  `ppsplus_group` int(8) NOT NULL DEFAULT '1',
  `fpps_rate` int(8) NOT NULL DEFAULT '25',
  `fpps_group` int(8) NOT NULL DEFAULT '1',
  `is_pay_enable` int(8) NOT NULL DEFAULT '1',
  `last_pay_at` timestamp NULL DEFAULT NULL,
  `min_pay` bigint(20) NOT NULL DEFAULT '5000000',
  `btc_type` int(10) NOT NULL,
  `status` int(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `puid` (`puid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for btc_user_pay_bill
-- ----------------------------
DROP TABLE IF EXISTS `btc_user_pay_bill`;
CREATE TABLE `btc_user_pay_bill` (
  `bill_number` char(21) NOT NULL COMMENT '账单号或者流水号',
  `day` int(11) NOT NULL COMMENT '支付天 或者 账期',
  `txid` char(100) DEFAULT NULL COMMENT 'txid',
  `pay_all_btc` bigint(20) DEFAULT NULL COMMENT '支付btc',
  `pay_at` datetime DEFAULT NULL COMMENT '支付时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态1= 未完成 0 = 完成',
  `cteate_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL,
  `btc_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`bill_number`),
  UNIQUE KEY `day` (`day`,`bill_number`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for btc_user_pay_bill_item
-- ----------------------------
DROP TABLE IF EXISTS `btc_user_pay_bill_item`;
CREATE TABLE `btc_user_pay_bill_item` (
  `bill_number` char(21) NOT NULL COMMENT '账单号或者流水号',
  `puid` bigint(20) NOT NULL COMMENT 'puid',
  `btc_wallet_address` varchar(100) NOT NULL COMMENT '支付地址',
  `pay_btc` bigint(20) DEFAULT NULL COMMENT '支付btc',
  `cteate_at` datetime DEFAULT NULL COMMENT '创建时间',
  `day` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`bill_number`),
  UNIQUE KEY `day` (`puid`,`day`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for found_blocks
-- ----------------------------
DROP TABLE IF EXISTS `found_blocks`;
CREATE TABLE `found_blocks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `puid` int(11) NOT NULL,
  `worker_id` bigint(20) NOT NULL,
  `worker_full_name` varchar(50) NOT NULL,
  `job_id` bigint(20) unsigned NOT NULL,
  `height` int(11) NOT NULL,
  `is_orphaned` tinyint(4) NOT NULL DEFAULT '0',
  `hash` char(64) NOT NULL,
  `rewards` bigint(20) NOT NULL,
  `size` int(11) NOT NULL,
  `prev_hash` char(64) NOT NULL,
  `bits` int(10) unsigned NOT NULL,
  `version` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `confirmed_num` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `hash` (`hash`),
  KEY `height` (`height`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pool_fpps_record
-- ----------------------------
DROP TABLE IF EXISTS `pool_fpps_record`;
CREATE TABLE `pool_fpps_record` (
  `pool_id` int(8) NOT NULL DEFAULT '1',
  `day` int(11) NOT NULL DEFAULT '0',
  `fpps_total` bigint(20) NOT NULL DEFAULT '0',
  `fpps_pay_user` bigint(20) NOT NULL DEFAULT '0',
  `fpps_pay_pool` bigint(20) NOT NULL DEFAULT '0',
  `fpps_user_count` int(11) NOT NULL DEFAULT '0',
  `creat_at` timestamp NULL DEFAULT NULL,
  UNIQUE KEY `pool_id_day` (`pool_id`,`day`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pool_ppsplus_record
-- ----------------------------
DROP TABLE IF EXISTS `pool_ppsplus_record`;
CREATE TABLE `pool_ppsplus_record` (
  `pool_id` int(8) NOT NULL DEFAULT '1',
  `day` int(11) NOT NULL DEFAULT '0',
  `ppsplus_total` bigint(20) NOT NULL DEFAULT '0',
  `ppsplus_pay_user` bigint(20) NOT NULL DEFAULT '0',
  `ppsplus_pay_pool` bigint(20) NOT NULL DEFAULT '0',
  `ppsplus_user_count` int(11) NOT NULL DEFAULT '0',
  `creat_at` timestamp NULL DEFAULT NULL,
  UNIQUE KEY `pool_id_day` (`pool_id`,`day`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_fpps_record
-- ----------------------------
DROP TABLE IF EXISTS `user_fpps_record`;
CREATE TABLE `user_fpps_record` (
  `puid` int(11) NOT NULL,
  `day` int(11) NOT NULL,
  `fpps_before_fee` bigint(20) NOT NULL DEFAULT '0',
  `fpps_after_fee` bigint(20) NOT NULL DEFAULT '0',
  `fpps_rate` int(8) NOT NULL DEFAULT '40',
  `creat_at` timestamp NULL DEFAULT NULL,
  UNIQUE KEY `puid_day` (`puid`,`day`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_ppsplus_record
-- ----------------------------
DROP TABLE IF EXISTS `user_ppsplus_record`;
CREATE TABLE `user_ppsplus_record` (
  `puid` int(11) NOT NULL,
  `day` int(11) NOT NULL,
  `ppsplus_before_fee` bigint(20) NOT NULL DEFAULT '0',
  `ppsplus_after_fee` bigint(20) NOT NULL DEFAULT '0',
  `ppsplus_rate` int(8) NOT NULL DEFAULT '35',
  `creat_at` timestamp NULL DEFAULT NULL,
  UNIQUE KEY `puid_day` (`puid`,`day`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- View structure for v_stats_pool_day
-- ----------------------------
DROP VIEW IF EXISTS `v_stats_pool_day`;
CREATE ALGORITHM=UNDEFINED DEFINER=`fcbss`@`%` SQL SECURITY DEFINER VIEW `v_stats_pool_day` AS select `m_pool`.`stats_pool_day`.`day` AS `day`,`m_pool`.`stats_pool_day`.`share_accept` AS `share_accept`,`m_pool`.`stats_pool_day`.`share_reject` AS `share_reject`,`m_pool`.`stats_pool_day`.`reject_rate` AS `reject_rate`,`m_pool`.`stats_pool_day`.`score` AS `score`,`m_pool`.`stats_pool_day`.`created_at` AS `created_at`,`m_pool`.`stats_pool_day`.`updated_at` AS `updated_at` from `m_pool`.`stats_pool_day` ;
