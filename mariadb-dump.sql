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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BoughtFishingRod`
--

LOCK TABLES `BoughtFishingRod` WRITE;
/*!40000 ALTER TABLE `BoughtFishingRod` DISABLE KEYS */;
INSERT INTO `BoughtFishingRod` VALUES
(6,1,1),
(7,1,2),
(8,7,1),
(9,7,2),
(10,7,3),
(11,8,1),
(12,8,2);
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CapturedFish`
--

LOCK TABLES `CapturedFish` WRITE;
/*!40000 ALTER TABLE `CapturedFish` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Token`
--

LOCK TABLES `Token` WRITE;
/*!40000 ALTER TABLE `Token` DISABLE KEYS */;
INSERT INTO `Token` VALUES
(18,'dd855c6f-fbf5-48e8-8aeb-08e40774e4fd',1,'2025-12-16 21:10:16'),
(19,'2b85fd28-a5e5-4341-9db7-9c8a7007dbd8',1,'2026-01-02 17:53:55'),
(20,'9be42ffc-9711-4205-a5eb-3b0f1bce3aa8',1,'2026-01-03 11:42:14'),
(21,'75abc92d-1359-4aed-b65f-fa15eb64b1b5',1,'2026-01-03 12:01:30'),
(22,'1ae0a860-469f-43f3-866d-ab28727aa585',1,'2026-01-03 12:02:15'),
(23,'1c929572-8d01-426e-804a-220022c95ffb',1,'2026-01-12 14:07:08'),
(24,'cb80d65b-ac56-4bed-bf8d-2a87c9a05cab',7,'2026-01-12 15:19:03'),
(25,'2be7f26d-7dbe-4d1c-a5d3-8b5948960c98',7,'2026-01-12 15:20:12'),
(26,'5f75c415-ebfb-47e0-b0bd-6d19a8a67d67',7,'2026-01-12 15:45:13'),
(27,'7bc1acad-b511-48db-83e4-20ea7e81bb19',7,'2026-01-12 16:31:36'),
(28,'4ca45814-6d85-44ef-a8fc-b22dcbc30964',7,'2026-01-12 15:46:31'),
(29,'51168466-9644-45e5-9a89-2fee2d117e3a',8,'2026-01-12 15:58:45'),
(30,'ac1370d8-8c4d-4134-88bb-7db425318ad2',8,'2026-01-12 16:33:22'),
(31,'925eedbb-ecc4-4b31-8b9c-bd16ffb24255',7,'2026-01-12 17:17:49');
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
  `equippedFishingRodId` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `USERNAME` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES
(1,'root','root@root.com','root',99700,1),
(5,'Palomo','palomo@asda','123456',1000,1),
(6,'Jose','aa@aa','123456',1000,1),
(7,'casa','casa@asd','CasaMala04$',250,3),
(8,'queso','queso@gmail','QuesoQueso11&',750,2);
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

-- Dump completed on 2026-01-12 17:19:53
