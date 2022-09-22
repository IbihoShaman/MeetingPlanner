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
-- Table structure for table `meetingnotes`
--

DROP TABLE IF EXISTS `meetingnotes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meetingnotes` (
  `noteID` int NOT NULL AUTO_INCREMENT,
  `meetingID` int NOT NULL,
  `noteText` text NOT NULL,
  PRIMARY KEY (`noteID`),
  UNIQUE KEY `noteID_UNIQUE` (`noteID`),
  KEY `meetingID_idx` (`meetingID`),
  CONSTRAINT `meetingID` FOREIGN KEY (`meetingID`) REFERENCES `meetinglist` (`meetingID`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meetingnotes`
--

LOCK TABLES `meetingnotes` WRITE;
/*!40000 ALTER TABLE `meetingnotes` DISABLE KEYS */;
INSERT INTO `meetingnotes` VALUES (1,1,'Adding note from crudLogicNotes unit test'),(20,47,'All participants have been informed'),(21,47,'Conference room has been booked'),(32,56,'Supermarket closed on sunday'),(33,56,'Use coupons'),(35,57,'Add unit tests'),(36,57,'Finish protocol'),(44,1,'Adding note from crudLogicNotes unit test'),(45,1,'Adding note from crudLogicNotes unit test');
/*!40000 ALTER TABLE `meetingnotes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

