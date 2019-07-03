/*
Navicat MySQL Data Transfer

Source Server         : 192.168.22.249_3306
Source Server Version : 50628
Source Host           : 192.168.22.249:3306
Source Database       : h_pool_admin

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2018-12-04 12:42:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `admin_sys_menu`;
CREATE TABLE `admin_sys_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT,
  `create_by` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `lastupdate_by` int(11) DEFAULT NULL,
  `lastupdate_time` datetime DEFAULT NULL,
  `heading` bit(1) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `order_num` int(11) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `perms` varchar(1024) DEFAULT NULL,
  `sref` varchar(100) DEFAULT NULL,
  `text` varchar(100) DEFAULT NULL,
  `translate` varchar(100) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for admin_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_sys_role`;
CREATE TABLE `admin_sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `create_by` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `lastupdate_by` int(11) DEFAULT NULL,
  `lastupdate_time` datetime DEFAULT NULL,
  `role_flag` varchar(10) DEFAULT NULL,
  `role_name` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for admin_sys_role_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `admin_sys_role_sys_menu`;
CREATE TABLE `admin_sys_role_sys_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_by` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `lastupdate_by` int(11) DEFAULT NULL,
  `lastupdate_time` datetime DEFAULT NULL,
  `menu_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for admin_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `admin_sys_user`;
CREATE TABLE `admin_sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `create_by` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `lastupdate_by` int(11) DEFAULT NULL,
  `lastupdate_time` datetime DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `login_ip` varchar(255) DEFAULT NULL,
  `login_time` datetime DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `telphone` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for admin_sys_user_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_sys_user_sys_role`;
CREATE TABLE `admin_sys_user_sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_by` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `lastupdate_by` int(11) DEFAULT NULL,
  `lastupdate_time` datetime DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- View structure for v_stats_pool_day
-- ----------------------------
DROP VIEW IF EXISTS `v_stats_pool_day`;
CREATE ALGORITHM=UNDEFINED DEFINER=`fcbss`@`%` SQL SECURITY DEFINER VIEW `v_stats_pool_day` AS select `m_pool`.`stats_pool_day`.`day` AS `day`,`m_pool`.`stats_pool_day`.`share_accept` AS `share_accept`,`m_pool`.`stats_pool_day`.`share_reject` AS `share_reject`,`m_pool`.`stats_pool_day`.`reject_rate` AS `reject_rate`,`m_pool`.`stats_pool_day`.`score` AS `score`,`m_pool`.`stats_pool_day`.`created_at` AS `created_at`,`m_pool`.`stats_pool_day`.`updated_at` AS `updated_at` from `m_pool`.`stats_pool_day` ;
