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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `admin` */

/*Table structure for table `appeal_buyer_to_seller` */

CREATE TABLE `appeal_buyer_to_seller` (
  `appeal_id` int NOT NULL AUTO_INCREMENT COMMENT '申报id',
  `appeal_reason` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '申诉原因',
  `appeal_result` tinyint(1) DEFAULT NULL COMMENT '申诉结果',
  `chencked` tinyint(1) DEFAULT NULL COMMENT '申诉是否被处理',
  `admin_id` int DEFAULT NULL COMMENT '处理申诉管理员id',
  `provePic` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '证明照片',
  `bill_id` int DEFAULT NULL COMMENT '商品id',
  `my_id` int DEFAULT NULL COMMENT '申诉者id',
  PRIMARY KEY (`appeal_id`),
  KEY `appeal_id` (`appeal_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `appeal_buyer_to_seller` */

/*Table structure for table `appeal_seller_to_buyer` */

CREATE TABLE `appeal_seller_to_buyer` (
  `appeal_id` int NOT NULL AUTO_INCREMENT COMMENT '申报id',
  `appeal_reason` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '申报原因',
  `pic_before` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '账号受损前图片',
  `pic_after` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '账号受损后图片',
  `accept` tinyint(1) DEFAULT '1' COMMENT '卖家是否接受调解',
  `bill_id` int DEFAULT NULL COMMENT '订单id',
  `chencked` tinyint(1) DEFAULT '0' COMMENT '申诉是否被处理',
  `admin_id` int DEFAULT NULL COMMENT '审核管理员id',
  `appeal_result` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '审核结果',
  `my_id` int DEFAULT NULL COMMENT '申诉者id',
  PRIMARY KEY (`appeal_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `appeal_seller_to_buyer` */


/*Table structure for table `bill` */

DROP TABLE IF EXISTS `bill`;

CREATE TABLE `bill` (
  `bill_id` int NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `good_id` int NOT NULL COMMENT '商品id',
  `create_time` bigint NOT NULL COMMENT '创建时间',
  `completed` tinyint(1) DEFAULT '0' COMMENT '是否完成',
  `seller_id` int NOT NULL COMMENT '卖家id',
  `buyer_id` int NOT NULL COMMENT '买家id',
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
  `admin_id` int DEFAULT NULL COMMENT '处理申诉的管理员id',
  `black_id` int NOT NULL AUTO_INCREMENT COMMENT '黑名单id',
  `disapprove_reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '不通过举报原因',
  `provePic` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '举报图片',
  KEY `black_id` (`black_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `blacklist` */

/*Table structure for table `bought_good` */

DROP TABLE IF EXISTS `bought_good`;

CREATE TABLE `bought_good` (
  `good_id` int NOT NULL COMMENT '商品id',
  `bought_time` bigint NOT NULL COMMENT '成交时间',
  `bought_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '成交价格',
  PRIMARY KEY (`good_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='记录用户购买过的商品\r\n';

/*Data for the table `bought_good` */

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
  `account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '游戏账号（已加密）',
  `account_password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '游戏账号密码（已加密）',
  `good_text` varchar(1000) DEFAULT NULL COMMENT '商品描述',
  `own_user_name` varchar(100) NOT NULL COMMENT '持有者名字',
  `own_user_id` int NOT NULL COMMENT '持有者id',
  `good_price` decimal(13,2) DEFAULT '0.00' COMMENT '价格',
  `create_time` bigint NOT NULL COMMENT '创建时间',
  `modified_time` bigint DEFAULT '0' COMMENT '修改时间',
  `sold` tinyint(1) DEFAULT '0' COMMENT '是否卖出',
  `type_tag_id` int NOT NULL COMMENT '商品类型tagId',
  `type_tag_name` varchar(100) NOT NULL COMMENT '商品类型tag名称',
  PRIMARY KEY (`good_id`,`own_user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品基本信息';

/*Data for the table `good` */

insert  into `good`(`good_id`,`good_name`,`account`,`account_password`,`good_text`,`own_user_name`,`own_user_id`,`good_price`,`create_time`,`modified_time`,`sold`,`type_tag_id`,`type_tag_name`) values 
(1,'test','testAccount','testAccountPassword','testText','testUser',1,0.00,1234567891234,1234567891234,0,2,'test1');

/*Table structure for table `good_cover_pic` */

DROP TABLE IF EXISTS `good_cover_pic`;

CREATE TABLE `good_cover_pic` (
  `good_id` int NOT NULL COMMENT '商品id',
  `cover_picture_url` varchar(200) NOT NULL COMMENT '封面图片url'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='记录商品与封面图片关系';

/*Data for the table `good_cover_pic` */

insert  into `good_cover_pic`(`good_id`,`cover_picture_url`) values 
(1,'4'),
(1,'5'),
(1,'/test/path1'),
(1,'/test/path2'),
(1,'/test/path3');

/*Table structure for table `good_detail_pic` */

DROP TABLE IF EXISTS `good_detail_pic`;

CREATE TABLE `good_detail_pic` (
  `good_id` int NOT NULL COMMENT '商品id',
  `detail_picture_url` varchar(200) NOT NULL COMMENT '详情图片url'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='记录商品与详情图片关系';

/*Data for the table `good_detail_pic` */

insert  into `good_detail_pic`(`good_id`,`detail_picture_url`) values 
(1,'1'),
(1,'2'),
(1,'3');

/*Table structure for table `good_fav` */

DROP TABLE IF EXISTS `good_fav`;

CREATE TABLE `good_fav` (
  `user_id` int NOT NULL COMMENT '收藏用户id',
  `good_id` int NOT NULL COMMENT '收藏商品id',
  `fav_time` bigint NOT NULL DEFAULT '0' COMMENT '收藏时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品收藏表';

/*Data for the table `good_fav` */

/*Table structure for table `good_tag` */

DROP TABLE IF EXISTS `good_tag`;

CREATE TABLE `good_tag` (
  `good_id` int NOT NULL COMMENT '商品id',
  `tag_id` int NOT NULL COMMENT 'tag ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品-tag表,记录商品对应的tag';

/*Data for the table `good_tag` */

insert  into `good_tag`(`good_id`,`tag_id`) values 
(1,2),
(2,4);

/*Table structure for table `money` */

DROP TABLE IF EXISTS `money`;

CREATE TABLE `money` (
  `user_id` int NOT NULL COMMENT '用户id',
  `money` decimal(13,2) DEFAULT '0.00' COMMENT '余额',
  `frozen` tinyint(1) DEFAULT '0' COMMENT '余额是否冻结',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户账户余额';

/*Data for the table `money` */

insert  into `money`(`user_id`,`money`,`frozen`) values 
(1,0.00,0),
(2,0.00,0);

/*Table structure for table `offer_price` */

DROP TABLE IF EXISTS `offer_price`;

CREATE TABLE `offer_price` (
  `bidder_id` int NOT NULL COMMENT '出价者id',
  `seller_id` int NOT NULL COMMENT '售卖者id',
  `offer_price` decimal(13,2) NOT NULL COMMENT '出价',
  `good_id` int NOT NULL COMMENT '商品id',
  `offer_time` bigint NOT NULL COMMENT '出价时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `offer_price` */

insert  into `offer_price`(`bidder_id`,`seller_id`,`offer_price`,`good_id`,`offer_time`) values 
(1,2,33.30,1,1),
(1,1,2.00,1,1212121);

/*Table structure for table `picture（弃用）` */

DROP TABLE IF EXISTS `picture（弃用）`;

CREATE TABLE `picture（弃用）` (
  `picture_id` int NOT NULL AUTO_INCREMENT COMMENT '图片id',
  `picture_url` varchar(200) NOT NULL COMMENT '图片url',
  PRIMARY KEY (`picture_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='记录图片id与对应url';

/*Data for the table `picture（弃用）` */

insert  into `picture（弃用）`(`picture_id`,`picture_url`) values 
(1,'/test/1'),
(2,'/test/2'),
(3,'/test/3'),
(4,'/test/4'),
(5,'/test/5');

/*Table structure for table `tag` */

DROP TABLE IF EXISTS `tag`;

CREATE TABLE `tag` (
  `tag_id` int NOT NULL AUTO_INCREMENT COMMENT 'tag ID',
  `tag_text` varchar(100) NOT NULL COMMENT 'tag内容',
  `click` int DEFAULT '0' COMMENT '点击数',
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `tag_text` (`tag_text`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='tag表,保存tag信息';

/*Data for the table `tag` */

insert  into `tag`(`tag_id`,`tag_text`,`click`) values 
(2,'text1',0),
(3,'text2',0),
(4,'text3',0),
(13,'aaa',0),
(14,'bbb',0),
(15,'1',0);

/*Table structure for table `user_identification` */

DROP TABLE IF EXISTS `user_identification`;

CREATE TABLE `user_identification` (
  `user_id` int NOT NULL COMMENT '用户id',
  `id_number` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '000000000000000000' COMMENT '身份证号（已加密）',
  `certified` tinyint(1) DEFAULT '0' COMMENT '是否认证',
  `modified_time` bigint DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户实名认证信息';

/*Data for the table `user_identification` */

insert  into `user_identification`(`user_id`,`id_number`,`certified`,`modified_time`) values 
(1,'000000000000000000',0,1683043875814),
(2,'000000000000000000',0,16830431235814);

/*Table structure for table `user_information` */

DROP TABLE IF EXISTS `user_information`;

CREATE TABLE `user_information` (
  `user_id` int NOT NULL COMMENT '用户id',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户邮箱',
  `profile_photo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '/imgs/profile_photos/default.jpg' COMMENT '头像url',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户一般信息';

/*Data for the table `user_information` */

insert  into `user_information`(`user_id`,`email`,`profile_photo`) values 
(1,'2967306689@qq.com','/imgs/profile_photos/default.jpg'),
(2,'2967306689@qq.com','/imgs/profile_photos/default.jpg');

/*Table structure for table `user_password` */

DROP TABLE IF EXISTS `user_password`;

CREATE TABLE `user_password` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码,md5加密',
  `salt` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密匙盐',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户密码表.使用md5加密,储存对应的盐';

/*Data for the table `user_password` */

insert  into `user_password`(`user_id`,`user_name`,`password`,`salt`) values 
(1,'user1','user1','397924423F4528482B4D6251655468576D5A7134743777217A25432A46294A40'),
(2,'user2','user2','397924423F4528482B4D6251655468576D5A7134743777217A25432A46294A40');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
