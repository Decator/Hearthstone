-- MySQL dump 10.13  Distrib 8.0.13, for macos10.14 (x86_64)
--
-- Host: localhost    Database: hearthstone
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `hero`
--

DROP TABLE IF EXISTS `hero`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `hero` (
  `id` smallint(5) unsigned NOT NULL,
  `type` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `damage` smallint(5) unsigned DEFAULT NULL,
  `nb_summon_hero` smallint(5) unsigned DEFAULT NULL,
  `id_invocation_hero` smallint(5) unsigned DEFAULT NULL,
  `armor_buff_hero` smallint(5) unsigned DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `target_hero` varchar(255) DEFAULT NULL,
  `health_points_hero` smallint(5) DEFAULT '30',
  `armor_points_hero` smallint(5) DEFAULT '0',
  `hero_power_used` tinyint(1) DEFAULT '0',
  `mana_cost` smallint(5) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hero`
--

LOCK TABLES `hero` WRITE;
/*!40000 ALTER TABLE `hero` DISABLE KEYS */;
INSERT INTO `hero` VALUES (1,'mage','Jaina',1,0,0,0,'Boule de feu : Inflige 1 point de dégâts.','all_1_all',30,0,0,0),(2,'paladin','Uther',0,1,12,0,'Renfort : Invoque une recrue de la Main d\'argent 1/1.',NULL,30,0,0,0),(3,'warrior','Garrosh',0,0,0,2,'Gain d\'armure ! : Confère 2 points d\'armure.',NULL,30,0,0,0);
/*!40000 ALTER TABLE `hero` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `minion`
--

DROP TABLE IF EXISTS `minion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `minion` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `mana_cost` smallint(5) unsigned NOT NULL,
  `damage` smallint(5) unsigned DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `health_points_minion` smallint(5) unsigned NOT NULL,
  `taunt_minion` tinyint(1) NOT NULL DEFAULT '0',
  `lifesteal_minion` tinyint(1) NOT NULL DEFAULT '0',
  `charge_minion` tinyint(1) NOT NULL DEFAULT '0',
  `attack_buff_aura_minion` smallint(5) DEFAULT '0',
  `attacked_minion` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `minion`
--

LOCK TABLES `minion` WRITE;
/*!40000 ALTER TABLE `minion` DISABLE KEYS */;
INSERT INTO `minion` VALUES (4,'common','Sanglier brocheroc',1,1,NULL,1,0,0,0,0,1),(5,'common','Soldat du compté-de-l\'or',1,1,'Provocation',2,1,0,0,0,1),(6,'common','Chevaucheur de loup',3,3,'Charge',1,0,0,1,0,1),(7,'common','Chef de raid',3,2,'Vos autres serviteurs ont +1 ATQ.',2,0,0,0,1,1),(8,'common','Yéti noroit',4,4,NULL,5,0,0,0,0,1),(9,'paladin','Champion frisselame',4,3,'Charge, Vol de vie',2,0,1,1,0,1),(10,'warrior','Avocat commis d\'office',2,0,'Provocation',7,1,1,1,0,1),(11,'invocation','Image miroir',0,0,'Provocation',2,1,0,0,0,1),(12,'invocation','Recrue de la Main d\'argent',1,1,NULL,1,0,0,0,0,1),(13,'invocation','Mouton',1,1,NULL,1,0,0,0,0,1);
/*!40000 ALTER TABLE `minion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spell`
--

DROP TABLE IF EXISTS `spell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `spell` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `mana_cost` smallint(5) unsigned NOT NULL,
  `damage` smallint(5) unsigned DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `nb_summon_spell` smallint(5) unsigned DEFAULT NULL,
  `id_invocation_spell` smallint(5) unsigned DEFAULT NULL,
  `attack_buff_spell` smallint(5) unsigned DEFAULT NULL,
  `armor_buff_spell` smallint(5) unsigned DEFAULT NULL,
  `nb_draw_spell` smallint(5) unsigned DEFAULT NULL,
  `polymorph_spell` tinyint(1) NOT NULL DEFAULT '0',
  `target_spell` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spell`
--

LOCK TABLES `spell` WRITE;
/*!40000 ALTER TABLE `Spell` DISABLE KEYS */;
INSERT INTO `spell` VALUES (14,'mage','Image miroir',1,0,'Invoque deux serviteurs 0/2 avec provocation.',2,11,0,0,0,0,NULL),(15,'mage','Explosion des arcanes',2,1,'Inflige 1 point de dégâts à tous les serviteurs adverses.',0,0,0,0,0,0,'minion_all_enemy'),(16,'mage','Métamorphose',4,0,'Transforme un serviteur en mouton 1/1.',1,13,0,0,0,1,'minion_1_enemy'),(17,'paladin','Bénédiction de puissance',1,0,'Confère +3 ATQ à un serviteur.',0,0,3,0,0,0,'minion_1_ally'),(18,'paladin','Consécration',4,2,'Inflige 2 points de dégâts à tous les adversaires.',0,0,0,0,0,0,'all_all_enemy'),(19,'warrior','Tourbillon',1,1,'Inflige 1 point de dégâts à TOUS les serviteurs.',0,0,0,0,0,0,'minion_all_all'),(20,'warrior','Maîtrise du blocage',3,0,'Vous gagnez 5 points d\'armure et vous piochez une carte.',0,0,0,5,1,0,'minion_all_all');
/*!40000 ALTER TABLE `Spell` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-07 23:23:41
