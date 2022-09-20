-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: meetings
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `meetinglist`
--

DROP TABLE IF EXISTS `meetinglist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meetinglist` (
  `meetingID` int NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `startDate` varchar(45) NOT NULL,
  `startTime` varchar(45) NOT NULL,
  `endDate` varchar(45) NOT NULL,
  `endTime` varchar(45) NOT NULL,
  `agenda` text NOT NULL,
  PRIMARY KEY (`meetingID`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meetinglist`
--

LOCK TABLES `meetinglist` WRITE;
/*!40000 ALTER TABLE `meetinglist` DISABLE KEYS */;
INSERT INTO `meetinglist` VALUES (1,'TestMeeting','01/01/2000','00:00','31/12/2999','23:59','Test'),(47,'Weekly Standup','13/09/2022','15:00','13/09/2022','15:30','1.First Item\n2.Second Item\n3.Third Item'),(56,'Shopping','02/09/2022','12:30','02/09/2022','13:00','1. Item one\n2. Item two\n3. Item three'),(57,'Panel Exam','30/09/2022','12:30','30/09/2022','13:00','1. Improvise\n2. ???\n3.Profit ');
/*!40000 ALTER TABLE `meetinglist` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

