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
  `start` varchar(45) NOT NULL,
  `end` varchar(45) NOT NULL,
  `agenda` text NOT NULL,
  PRIMARY KEY (`meetingID`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meetinglist`
--

LOCK TABLES `meetinglist` WRITE;
/*!40000 ALTER TABLE `meetinglist` DISABLE KEYS */;
INSERT INTO `meetinglist` VALUES (12,'asdasd','04/08/2022','07/08/2022',''),(18,'fghjk','01/09/2022','09/09/2022',''),(23,'Hello','15/09/2022','09/09/2022',''),(24,'jojaju','16/09/2022','18/09/2022',''),(25,'hhhiiiigggd','08/09/2022','16/09/2022',''),(27,'gfgffsdczzzzzz','11/09/2022','16/09/2022',''),(28,'bbbbbbbbbbb','28/09/2022','30/09/2022',''),(36,'Test','09/09/2022','17/09/2022',''),(38,'Test2','09/09/2022','17/09/2022',''),(39,'Test4','03/09/2022','09/09/2022','1.Do this\n2.Do something else');
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

