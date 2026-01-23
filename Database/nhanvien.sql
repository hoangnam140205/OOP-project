-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: quan_ly_nhan_su
-- ------------------------------------------------------
-- Server version	8.0.44

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
-- Table structure for table `bang_luong`
--

DROP TABLE IF EXISTS `bang_luong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bang_luong` (
  `luong_chinh` double DEFAULT NULL,
  `nam` int NOT NULL,
  `so_gio_lam` double DEFAULT NULL,
  `so_ngay_cong` double DEFAULT NULL,
  `thang` int NOT NULL,
  `thuc_lanh` double DEFAULT NULL,
  `tong_phat` double DEFAULT NULL,
  `tong_thuong` double DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ngay_tao` datetime(6) DEFAULT NULL,
  `ma_nv` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7ill9v1i9x2ot8olcwg8rmfq2` (`ma_nv`),
  CONSTRAINT `FK7ill9v1i9x2ot8olcwg8rmfq2` FOREIGN KEY (`ma_nv`) REFERENCES `nhan_vien` (`ma_nv`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bang_luong`
--

LOCK TABLES `bang_luong` WRITE;
/*!40000 ALTER TABLE `bang_luong` DISABLE KEYS */;
/*!40000 ALTER TABLE `bang_luong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cham_cong`
--

DROP TABLE IF EXISTS `cham_cong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cham_cong` (
  `gio_ra` time(6) DEFAULT NULL,
  `gio_vao` time(6) DEFAULT NULL,
  `ngay_cham_cong` date NOT NULL,
  `so_gio_lam_them` double DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ghi_chu` varchar(255) DEFAULT NULL,
  `ma_nv` varchar(255) NOT NULL,
  `trang_thai` enum('CO_MAT','DI_MUON','NGHI_LE','NGHI_PHEP','VANG') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7mbgiu0agpchoxgjw5kqda53u` (`ma_nv`),
  CONSTRAINT `FK7mbgiu0agpchoxgjw5kqda53u` FOREIGN KEY (`ma_nv`) REFERENCES `nhan_vien` (`ma_nv`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cham_cong`
--

LOCK TABLES `cham_cong` WRITE;
/*!40000 ALTER TABLE `cham_cong` DISABLE KEYS */;
/*!40000 ALTER TABLE `cham_cong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nhan_vien`
--

DROP TABLE IF EXISTS `nhan_vien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nhan_vien` (
  `he_so_luong` double DEFAULT NULL,
  `luong_co_ban` double DEFAULT NULL,
  `luong_theo_gio` double DEFAULT NULL,
  `ngay_sinh` date DEFAULT NULL,
  `ngay_vao_lam` date DEFAULT NULL,
  `so_gio_lam` int DEFAULT NULL,
  `loai_nhan_vien` varchar(31) NOT NULL,
  `chuc_vu` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `ho_ten` varchar(255) DEFAULT NULL,
  `ma_nv` varchar(255) NOT NULL,
  `ma_phong` varchar(255) DEFAULT NULL,
  `so_dien_thoai` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ma_nv`),
  KEY `FKhd1kn3ja7my68at9cfyna4yxb` (`ma_phong`),
  CONSTRAINT `FKhd1kn3ja7my68at9cfyna4yxb` FOREIGN KEY (`ma_phong`) REFERENCES `phong_ban` (`ma_phong`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhan_vien`
--

LOCK TABLES `nhan_vien` WRITE;
/*!40000 ALTER TABLE `nhan_vien` DISABLE KEYS */;
INSERT INTO `nhan_vien` VALUES (NULL,NULL,40000,'1990-10-11','2024-03-29',NULL,'PART_TIME',NULL,'nv1@company.com','Phan Thị Lan','NV001','KT','0905576637'),(NULL,NULL,85000,'1986-12-24','2025-09-29',NULL,'PART_TIME',NULL,'nv2@company.com','Võ Gia Dũng','NV002','IT','0904670182'),(1.3,24000000,NULL,'1992-02-07','2025-12-29',NULL,'FULL_TIME','Nhân viên','nv3@company.com','Phan Quang Dũng','NV003','KD','0907893845'),(1.1,13000000,NULL,'1993-12-08','2024-03-29',NULL,'FULL_TIME','Chuyên viên','nv4@company.com','Huỳnh Gia Dũng','NV004','KD','0901925283'),(1.3,27000000,NULL,'1994-03-15','2024-01-29',NULL,'FULL_TIME','Nhân viên','nv5@company.com','Vũ Đức Hạnh','NV005','IT','0907328117'),(1.2,24000000,NULL,'1992-05-19','2025-01-29',NULL,'FULL_TIME','Nhân viên','nv6@company.com','Phạm Thị Lan','NV006','IT','0900908407'),(NULL,NULL,85000,'1993-11-10','2024-12-29',NULL,'PART_TIME',NULL,'nv7@company.com','Nguyễn Xuân Sơn','NV007','KT','0904468708'),(NULL,NULL,95000,'1995-02-11','2024-03-29',NULL,'PART_TIME',NULL,'nv8@company.com','Nguyễn Đức Lan','NV008','KD','0904253819'),(NULL,NULL,80000,'1996-04-20','2025-04-29',NULL,'PART_TIME',NULL,'nv9@company.com','Trần Thị Lan','NV009','KT','0905051879'),(1.3,27000000,NULL,NULL,NULL,NULL,'FULL_TIME','Nhân viên',NULL,'Huỳnh Đức Sơn','NV010','IT','0906540227'),(1.4,19000000,NULL,'1988-09-14','2025-03-29',NULL,'FULL_TIME','Nhân viên','nv11@company.com','Huỳnh Văn Mai','NV011','KT','0906866211'),(1.2,15000000,NULL,'1996-11-13','2025-01-29',NULL,'FULL_TIME','Chuyên viên','nv12@company.com','Lê Thị Hạnh','NV012','IT','0901973271'),(1.1,16000000,NULL,'1996-06-24','2024-11-29',NULL,'FULL_TIME','Chuyên viên','nv13@company.com','Nguyễn Ngọc Sơn','NV013','HR','0902295098'),(1.4,17000000,NULL,'1999-09-07','2024-10-29',NULL,'FULL_TIME','Chuyên viên','nv14@company.com','Đặng Minh Linh','NV014','IT','0902373716'),(1,10000000,NULL,'1989-04-19','2024-10-29',NULL,'FULL_TIME','Chuyên viên','nv15@company.com','Nguyễn Minh Trang','NV015','IT','0907181434'),(1,14000000,NULL,'1995-08-14','2024-09-29',NULL,'FULL_TIME','Chuyên viên','nv16@company.com','Hoàng Xuân Hạnh','NV016','KT','0902528073'),(1,11000000,NULL,'1999-11-04','2025-06-29',NULL,'FULL_TIME','Chuyên viên','nv17@company.com','Huỳnh Văn Hùng','NV017','KT','0909811557'),(1,29000000,NULL,'1988-03-24','2025-11-29',NULL,'FULL_TIME','Chuyên viên','nv18@company.com','Võ Thành Lan','NV018','HR','0909431148'),(1.4,25000000,NULL,'1986-10-11','2024-01-29',NULL,'FULL_TIME','Chuyên viên','nv19@company.com','Võ Ngọc Hùng','NV019','KD','0907438570'),(1.4,13000000,NULL,'1992-12-21','2024-12-29',NULL,'FULL_TIME','Nhân viên','nv20@company.com','Nguyễn Minh Hùng','NV020','KT','0904351863'),(1.3,22000000,NULL,'1998-11-18','2025-12-29',NULL,'FULL_TIME','Nhân viên','nv21@company.com','Hoàng Ngọc Hương','NV021','IT','0909942330'),(NULL,NULL,60000,'1993-06-10','2024-12-29',NULL,'PART_TIME',NULL,'nv22@company.com','Phan Thành Dũng','NV022','HR','0909112574'),(1.4,12000000,NULL,'1996-07-03','2025-09-29',NULL,'FULL_TIME','Nhân viên','nv23@company.com','Vũ Thu Hạnh','NV023','KT','0902166991'),(1.1,27000000,NULL,'1993-01-12','2025-02-28',NULL,'FULL_TIME','Nhân viên','nv24@company.com','Phạm Quang Trang','NV024','HR','0907936948'),(1,17000000,NULL,'1993-05-05','2025-03-29',NULL,'FULL_TIME','Chuyên viên','nv25@company.com','Huỳnh Ngọc Lan','NV025','KT','0905681994'),(1.1,21000000,NULL,'1986-05-09','2025-11-29',NULL,'FULL_TIME','Chuyên viên','nv26@company.com','Vũ Thu Nam','NV026','KT','0903777345'),(NULL,NULL,65000,'1988-11-19','2024-03-29',NULL,'PART_TIME',NULL,'nv27@company.com','Vũ Văn Mai','NV027','KT','0906833838'),(1,11000000,NULL,'1991-06-17','2025-10-29',NULL,'FULL_TIME','Nhân viên','nv28@company.com','Nguyễn Thu Linh','NV028','KD','0906247823'),(1.3,12000000,NULL,'1987-06-26','2025-02-28',NULL,'FULL_TIME','Nhân viên','nv29@company.com','Vũ Minh Hương','NV029','IT','0903211258'),(1.4,20000000,NULL,'1989-08-18','2025-03-29',NULL,'FULL_TIME','Nhân viên','nv30@company.com','Nguyễn Thị Nam','NV030','HR','0900341880'),(10,100000000,NULL,NULL,NULL,NULL,'FULL_TIME','Trưởng Phòng',NULL,'Nguyễn Hoàng Phong','NV031','IT','0977341380');
/*!40000 ALTER TABLE `nhan_vien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phong_ban`
--

DROP TABLE IF EXISTS `phong_ban`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phong_ban` (
  `ma_phong` varchar(255) NOT NULL,
  `ten_phong` varchar(255) NOT NULL,
  PRIMARY KEY (`ma_phong`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phong_ban`
--

LOCK TABLES `phong_ban` WRITE;
/*!40000 ALTER TABLE `phong_ban` DISABLE KEYS */;
INSERT INTO `phong_ban` VALUES ('HR','Nhân Sự'),('IT','Công Nghệ Thông Tin'),('KD','Kinh Doanh'),('KT','Kế Toán');
/*!40000 ALTER TABLE `phong_ban` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `thuong_phat`
--

DROP TABLE IF EXISTS `thuong_phat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `thuong_phat` (
  `ngay_quyet_dinh` date DEFAULT NULL,
  `so_tien` double NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ly_do` varchar(255) DEFAULT NULL,
  `ma_nv` varchar(255) NOT NULL,
  `loai` enum('PHAT','THUONG') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4rx3bous3xc7e7lmm12kso2iv` (`ma_nv`),
  CONSTRAINT `FK4rx3bous3xc7e7lmm12kso2iv` FOREIGN KEY (`ma_nv`) REFERENCES `nhan_vien` (`ma_nv`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thuong_phat`
--

LOCK TABLES `thuong_phat` WRITE;
/*!40000 ALTER TABLE `thuong_phat` DISABLE KEYS */;
/*!40000 ALTER TABLE `thuong_phat` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-10 21:28:59
