/*
SQLyog Community v13.2.0 (64 bit)
MySQL - 8.0.31 : Database - gseek
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`gseek` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `gseek`;

/*Table structure for table `admin` */

DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
  `admin_id` int NOT NULL AUTO_INCREMENT COMMENT '管理员id',
  `admin_password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号密码',
  `admin_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号名',
  `salt` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '盐值',
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `admin` */

/*Table structure for table `appeal` */

DROP TABLE IF EXISTS `appeal`;

CREATE TABLE `appeal` (
  `user_id` int NOT NULL COMMENT '用户id',
  `appeal_reason` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '申诉原因',
  `appeal_result` tinyint(1) DEFAULT NULL COMMENT '申诉结果',
  `chencked` tinyint(1) DEFAULT NULL COMMENT '申诉是否被处理',
  `admin_id` int DEFAULT NULL COMMENT '处理申诉管理员id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `appeal` */

/*Table structure for table `bill` */

DROP TABLE IF EXISTS `bill`;

CREATE TABLE `bill` (
  `bill_id` int NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `good_id` int NOT NULL COMMENT '商品id',
  `create_time` bigint NOT NULL COMMENT '创建时间',
  `completed` tinyint(1) DEFAULT '0' COMMENT '是否完成',
  `seller_id` int NOT NULL COMMENT '卖家id',
  `buyer_id` int NOT NULL COMMENT '买家id',
  `delivered` tinyint(1) DEFAULT '0' COMMENT '是否收货',
  `delivered_time` bigint DEFAULT NULL COMMENT '收货时间',
  `inspected` tinyint(1) DEFAULT '0' COMMENT '是否验货',
  `inspected_time` bigint DEFAULT NULL COMMENT '验货时间',
  PRIMARY KEY (`bill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单基本信息';

/*Data for the table `bill` */

/*Table structure for table `blacklist` */

DROP TABLE IF EXISTS `blacklist`;

CREATE TABLE `blacklist` (
  `claimer_id` int NOT NULL COMMENT '申诉人id',
  `respondent_id` int NOT NULL COMMENT '被申诉人id',
  `appeal_reason` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '申诉原因',
  `appeal_result` tinyint(1) DEFAULT NULL COMMENT '申诉结果',
  `checked` tinyint(1) NOT NULL DEFAULT '0' COMMENT '申诉是否被处理',
  `admin_id` int DEFAULT NULL COMMENT '处理申诉的管理员id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `blacklist` */

/*Table structure for table `chat` */

DROP TABLE IF EXISTS `chat`;

CREATE TABLE `chat` (
  `good_id` int NOT NULL COMMENT '商品id,表示在哪个商品下进行的对话',
  `from_user_id` int NOT NULL COMMENT '发送用户id',
  `to_user_id` int NOT NULL COMMENT '接收用户id',
  `text` varchar(300) DEFAULT NULL COMMENT '聊天内容,若为[pic]开头即为图片url',
  `send_time` bigint NOT NULL COMMENT '发送时间',
  PRIMARY KEY (`good_id`,`from_user_id`,`to_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='聊天记录表';

/*Data for the table `chat` */

/*Table structure for table `good` */

DROP TABLE IF EXISTS `good`;

CREATE TABLE `good` (
  `good_id` int NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `good_name` varchar(100) NOT NULL COMMENT '商品名',
  `account` varchar(100) NOT NULL COMMENT '游戏账号',
  `account_password` varchar(100) NOT NULL COMMENT '游戏账号密码',
  `good_text` varchar(1000) DEFAULT NULL COMMENT '商品描述',
  `own_user_id` int NOT NULL COMMENT '持有者id',
  `good_price` decimal(13,2) DEFAULT '0.00' COMMENT '价格',
  `create_time` bigint NOT NULL COMMENT '创建时间',
  `modified_time` bigint DEFAULT '0' COMMENT '修改时间',
  `sold` tinyint(1) DEFAULT '0' COMMENT '是否卖出',
  PRIMARY KEY (`good_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品基本信息';

/*Data for the table `good` */

/*Table structure for table `good_fav` */

DROP TABLE IF EXISTS `good_fav`;

CREATE TABLE `good_fav` (
  `user_id` int NOT NULL COMMENT '收藏用户id',
  `good_id` int NOT NULL COMMENT '收藏商品id',
  `fav_time` bigint NOT NULL DEFAULT '0' COMMENT '收藏时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品收藏表';

/*Data for the table `good_fav` */

/*Table structure for table `good_tag` */

DROP TABLE IF EXISTS `good_tag`;

CREATE TABLE `good_tag` (
  `good_id` int NOT NULL COMMENT '商品id',
  `tag_id` int NOT NULL COMMENT 'tag ID',
  PRIMARY KEY (`good_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品-tag表,记录商品对应的tag';

/*Data for the table `good_tag` */

/*Table structure for table `money` */

DROP TABLE IF EXISTS `money`;

CREATE TABLE `money` (
  `user_id` int NOT NULL COMMENT '用户id',
  `money` decimal(13,2) DEFAULT '0.00' COMMENT '余额',
  `frozen` tinyint(1) DEFAULT '0' COMMENT '余额是否冻结',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户账户余额';

/*Data for the table `money` */

/*Table structure for table `tag` */

DROP TABLE IF EXISTS `tag`;

CREATE TABLE `tag` (
  `tag_id` int NOT NULL AUTO_INCREMENT COMMENT 'tag ID',
  `tag_text` varchar(100) NOT NULL COMMENT 'tag内容',
  PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='tag表,保存tag信息';

/*Data for the table `tag` */

/*Table structure for table `user_identification` */

DROP TABLE IF EXISTS `user_identification`;

CREATE TABLE `user_identification` (
  `user_id` int NOT NULL COMMENT '用户id',
  `id_number` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '000000000000000000' COMMENT '身份证号',
  `certified` tinyint(1) DEFAULT '0' COMMENT '是否认证',
  `modified_time` bigint DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户实名认证信息';

/*Data for the table `user_identification` */

/*Table structure for table `user_information` */

DROP TABLE IF EXISTS `user_information`;

CREATE TABLE `user_information` (
  `user_id` int NOT NULL COMMENT '用户id',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户邮箱',
  `profile_photo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像url',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户一般信息';

/*Data for the table `user_information` */

/*Table structure for table `user_password` */

DROP TABLE IF EXISTS `user_password`;

CREATE TABLE `user_password` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码,md5加密',
  `salt` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密匙盐',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户密码表.使用md5加密,储存对应的盐';

/*Data for the table `user_password` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
