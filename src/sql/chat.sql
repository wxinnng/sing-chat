-- MySQL dump 10.13  Distrib 5.7.19, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: chatroom
-- ------------------------------------------------------
-- Server version	5.7.19

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
-- Table structure for table `application`
--

DROP TABLE IF EXISTS `application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_user` int(11) DEFAULT NULL COMMENT '发起申请的用户',
  `to_user` int(11) DEFAULT NULL COMMENT '接到邀请的用户',
  `type` tinyint(4) DEFAULT NULL COMMENT '群聊还是聊天：聊天：0 群聊 1',
  `status` tinyint(4) DEFAULT NULL COMMENT '0: 待同意 1:已同意  2:已拒绝',
  `create_time` datetime DEFAULT NULL COMMENT '发起的时间',
  `group_id` int(11) DEFAULT NULL COMMENT '组的ID',
  PRIMARY KEY (`id`),
  KEY `application_from_user_index` (`from_user`),
  KEY `application_to_user_index` (`to_user`)
) ENGINE=InnoDB AUTO_INCREMENT=608264240 DEFAULT CHARSET=utf8 COMMENT='申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application`
--

LOCK TABLES `application` WRITE;
/*!40000 ALTER TABLE `application` DISABLE KEYS */;
INSERT INTO `application` VALUES (608264228,1012,1002,0,2,'2024-05-10 16:21:33',NULL),(608264229,1012,1002,0,2,'2024-05-10 16:24:37',NULL),(608264230,1012,1001,0,1,'2024-05-10 16:27:35',NULL),(608264231,1012,1002,0,2,'2024-05-10 16:28:50',NULL),(608264232,1012,1002,0,2,'2024-05-10 16:32:11',NULL),(608264233,1012,1002,0,2,'2024-05-10 16:32:33',NULL),(608264236,1012,1002,1,1,'2024-05-10 16:44:17',10016),(608264237,1012,1002,0,1,'2024-05-10 19:08:23',NULL),(608264238,1014,1001,0,1,'2024-05-11 14:30:31',NULL),(608264239,1002,1014,1,1,'2024-05-11 14:32:46',10017);
/*!40000 ALTER TABLE `application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_info`
--

DROP TABLE IF EXISTS `group_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `group_name` varchar(50) DEFAULT NULL,
  `owner_id` int(11) DEFAULT NULL COMMENT '群主ID，聊天中没有这一项，以此区分二者',
  `cur_num` tinyint(3) unsigned DEFAULT NULL COMMENT '当前群聊中的人数',
  `introduce` varchar(50) DEFAULT NULL COMMENT '群聊介绍',
  `create_time` datetime DEFAULT NULL COMMENT '创建的时间',
  `avatar` varchar(50) DEFAULT NULL COMMENT '群头像',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10018 DEFAULT CHARSET=utf8 COMMENT='群聊和聊天表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_info`
--

LOCK TABLES `group_info` WRITE;
/*!40000 ALTER TABLE `group_info` DISABLE KEYS */;
INSERT INTO `group_info` VALUES (10017,'Group',1014,3,'null','2024-05-11 14:31:51',NULL);
/*!40000 ALTER TABLE `group_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `member` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `group_id` int(11) NOT NULL COMMENT '组ID',
  `is_owner` int(11) DEFAULT NULL COMMENT '是否群主',
  `join_time` datetime DEFAULT NULL COMMENT '加入的时间',
  PRIMARY KEY (`id`),
  KEY `member_group_id_index` (`group_id`),
  KEY `member_user_id_index` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='成员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (13,1014,10017,1,'2024-05-11 14:31:51'),(14,1001,10017,0,'2024-05-11 14:31:51'),(15,1002,10017,0,'2024-05-11 14:32:53');
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '用户ID，唯一',
  `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
  `avatar` varchar(255) NOT NULL DEFAULT '/avatar/default.jpg' COMMENT '用户头像路径（服务端）',
  `password` varchar(64) NOT NULL COMMENT '64位长度的密码',
  `email` varchar(50) DEFAULT NULL COMMENT '用户邮箱',
  `age` tinyint(4) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1015 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1001,'珍德士妮娅','https://singingchat-1322885130.cos.ap-beijing.myqcloud.com/avatar/43741715247372612.jpg','202cb962ac59075b964b07152d234b70','1907822568@qq.com',2,'19846811030'),(1002,'鱼虾一整碗','https://singingchat-1322885130.cos.ap-beijing.myqcloud.com/avatar/69901715246514902.jpg','db502c15530387f98ae67bf0d8edb134','wangxing@qq.com',9,'1090192019'),(1014,'喵喵熙渝怀','https://singingchat-1322885130.cos.ap-beijing.myqcloud.com/avatar/68571715408993428.jpg','202cb962ac59075b964b07152d234b70','miaomiao@qq.com',20,'1902194843');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_relation`
--

DROP TABLE IF EXISTS `user_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户的唯一ID',
  `user_one` int(11) DEFAULT NULL COMMENT '用户1',
  `user_two` int(11) DEFAULT NULL COMMENT '用户2',
  `create_time` datetime DEFAULT NULL COMMENT '加好友的时间',
  PRIMARY KEY (`id`),
  KEY `user_relation__index` (`user_one`)
) ENGINE=InnoDB AUTO_INCREMENT=2118238211 DEFAULT CHARSET=utf8 COMMENT='好友关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_relation`
--

LOCK TABLES `user_relation` WRITE;
/*!40000 ALTER TABLE `user_relation` DISABLE KEYS */;
INSERT INTO `user_relation` VALUES (-524255230,1001,1014,'2024-05-11 14:30:44'),(-524255229,1014,1001,'2024-05-11 14:30:44');
/*!40000 ALTER TABLE `user_relation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-15 10:10:11
