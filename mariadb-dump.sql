/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.4.7-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: fish_and_chill
-- ------------------------------------------------------
-- Server version	11.4.7-MariaDB-0ubuntu0.25.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `BoughtFishingRod`
--

DROP TABLE IF EXISTS `BoughtFishingRod`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `BoughtFishingRod` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `fishingRodId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `USERID_BFR` (`userId`),
  KEY `FISHINGROD_BFR` (`fishingRodId`),
  CONSTRAINT `FISHINGROD_BFR` FOREIGN KEY (`fishingRodId`) REFERENCES `FishingRod` (`id`),
  CONSTRAINT `USERID_BFR` FOREIGN KEY (`userId`) REFERENCES `User` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BoughtFishingRod`
--

LOCK TABLES `BoughtFishingRod` WRITE;
/*!40000 ALTER TABLE `BoughtFishingRod` DISABLE KEYS */;
INSERT INTO `BoughtFishingRod` VALUES
(1,2,1),
(2,2,2),
(3,2,3),
(4,4,3),
(5,4,2);
/*!40000 ALTER TABLE `BoughtFishingRod` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CapturedFish`
--

DROP TABLE IF EXISTS `CapturedFish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `CapturedFish` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `fishId` int(11) NOT NULL,
  `weight` float DEFAULT NULL,
  `captureTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FISHID_CF` (`fishId`),
  KEY `USERID_CF` (`userId`),
  CONSTRAINT `FISHID_CF` FOREIGN KEY (`fishId`) REFERENCES `Fish` (`id`),
  CONSTRAINT `USERID_CF` FOREIGN KEY (`userId`) REFERENCES `User` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CapturedFish`
--

LOCK TABLES `CapturedFish` WRITE;
/*!40000 ALTER TABLE `CapturedFish` DISABLE KEYS */;
INSERT INTO `CapturedFish` VALUES
(1,2,1,23,'2025-11-22 11:24:48'),
(2,2,2,32,'2025-11-22 12:32:15'),
(3,2,1,25,'2025-11-22 13:01:32'),
(4,4,1,49,'2025-11-23 23:54:44');
/*!40000 ALTER TABLE `CapturedFish` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Fish`
--

DROP TABLE IF EXISTS `Fish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Fish` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `speciesName` varchar(50) NOT NULL,
  `speciesWeight` float NOT NULL,
  `rarity` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `SPECIESNAME` (`speciesName`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Fish`
--

LOCK TABLES `Fish` WRITE;
/*!40000 ALTER TABLE `Fish` DISABLE KEYS */;
INSERT INTO `Fish` VALUES
(1,'Trout',10,1),
(2,'Salmon',32,1),
(3,'Bass',34,1),
(4,'Catfish',53,2),
(5,'Carp',45,2),
(6,'Moonfish',82,3);
/*!40000 ALTER TABLE `Fish` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FishingRod`
--

DROP TABLE IF EXISTS `FishingRod`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `FishingRod` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `speed` float NOT NULL,
  `power` float NOT NULL,
  `rarity` int(11) NOT NULL,
  `durability` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `FISHINGRODNAME` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FishingRod`
--

LOCK TABLES `FishingRod` WRITE;
/*!40000 ALTER TABLE `FishingRod` DISABLE KEYS */;
INSERT INTO `FishingRod` VALUES
(1,'basic',1,1,1,50,100),
(2,'iron',1.5,2,1,120,250),
(3,'golden',2,3,2,200,500),
(4,'crystal',2.3,3.5,2,250,700),
(5,'titanium',3,5,3,350,1300),
(6,'mythic',3.5,6,4,400,1700),
(7,'dragon',4,7,4,500,2200),
(8,'void',4.5,8,5,600,3000),
(9,'celestial',5,9.5,5,800,4000);
/*!40000 ALTER TABLE `FishingRod` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Token`
--

DROP TABLE IF EXISTS `Token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `token` varchar(100) NOT NULL,
  `userId` int(11) NOT NULL,
  `lastAccess` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `TOKEN` (`token`),
  KEY `Token_User_id_fk` (`userId`),
  CONSTRAINT `Token_User_id_fk` FOREIGN KEY (`userId`) REFERENCES `User` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Token`
--

LOCK TABLES `Token` WRITE;
/*!40000 ALTER TABLE `Token` DISABLE KEYS */;
INSERT INTO `Token` VALUES
(1,'9cecc8fd-0f5d-4abb-bc6d-6d0677772c89',4,'2025-11-24 00:32:22'),
(2,'d69e9ed7-f792-4879-be48-c3c269c4998a',4,'2025-11-24 00:37:31'),
(3,'12e9e9b9-a1c1-4cbc-92b3-84deeb9b4322',4,'2025-11-24 00:38:22'),
(4,'89b2e1e5-06af-437d-9fb1-cbe859ecf748',4,'2025-11-24 00:39:43'),
(5,'94e9e115-e4fd-44ca-95fc-ec416f15070e',4,'2025-11-24 00:40:33'),
(6,'cb199c93-41fc-4a5c-9857-bb367e46403f',4,'2025-11-24 00:41:05'),
(7,'c9a14767-7b94-40e2-ac3b-b0845caeaf53',4,'2025-11-24 00:46:24'),
(9,'728739fb-f814-41b6-9ce3-aaddd903d47b',4,'2025-11-24 15:11:01');
/*!40000 ALTER TABLE `Token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `coins` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `USERNAME` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES
(1,'root','root@root.com','root',100000),
(2,'midPlayer','midplayer@upc.edu','123321',1234),
(4,'jairo','jairo@upd.edu','hola',1106);
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2025-11-25 12:02:55
