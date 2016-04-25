-- MySQL dump 10.13  Distrib 5.5.17, for Linux (x86_64)
--
-- Host: localhost    Database: phoenix_tongren
-- ------------------------------------------------------
-- Server version	5.5.17-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tb_mq_msg_sendrecord`
--

DROP TABLE IF EXISTS `tb_mq_msg_sendrecord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_mq_msg_sendrecord` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `msg_id` bigint(20) DEFAULT NULL COMMENT '消息的id',
  `msg_content` varchar(3000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '消息内容',
  `topic` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '发送目的地的topic',
  `tags` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '记录的创建时间',
  `last_send_time` datetime DEFAULT NULL COMMENT '最后一次发送时间',
  `send_status` tinyint(4) DEFAULT NULL COMMENT '1 发送成功 2 发送失败',
  `send_count` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='mq消息发送记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization`
--

DROP TABLE IF EXISTS `tb_organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '名称',
  `classification` bigint(20) DEFAULT NULL COMMENT '分类',
  `introduction` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '简介',
  `logo` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '组织的LOGO图标，存File中taskId',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `area` bigint(50) DEFAULT NULL COMMENT '地区',
  `industry` bigint(50) DEFAULT NULL COMMENT '行业',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '组织状态 0:正常 1:解散',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_attendance_records`
--

DROP TABLE IF EXISTS `tb_organization_attendance_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_attendance_records` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `attendance_system_id` bigint(20) DEFAULT NULL COMMENT '考勤制度ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `date` datetime DEFAULT NULL,
  `start_work_time` datetime DEFAULT NULL,
  `work_time_out` datetime DEFAULT NULL,
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `create_time` datetime DEFAULT NULL COMMENT '记录创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '记录修改时间',
  `ip_addr_start` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '打卡时ip地址（web端打卡）签到',
  `ip_addr_end` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '打卡时ip地址（web端打卡）签退',
  `lonlat_start` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'app打卡的经纬度（签到）',
  `lonlat_end` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'app打卡的经纬度（签退）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织考勤记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_attendance_system`
--

DROP TABLE IF EXISTS `tb_organization_attendance_system`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_attendance_system` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '考勤名称',
  `start_work_time` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上班时间',
  `work_time_out` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '下班时间',
  `week_working_days` int(11) DEFAULT NULL COMMENT '周工作制',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `working_hours` int(11) DEFAULT NULL,
  `elasticity_minutes` int(11) DEFAULT NULL,
  `description` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织考勤';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_authority`
--

DROP TABLE IF EXISTS `tb_organization_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_authority` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `authority_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '权限名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creater_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `authority_no` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '权限编号',
  `description` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述',
  `pid` bigint(20) DEFAULT NULL COMMENT '父id',
  `level` int(11) DEFAULT NULL COMMENT '层级',
  PRIMARY KEY (`id`),
  UNIQUE KEY `authority_no` (`authority_no`),
  UNIQUE KEY `authority_name` (`authority_name`),
  UNIQUE KEY `authority_name_2` (`authority_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织角色权限';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_bank_account`
--

DROP TABLE IF EXISTS `tb_organization_bank_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_bank_account` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `organization_capital_id` bigint(20) DEFAULT NULL COMMENT '组织资金ID',
  `bank` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '银行',
  `bank_number` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '银行卡',
  `bank_card_type` tinyint(4) DEFAULT NULL COMMENT '1信用卡、2借记卡',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织第三方外部资金账号';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_capital`
--

DROP TABLE IF EXISTS `tb_organization_capital`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_capital` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `organization_amount` double DEFAULT NULL COMMENT '资金额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织资金';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_capital_inflows`
--

DROP TABLE IF EXISTS `tb_organization_capital_inflows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_capital_inflows` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `payer_id` bigint(20) DEFAULT NULL COMMENT '付款者ID',
  `payer_organization_id` bigint(20) DEFAULT NULL COMMENT '付款者所属组织ID',
  `pay_amount` double DEFAULT NULL COMMENT '付款金额',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
  `pay_title` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '付款标题',
  `pay_content` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '付款内容',
  `pay_ways` tinyint(4) DEFAULT NULL COMMENT '付款方式',
  `pay_account_id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '付款账号',
  `to_account_time` datetime DEFAULT NULL COMMENT '到账日期',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `is_invoice` tinyint(4) DEFAULT NULL COMMENT '是否开取发票',
  `organization_capital_id` bigint(20) DEFAULT NULL COMMENT '组织资金ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='资金流入明细';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_capital_outflows`
--

DROP TABLE IF EXISTS `tb_organization_capital_outflows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_capital_outflows` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `drawer_id` bigint(20) DEFAULT NULL COMMENT '提款者ID',
  `withdraw_time` datetime DEFAULT NULL COMMENT '提款时间',
  `drawer_organization_id` bigint(20) DEFAULT NULL COMMENT '所属组织ID',
  `withdraw_title` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '提款标题',
  `withdraw_content` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '提款内容',
  `withdraw_ways` tinyint(4) DEFAULT NULL COMMENT '提款方式',
  `withdraw_status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `withdray_amount` double DEFAULT NULL COMMENT '提款金额',
  `organization_capital_id` bigint(20) DEFAULT NULL COMMENT '组织资金ID',
  `receive_account` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '提款者接收账号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='资金流出明细';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_certified`
--

DROP TABLE IF EXISTS `tb_organization_certified`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_certified` (
  `id` bigint(20) DEFAULT NULL COMMENT '主键',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `full_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '组织全称',
  `introduction` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '组织简介',
  `organization_type` tinyint(4) DEFAULT NULL COMMENT '组织类型',
  `legal_person` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '法人',
  `legal_person_mobile` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '法人手机号',
  `logo` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '企业LOGO',
  `business_license` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '营业执照复印件',
  `identity_card` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '法人身份证复印件',
  `status` tinyint(4) DEFAULT NULL COMMENT '认证状态 1 待认证 2 认证通过 3 认证不通过\r\n            1 待认证  首次提交即为待认证状态\r\n            ',
  `create_time` datetime DEFAULT NULL COMMENT '记录的创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '记录的修改时间',
  `oper_user_id` bigint(20) DEFAULT NULL COMMENT '操作人id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织认证';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_classification`
--

DROP TABLE IF EXISTS `tb_organization_classification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_classification` (
  `id` int(11) NOT NULL,
  `classification_name` varchar(0) COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_id` int(11) DEFAULT NULL,
  `description` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织分类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_department`
--

DROP TABLE IF EXISTS `tb_organization_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_department` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `dep_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '部门名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `description` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '部门描述',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `status` tinyint(4) DEFAULT NULL COMMENT '部门状态0有效、1无效',
  `pid` bigint(20) DEFAULT NULL COMMENT '上级部门ID',
  `level` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '部门等级 1、1-1、1-2',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织分为多个部门';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_department_authority`
--

DROP TABLE IF EXISTS `tb_organization_department_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_department_authority` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `department_id` bigint(20) DEFAULT NULL COMMENT '组织角色ID',
  `authority` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '1查看、2编辑、3删除、5添加',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织部门权限';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_department_member`
--

DROP TABLE IF EXISTS `tb_organization_department_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_department_member` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `department_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creater_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `organization_member_id` bigint(20) DEFAULT NULL COMMENT '组织成员Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织部门成员';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_join_way`
--

DROP TABLE IF EXISTS `tb_organization_join_way`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_join_way` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `join_way` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='成员加入方式';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_member`
--

DROP TABLE IF EXISTS `tb_organization_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_member` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `join_way` tinyint(4) DEFAULT NULL COMMENT '1我的好友、2搜索、3系统推荐',
  `apply_time` datetime DEFAULT NULL COMMENT '申请加入时间',
  `status` tinyint(4) DEFAULT NULL COMMENT ' 对应status表id',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AVG_ROW_LENGTH=3276 COMMENT='组织成员';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_member_role`
--

DROP TABLE IF EXISTS `tb_organization_member_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_member_role` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `organization_member_id` bigint(20) DEFAULT NULL COMMENT '组织成员ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creater_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织角色成员组';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_member_status`
--

DROP TABLE IF EXISTS `tb_organization_member_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_member_status` (
  `id` bigint(11) NOT NULL,
  `value` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='成员加入状态';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_message_create`
--

DROP TABLE IF EXISTS `tb_organization_message_create`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_message_create` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `title` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `content` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_id` bigint(20) DEFAULT NULL,
  `organization_id` bigint(20) DEFAULT NULL,
  `message_status` tinyint(4) DEFAULT NULL COMMENT '0草稿、1正稿',
  `message_type` tinyint(4) DEFAULT NULL COMMENT '0、普通消息  1、邀请组织消息 2、申请组织消息 3、申请退出组织消息 4、加入组织消息 5、拒绝加入组织消息 6、退出组织消息 7、拒绝退出组织消息 8、解散组织 9、被组织踢 10、分配任务 11、退回任务 12、重发任务 13、放弃项目 14、结束项目 15、某组织想承接项目 16、提项目文档 17、分配子任务 18、项目邀请组织承接 19、同意承接项目 20、拒绝承接项目',
  `project_id` bigint(20) DEFAULT '0',
  `cycle` int(11) unsigned DEFAULT '0' COMMENT '延期项目申请的天数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_message_receive`
--

DROP TABLE IF EXISTS `tb_organization_message_receive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_message_receive` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `message_id` bigint(20) DEFAULT NULL COMMENT '消息ID',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '接收所属组织ID',
  `department_id` bigint(20) DEFAULT NULL COMMENT '接收者部门ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '接收者ID',
  `status` tinyint(4) DEFAULT NULL COMMENT '0已阅读、1未阅读',
  `receive_time` datetime DEFAULT NULL COMMENT '接收时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织消息接收记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_message_send`
--

DROP TABLE IF EXISTS `tb_organization_message_send`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_message_send` (
  `id` bigint(11) NOT NULL,
  `message_id` bigint(11) DEFAULT NULL COMMENT '消息ID',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `user_id` bigint(11) DEFAULT NULL COMMENT '发送者',
  `status` tinyint(4) DEFAULT NULL COMMENT '0待发送、1发送成功、2发送失败',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '发送者所属组织ID',
  `department_id` bigint(20) DEFAULT NULL COMMENT '发送者所属部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织消息发送记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_module`
--

DROP TABLE IF EXISTS `tb_organization_module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_module` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模块名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `creater_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `description` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模块描述',
  `status` tinyint(4) DEFAULT NULL COMMENT '1启用、2关闭',
  `url` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模块URL',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织模块';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_module_application`
--

DROP TABLE IF EXISTS `tb_organization_module_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_module_application` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `module_id` bigint(20) DEFAULT NULL COMMENT '模块ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creater_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织模块应用';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_module_property`
--

DROP TABLE IF EXISTS `tb_organization_module_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_module_property` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '属性名称',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '属性状态',
  `description` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '属性描述',
  `module_id` bigint(20) DEFAULT NULL COMMENT '模块ID',
  `url` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '属性URL',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_review_application`
--

DROP TABLE IF EXISTS `tb_organization_review_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_review_application` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `review_process_id` bigint(20) DEFAULT NULL COMMENT '审核流程ID',
  `review_genre_id` bigint(20) DEFAULT NULL COMMENT '审核流程类型',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `apply_id` bigint(20) DEFAULT NULL COMMENT '申请者ID',
  `apply_date` datetime DEFAULT NULL COMMENT '申请日期',
  `apply_rereason` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '申请原因',
  `start_time` datetime DEFAULT NULL COMMENT '流程开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '流程结束时间',
  `application_no` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '申请编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织审核申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_review_genre`
--

DROP TABLE IF EXISTS `tb_organization_review_genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_review_genre` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '类型名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建人Id',
  `pid` bigint(20) DEFAULT NULL COMMENT '父级Id process主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_review_object`
--

DROP TABLE IF EXISTS `tb_organization_review_object`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_review_object` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `review_level` tinyint(4) DEFAULT NULL COMMENT '检核级别',
  `review_role_id` bigint(20) DEFAULT NULL COMMENT '签核角色ID',
  `review_user_id` bigint(20) DEFAULT NULL COMMENT '签核人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `upate_time` datetime DEFAULT NULL COMMENT '更新日期',
  `create_id` bigint(20) DEFAULT NULL,
  `backup_review_user_id` bigint(20) DEFAULT NULL COMMENT '签核备份人ID',
  `review_process` bigint(20) DEFAULT NULL COMMENT '审核流程ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织审核对象';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_review_process`
--

DROP TABLE IF EXISTS `tb_organization_review_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_review_process` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `review_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '审核名称',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `review_no` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '审核NO',
  `description` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '说明',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织审核流程';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_review_records`
--

DROP TABLE IF EXISTS `tb_organization_review_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_review_records` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `review_user_id` bigint(20) DEFAULT NULL COMMENT '签核人ID',
  `review_date` datetime DEFAULT NULL COMMENT '签核日期',
  `review_message` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '签核消息',
  `review_status` tinyint(4) DEFAULT NULL COMMENT '0审核中、2审核通过、3审核拒绝',
  `application_id` bigint(20) DEFAULT NULL COMMENT '审核申请id',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织id',
  `is_review` int(2) DEFAULT NULL COMMENT '当前是否需要审核：1需要审核 0不需要审核',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织审核记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_role`
--

DROP TABLE IF EXISTS `tb_organization_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_role` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `role_name` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '角色名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `description` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织角色';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_role_authority`
--

DROP TABLE IF EXISTS `tb_organization_role_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_role_authority` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `role_id` bigint(20) DEFAULT NULL COMMENT '组织角色ID',
  `authority_id` bigint(20) DEFAULT NULL COMMENT '权限',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creater_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织角色权限';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_organization_third_purse`
--

DROP TABLE IF EXISTS `tb_organization_third_purse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_organization_third_purse` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '名称',
  `account` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '账号',
  `organization_capital_id` bigint(20) DEFAULT NULL COMMENT '组织资金ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织第三方钱包';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_project`
--

DROP TABLE IF EXISTS `tb_project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_project` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '项目名称',
  `introduction` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '项目介绍',
  `cycle` int(11) DEFAULT NULL COMMENT '项目周期，单位天',
  `area` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `industry` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `document` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文档、对应taskId',
  `create_id` bigint(20) DEFAULT '0' COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '创建者所属组织',
  `remuneration` double DEFAULT NULL COMMENT '酬劳',
  `status` tinyint(4) DEFAULT NULL COMMENT '0草稿、1正式',
  `validity_start_time` datetime DEFAULT NULL,
  `validity_end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='项目';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_project_aborted`
--

DROP TABLE IF EXISTS `tb_project_aborted`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_project_aborted` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `project_undertaken_id` bigint(20) DEFAULT NULL COMMENT '项目承接ID',
  `reason` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '失败原因',
  `recipient_id` bigint(20) DEFAULT NULL COMMENT '承接人ID',
  `undertaken_organization_id` bigint(20) DEFAULT NULL COMMENT '承接组织ID',
  `operation_id` bigint(20) DEFAULT NULL COMMENT '操作人id',
  `create_organization_id` bigint(20) DEFAULT NULL COMMENT '项目创建组织ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='项目失败表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_project_apply`
--

DROP TABLE IF EXISTS `tb_project_apply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_project_apply` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `proposer_id` bigint(20) DEFAULT NULL COMMENT '申请人 user_id',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '申请人所属组织ID',
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `project_id` bigint(20) DEFAULT NULL COMMENT '申请项目ID',
  `status` tinyint(4) DEFAULT NULL COMMENT '0申请失败、1审核中、2申请成功',
  `review_time` datetime DEFAULT NULL COMMENT '审核日期',
  `reviewer_id` bigint(20) DEFAULT NULL COMMENT '审核人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='项目申请';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_project_assign_task`
--

DROP TABLE IF EXISTS `tb_project_assign_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_project_assign_task` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `project_task_id` bigint(20) DEFAULT NULL,
  `assigner_id` bigint(20) DEFAULT NULL COMMENT '分配人ID',
  `assign_time` datetime DEFAULT NULL COMMENT '分配时间',
  `performer_id` bigint(20) DEFAULT NULL COMMENT '执行者ID',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '此任务所属组织的id',
  `status` tinyint(4) DEFAULT NULL COMMENT '0有效 1 无效',
  `remark` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注,组织任务拒绝时存放拒绝原因',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='项目任务分配表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_project_assign_task_copy`
--

DROP TABLE IF EXISTS `tb_project_assign_task_copy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_project_assign_task_copy` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `project_task_id` bigint(20) DEFAULT NULL,
  `assigner_id` bigint(20) DEFAULT NULL COMMENT '分配人ID',
  `assign_time` datetime DEFAULT NULL COMMENT '分配时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '0未分配、1分配中、2分配成功、3分配失败',
  `performer_id` bigint(20) DEFAULT NULL COMMENT '执行者ID',
  `reject_reason` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '拒绝原因',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '所属组织ID',
  `assign_task_group` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='项目任务分配表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_project_delivery`
--

DROP TABLE IF EXISTS `tb_project_delivery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_project_delivery` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `deliverer_id` bigint(20) DEFAULT NULL COMMENT '交付人ID',
  `delivery_organization_id` bigint(20) DEFAULT NULL COMMENT '交付组织ID',
  `project_undertaken_id` bigint(20) DEFAULT NULL COMMENT '项目承接ID',
  `deliver_time` bigint(20) DEFAULT NULL COMMENT '交付时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '0按期完成、1延期完成',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '项目创建人ID',
  `create_organization_id` bigint(20) DEFAULT NULL COMMENT '项目创建组织ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='项目完成交付';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_project_operation`
--

DROP TABLE IF EXISTS `tb_project_operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_project_operation` (
  `id` bigint(20) NOT NULL,
  `project_id` bigint(20) DEFAULT '0' COMMENT '项目id',
  `operation_uid` bigint(20) DEFAULT NULL COMMENT '操作人id',
  `operation_time` datetime DEFAULT NULL COMMENT '操作时间',
  `operation_code` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '操作类型',
  `remark` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注 ：如果上传 存的是项目taskID',
  `organization_task_Id` bigint(20) DEFAULT '0' COMMENT '组织任务id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_project_publish`
--

DROP TABLE IF EXISTS `tb_project_publish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_project_publish` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `start_date` datetime DEFAULT NULL COMMENT '发布起始日',
  `end_date` datetime DEFAULT NULL COMMENT '发布结束日',
  `publisher_id` bigint(20) DEFAULT NULL COMMENT '发布人ID',
  `project_id` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0发布失败、1发布成功、2屏蔽发布、3已过期,4项目完成,5项目放弃,6项目延期,7项目过期未完成,8项目进行中',
  `organization_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='项目发布';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_project_resources_attachment`
--

DROP TABLE IF EXISTS `tb_project_resources_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_project_resources_attachment` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `creater_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `task_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '图片标识ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='项目附件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_project_task`
--

DROP TABLE IF EXISTS `tb_project_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_project_task` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `project_undertaken_id` bigint(20) DEFAULT NULL COMMENT '承接项目ID',
  `title` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '名称',
  `cycle` int(11) DEFAULT NULL COMMENT '任务周期，单位天',
  `start_time` datetime DEFAULT NULL COMMENT '起始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '任务的创建时间',
  `task_pid` bigint(20) DEFAULT NULL COMMENT '父任务ID',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `task_status` tinyint(4) DEFAULT NULL COMMENT '0 准备中 1 已开始 2 已完成 3 已过期',
  `progress` float DEFAULT NULL COMMENT '0-100 范围内的浮点数，不能超过这个范围',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '次任务对应项目的所属组织id',
  `task_description` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `task_type` tinyint(4) DEFAULT NULL COMMENT '0 项目任务 1 组织任务',
  `attach_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '附件id',
  `partition_date` date DEFAULT NULL COMMENT '分区字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='项目任务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_project_undertaken`
--

DROP TABLE IF EXISTS `tb_project_undertaken`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_project_undertaken` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `project_id` bigint(20) DEFAULT NULL COMMENT '承接项目ID',
  `recipient_id` bigint(20) DEFAULT NULL COMMENT '承接人',
  `recipient_organization_id` bigint(20) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL COMMENT '承接项目开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '承接项目结束时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '0项目进行中、1完成、2、放弃、3已过期',
  `publish_id` bigint(20) DEFAULT NULL COMMENT '发布人ID',
  `publish_organization_id` bigint(20) DEFAULT NULL COMMENT '发布人所属组织ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='项目承接表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_resources_local_object`
--

DROP TABLE IF EXISTS `tb_resources_local_object`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_resources_local_object` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `task_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '图片标识ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='我的本地资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_resources_organization_object`
--

DROP TABLE IF EXISTS `tb_resources_organization_object`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_resources_organization_object` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `create_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `task_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '图片标识ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='组织资源文件表';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-15 11:38:24
