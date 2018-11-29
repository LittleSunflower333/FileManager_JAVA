/*
SQLyog Ultimate v12.3.1 (64 bit)
MySQL - 5.6.36 : Database - kitty
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`kitty` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `kitty`;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `create_by` varchar(100) NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1068024549741101058 DEFAULT CHARSET=utf8 COMMENT='用户';

/*Data for the table `user` */

insert  into `user`(`id`,`user_name`,`password`,`create_by`) values
(1062916940776337409,'admin','654321',''),
(1068009205718724610,'tom','123456',''),
(1068024034068205569,'123456','123456','admin'),
(1068024452852678657,'8888','8888','admin'),
(1068024549741101057,'898989','','admin');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
