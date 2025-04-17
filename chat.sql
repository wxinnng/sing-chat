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
) ENGINE=InnoDB AUTO_INCREMENT=608264243 DEFAULT CHARSET=utf8 COMMENT='申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application`
--

LOCK TABLES `application` WRITE;
/*!40000 ALTER TABLE `application` DISABLE KEYS */;
INSERT INTO `application` VALUES (608264241,1017,1016,0,1,'2024-11-03 19:26:52',NULL),(608264242,1018,1016,0,1,'2024-12-28 15:07:47',NULL);
/*!40000 ALTER TABLE `application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_info`
--

DROP TABLE IF EXISTS `file_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_info` (
  `file_id` char(10) NOT NULL COMMENT '文件ID',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户ID',
  `file_md5` varchar(64) DEFAULT NULL COMMENT '文件的md5值',
  `file_pid` char(10) DEFAULT NULL COMMENT '父文件夹',
  `file_size` mediumtext COMMENT '文件的大小',
  `file_name` varchar(100) DEFAULT NULL COMMENT '文件名字',
  `file_path` varchar(100) DEFAULT NULL COMMENT '文件在服务器中的路径',
  `create_time` date DEFAULT NULL COMMENT '文件创建的时间',
  `file_category` tinyint(4) DEFAULT NULL COMMENT '文件类型',
  `state` tinyint(4) DEFAULT NULL COMMENT '传输状态',
  `del_flag` tinyint(4) DEFAULT NULL COMMENT '删除状态',
  PRIMARY KEY (`file_id`),
  KEY `file_info_user_id` (`user_id`),
  KEY `file_info_md5` (`file_md5`),
  CONSTRAINT `file_info_user__fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_info`
--

LOCK TABLES `file_info` WRITE;
/*!40000 ALTER TABLE `file_info` DISABLE KEYS */;
INSERT INTO `file_info` VALUES ('1I2W7X7zRx',1016,'62d1efcbc594f7364886cf3d6a3e24c8','root','2559860','观察者模式实例一.ppt','singing/ppt/962de828-1661-4347-a4ef-0a8489e43b3e观察者模式实例一.ppt','2024-12-28',20,3,2),('2P2KTSMquR',1017,'3331ee92b0a2722cb2f82baa1284fb94','root','127814851','计算机网络（第8版） (谢希仁) (Z-Library).pdf','singing/pdf/10173331ee92b0a2722cb2f82baa1284fb94.pdf','2024-11-03',13,3,2),('4PBhGwul4F',1016,'b806909f392813ae9870e291735ebf5d','root','17145','24-25-1 发展对象支部划分.xlsx','singing/xlsx/e3605e22-3787-474b-9b1a-76f1bf9527ac24-25-1 发展对象支部划分.xlsx','2024-12-28',20,3,2),('5uFwkIhLR4',1017,'bb85cfaa7133fbb54b5c1e5f70e1c98c','root','481655','软工22-3-王星.pdf','singing/pdf/5dd9321f-66f8-443a-bab1-1ec427791ebc软工22-3-王星.pdf','2024-11-03',13,3,2),('abcvUKFriM',1017,'da49523b426d0c84aec81436c819dbbc','root','38075','th.jpg','singing/jpg/ad7ae776-4038-42ba-9ed1-e0ca49214b9fth.jpg','2024-11-03',9,3,2),('Df7R_HmHkv',1016,'668dbb5c89680b439afe0d6935fc2010','root','579021','济南大学.png','singing/png/80983354-43ca-4eba-b0ac-d4aeff3dee21济南大学.png','2025-01-03',14,3,2),('eysGnGVIYv',1016,'62d1efcbc594f7364886cf3d6a3e24c8','root','2559860','观察者模式实例一.ppt','singing/ppt/962de828-1661-4347-a4ef-0a8489e43b3e观察者模式实例一.ppt','2024-12-28',20,3,0),('ftKDEdXurG',1016,'93b8654f5938fd7f4d5d3a413da04e27','J2cCFWoJDL','13715','中安达实习招聘.docx','singing/docx/c8f951be-ba37-4d7b-876f-f27c6cbdbf3a中安达实习招聘.docx','2024-12-28',18,3,0),('hQiC0z9BAu',1017,'1cac5cc1af5d8dcc7b649f7c4cb9cd6c','root','867','aa.txt','singing/txt/4c6e24d6-beb5-4434-a52f-13fc6133bba1aa.txt','2024-11-03',17,3,2),('J2cCFWoJDL',1016,NULL,'root',NULL,'AAA',NULL,'2024-12-28',4,NULL,2),('jYDeSyEOPS',1016,'d08d2f4aa37a833f0af8ef6016153e40','root','18148','王星发展对象考察报告.docx',NULL,'2024-11-04',18,3,0),('mt7YEihYJU',1017,'eff59b5b93700478704cadfdf5293424','root','41930','th (1).jpeg','singing/jpeg/249486ab-f2c7-461e-98cb-e055b5104870th (1).jpeg','2024-11-03',20,3,2),('szhvSZ5W61',1016,'5028384f144319908a66abf1025f0bdf','root','1895673','8b62d7d0-1057-40cb-90c3-2034048dda5d.tmp','singing/tmp/a391ce25-4793-41b0-81a1-86871751cbca8b62d7d0-1057-40cb-90c3-2034048dda5d.tmp','2025-01-03',20,3,2),('szZ4DpOdvW',1016,'93b8654f5938fd7f4d5d3a413da04e27','root','13715','中安达实习招聘.docx','singing/docx/c8f951be-ba37-4d7b-876f-f27c6cbdbf3a中安达实习招聘.docx','2024-12-28',18,3,2),('Ur_ewhUtHW',1016,'5028384f144319908a66abf1025f0bdf','J2cCFWoJDL','1895673','8b62d7d0-1057-40cb-90c3-2034048dda5d.tmp','singing/tmp/a391ce25-4793-41b0-81a1-86871751cbca8b62d7d0-1057-40cb-90c3-2034048dda5d.tmp','2024-12-28',20,3,2),('Uv5yPg2hBj',1019,'c2da73940f3669b6ed71276da6d5d3be','root','1774708','Dormitory.html','singing/html/1ad2cf0a-d801-43ac-9193-4fea7771fd99Dormitory.html','2024-12-28',6,3,2),('xew2mk4W3W',1018,'5028384f144319908a66abf1025f0bdf','root','1895673','8b62d7d0-1057-40cb-90c3-2034048dda5d.tmp','singing/tmp/a391ce25-4793-41b0-81a1-86871751cbca8b62d7d0-1057-40cb-90c3-2034048dda5d.tmp','2024-12-28',20,3,2),('_RuCxJCCrh',1018,'93b8654f5938fd7f4d5d3a413da04e27','root','13715','中安达实习招聘.docx','singing/docx/c8f951be-ba37-4d7b-876f-f27c6cbdbf3a中安达实习招聘.docx','2024-12-28',18,3,0);
/*!40000 ALTER TABLE `file_info` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='群聊和聊天表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_info`
--

LOCK TABLES `group_info` WRITE;
/*!40000 ALTER TABLE `group_info` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='成员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=1020 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1016,'我想的很远~','https://singingchat-1322885130.cos.ap-beijing.myqcloud.com/avatar/10511730631722963.jpeg','db502c15530387f98ae67bf0d8edb134',NULL,90,NULL),(1017,'喵喵兮予怀','https://singingchat-1322885130.cos.ap-beijing.myqcloud.com/avatar/26451730633912776.jpeg','db502c15530387f98ae67bf0d8edb134',NULL,NULL,NULL),(1018,'WangXing111','https://singingchat-1322885130.cos.ap-beijing.myqcloud.com/avatar/56841735350106803.jpg','202cb962ac59075b964b07152d234b70',NULL,NULL,NULL),(1019,'shareFileTest','/avatar/default.jpg','202cb962ac59075b964b07152d234b70',NULL,NULL,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=1950408707 DEFAULT CHARSET=utf8 COMMENT='好友关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_relation`
--

LOCK TABLES `user_relation` WRITE;
/*!40000 ALTER TABLE `user_relation` DISABLE KEYS */;
INSERT INTO `user_relation` VALUES (662794242,1016,1017,'2024-11-03 19:27:06'),(725708802,1017,1016,'2024-11-03 19:27:06'),(1891688449,1016,1018,'2024-12-28 15:07:55'),(1950408706,1018,1016,'2024-12-28 15:07:55');
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

-- Dump completed on 2025-04-17 13:20:20
