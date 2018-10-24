-- MySQL dump 10.13  Distrib 8.0.12, for Win64 (x86_64)
--
-- Host: localhost    Database: banka
-- ------------------------------------------------------
-- Server version	8.0.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `logs`
--

DROP TABLE IF EXISTS `logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `logs` (
  `lognumber` int(11) NOT NULL AUTO_INCREMENT,
  `user` int(11) NOT NULL COMMENT 'identifiziert über Kontonummer',
  `log` text,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`lognumber`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logs`
--

LOCK TABLES `logs` WRITE;
/*!40000 ALTER TABLE `logs` DISABLE KEYS */;
INSERT INTO `logs` VALUES (1,6077,'banka - WireTransfer - 100 - 1337 - banka','2018-10-23 14:16:21'),(2,1337,'banka - WireTransfer - -100 - 6077 - banka','2018-10-23 14:16:21'),(3,1337,'banka - Withdraw - 1000 - 1337 - banka','2018-10-23 14:47:10'),(4,1337,'banka - Deposit - 2500 - 1337 - banka','2018-10-23 14:47:13'),(5,1337,'banka - Deposit - 3500 - 1337 - banka','2018-10-23 14:47:15'),(6,1337,'banka - Deposit - 1279 - 1337 - banka','2018-10-23 14:47:19'),(7,1337,'banka - Withdraw - 1172 - 1337 - banka','2018-10-23 14:47:22'),(8,1337,'banka - Deposit - 593 - 1337 - banka','2018-10-23 14:47:31'),(9,1337,'banka - Withdraw - 2000 - 1337 - banka','2018-10-23 14:47:34'),(10,1337,'banka - Deposit - 4000 - 1337 - banka','2018-10-23 14:47:36');
/*!40000 ALTER TABLE `logs` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-10-24  7:01:39
