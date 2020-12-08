create database configserver;
use configserver;

CREATE TABLE `cf_app_file_name` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `app_id` int(11) DEFAULT NULL COMMENT '应用ID',
  `prop_file_name` varchar(45) DEFAULT NULL COMMENT '配置文件名',
  `is_delete` tinyint(4) DEFAULT NULL COMMENT '是否删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `app_id_and_is_delete` (`app_id`,`is_delete`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='app应用里配置文件的名称';

CREATE TABLE `cf_app_name` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(60) DEFAULT NULL COMMENT '应用名',
  `is_delete` tinyint(4) DEFAULT NULL COMMENT '是否删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `app_name_and_is_delete_index` (`app_name`,`is_delete`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `cf_oprater_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prop_conf_id` int(11) DEFAULT NULL COMMENT 'cf_prop_conf.id',
  `type` varchar(45) DEFAULT NULL COMMENT '1：申请 2：审核 3：拆消  4：恢复 ',
  `app_id` int(11) DEFAULT NULL COMMENT '应用ID',
  `before_content` longtext COMMENT '操作前内容',
  `operator` varchar(50) DEFAULT NULL COMMENT '操作人',
  `environment` varchar(60) DEFAULT NULL COMMENT '系统 运行 环境：test staging produce',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  `after_content` longtext COMMENT '操作后内容',
  `app_file_id` int(11) DEFAULT NULL COMMENT 'cf_app_file_name.id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `prop_conf_id_and_type_index` (`prop_conf_id`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='操作日志';

CREATE TABLE `cf_prop_conf` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `app_file_id` int(11) DEFAULT NULL COMMENT 'app中的文件名',
  `app_id` int(11) DEFAULT NULL COMMENT '应用ID',
  `actual_use` int(11) DEFAULT '0' COMMENT '实际使用，1：可使用;2:使用中',
  `content` longtext COMMENT '应用配置项内容',
  `status` int(11) DEFAULT NULL COMMENT '申请状态：0：申请中;1: 审核通过;2:审核不通过',
  `environment` varchar(50) DEFAULT NULL COMMENT '系统 运行 环境 test  | staging | produce',
  `user_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `item_modify_time` datetime DEFAULT NULL COMMENT '有配置项更新的时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `app_id_and_app_file_id_index` (`app_id`,`app_file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='应用配置项';

CREATE TABLE `cf_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(45) DEFAULT NULL COMMENT '用户名',
  `password` varchar(80) DEFAULT NULL COMMENT '密码',
  `role` varchar(45) DEFAULT NULL COMMENT '角色： tl:业务线 tl;  opm:运维 ;  admin：管理 员',
  `is_delete` tinyint(4) DEFAULT NULL COMMENT '是否删除',
  `app_ids` varchar(45) DEFAULT NULL COMMENT '可操作的应用IDS',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `username_password_index` (`username`,`password`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户表';

CREATE TABLE `city` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `spelling` varchar(50) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `is_delete` tinyint(4) DEFAULT NULL,
  `add_date` datetime DEFAULT NULL,
  `last_date` datetime DEFAULT NULL,
  `agency_id` int(11) DEFAULT NULL,
  `frist_word` varchar(5) DEFAULT NULL,
  `simple_spelling` varchar(60) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `citycol` varchar(45) DEFAULT NULL,
  `p1` float DEFAULT NULL,
  `p2` double DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#初始化admin用户，密码为: 111111 #
INSERT INTO `configserver`.`cf_users` (`username`, `password`, `role`, `is_delete`,`app_ids`, `modify_time`, `create_time`) VALUES ('admin', 'dd8635a27fa3d480168cff486533b698', 'admin', '0','', '2015-02-09 18:14:45', '2015-02-09 18:14:45');

/**
ALTER TABLE `configserver`.`cf_oprater_log`
ADD INDEX `prop_conf_id_and_type_index` (`prop_conf_id` ASC, `type` ASC);

ALTER TABLE `configserver`.`cf_app_name`
ADD INDEX `app_name_and_is_delete_index` (`app_name` ASC, `is_delete` ASC);

ALTER TABLE `configserver`.`cf_app_file_name`
ADD INDEX `app_id_and_is_delete` (`app_id` ASC, `is_delete` ASC);

ALTER TABLE `configserver`.`cf_prop_conf`
ADD INDEX `app_id_and_app_file_id_index` (`app_id` ASC, `app_file_id` ASC);

ALTER TABLE `configserver`.`cf_users`
ADD INDEX `username_and_password_index` (`username` ASC, `password` ASC);
*/
