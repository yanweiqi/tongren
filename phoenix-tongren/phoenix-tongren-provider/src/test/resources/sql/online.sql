
CREATE DATABASE /*!32312 IF NOT EXISTS*/`phoenix_demand` ;

USE `phoenix_demand`;

/*Table structure for table `tb_connect_info` */

DROP TABLE IF EXISTS `tb_connect_info`;

CREATE TABLE `tb_connect_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `demandId` bigint(20) DEFAULT NULL COMMENT '知识ID',
  `tag` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联标签',
  `connType` int(1) DEFAULT NULL COMMENT '关联类型(2-p:人脉1-,r:事件5-,o:组织,6-k:知识)',
  `connId` bigint(20) DEFAULT NULL COMMENT '关联数据ID',
  `connName` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联数据名称',
  `ownerId` bigint(20) DEFAULT NULL COMMENT '拥有者ID',
  `owner` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '拥有者名称',
  `requirementtype` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '需求类型(需求使用)',
  `career` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '职业（人使用）',
  `company` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公司（人使用）',
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址（组织使用）',
  `hy` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '行业（组织使用）',
  `columnPath` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '栏目路径(知识使用)',
  `columnType` int(2) DEFAULT NULL COMMENT '栏目类型（知识使用）',
  `url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '跳转URL',
  `picPath` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像路径',
  `allasso` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='关联信息表';

/*Table structure for table `tb_demand_category` */

DROP TABLE IF EXISTS `tb_demand_category`;

CREATE TABLE `tb_demand_category` (
  `demand_id` bigint(20) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='需求目录关系表';

/*Table structure for table `tb_demand_report` */

DROP TABLE IF EXISTS `tb_demand_report`;

CREATE TABLE `tb_demand_report` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT,
  `content` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '举报内容',
  `reason` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '举报理由',
  `contact` bigint(32) DEFAULT NULL COMMENT '联系人电话',
  `user_id` bigint(32) DEFAULT NULL COMMENT '举报人id(暂时不显示)',
  `user_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '举报人姓名(暂时不显示)',
  `demand_id` bigint(32) DEFAULT NULL COMMENT '需求id',
  `create_time` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '举报时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `tb_demand_tag` */

DROP TABLE IF EXISTS `tb_demand_tag`;

CREATE TABLE `tb_demand_tag` (
  `demand_id` bigint(20) DEFAULT NULL,
  `tag_id` bigint(20) DEFAULT NULL,
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='需求标签关系表';

/*Table structure for table `tb_user_category` */

DROP TABLE IF EXISTS `tb_user_category`;

CREATE TABLE `tb_user_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户Id',
  `category_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '目录名称',
  `sort_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路径Id，000000001000000001',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='目录表(左树)';

/*Table structure for table `tb_user_demand_permission` */

DROP TABLE IF EXISTS `tb_user_demand_permission`;

CREATE TABLE `tb_user_demand_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `demandId` bigint(20) DEFAULT NULL COMMENT '需求ID',
  `sendId` bigint(20) DEFAULT NULL COMMENT '发送者ID',
  `receiveId` bigint(20) DEFAULT NULL COMMENT '接收者ID',
  `demandType` int(1) DEFAULT NULL COMMENT '投融资需求类型',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `pType` int(1) DEFAULT NULL COMMENT 'ptype（权限类型，1：独乐 , 2：大乐，3：中乐，4：小乐）',
  `isPush` int(1) DEFAULT '0' COMMENT '是否是大数据推荐(1-是 0-否)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=997 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户需求权限表';

/*Table structure for table `tb_user_tag` */

DROP TABLE IF EXISTS `tb_user_tag`;

CREATE TABLE `tb_user_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `tag` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户标签表';


use `phoenix_file`
ALTER TABLE tb_file_index ADD fileType INT(1) DEFAULT '3' COMMENT '文件类型（1-图片，2-视频，3-附件）';
ALTER TABLE tb_file_index ADD thumbnailsPath VARCHAR(255) COMMENT '缩略图路径';
ALTER TABLE tb_file_index ADD isTranscoding INT(1) DEFAULT 0 COMMENT '是否转码（1-是，0-否）';