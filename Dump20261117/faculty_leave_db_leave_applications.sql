-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: faculty_leave_db
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `leave_applications`
--

DROP TABLE IF EXISTS `leave_applications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leave_applications` (
  `leave_id` int NOT NULL AUTO_INCREMENT,
  `faculty_id` int NOT NULL,
  `leave_type` varchar(50) NOT NULL,
  `from_date` date NOT NULL,
  `to_date` date NOT NULL,
  `reason` text,
  `contact_no` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `status` enum('Pending','Approved','Rejected') DEFAULT 'Pending',
  `applied_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`leave_id`),
  KEY `faculty_id` (`faculty_id`),
  CONSTRAINT `leave_applications_ibfk_1` FOREIGN KEY (`faculty_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `leave_applications_chk_1` CHECK ((`to_date` >= `from_date`))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leave_applications`
--

LOCK TABLES `leave_applications` WRITE;
/*!40000 ALTER TABLE `leave_applications` DISABLE KEYS */;
INSERT INTO `leave_applications` VALUES (1,1,'Medical Leave','2026-11-25','2026-11-29','bemeri','98204649','kanjug','Rejected','2026-11-03 07:27:36'),(2,1,'Casual Leave','2026-11-25','2026-11-28','kkbkl','345678','tmkc','Approved','2026-11-03 08:14:02'),(3,1,'Earned Leave','2026-11-26','2026-12-10','paisa hi pasisa hogaaaaaa','6377005348','anand006@gmail.com','Approved','2026-11-03 08:15:38'),(4,1,'Casual Leave','2026-12-26','2026-12-29','qww','3216549870','asdasd@','Pending','2026-11-03 08:38:09');
/*!40000 ALTER TABLE `leave_applications` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-17 14:05:34
