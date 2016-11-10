-- phpMyAdmin SQL Dump
-- version 4.6.3deb1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Creato il: Nov 10, 2016 alle 17:08
-- Versione del server: 5.6.30-1
-- Versione PHP: 7.0.8-5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `progetto_ai`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `UserConnection`
--
DROP TABLE IF EXISTS `UserConnection`;
CREATE TABLE `UserConnection` (
  `userId` varchar(255) NOT NULL,
  `providerId` varchar(255) NOT NULL,
  `providerUserId` varchar(255) NOT NULL DEFAULT '',
  `rank` int(11) NOT NULL,
  `displayName` varchar(255) DEFAULT NULL,
  `profileUrl` varchar(512) DEFAULT NULL,
  `imageUrl` varchar(512) DEFAULT NULL,
  `accessToken` varchar(512) NOT NULL,
  `secret` varchar(512) DEFAULT NULL,
  `refreshToken` varchar(512) DEFAULT NULL,
  `expireTime` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `UserConnection`
--
ALTER TABLE `UserConnection`
  ADD PRIMARY KEY (`userId`,`providerId`,`providerUserId`),
  ADD UNIQUE KEY `UserConnectionRank` (`userId`,`providerId`,`rank`);

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `UserConnection`
--
ALTER TABLE `UserConnection`
  ADD CONSTRAINT `fk_uc_u` FOREIGN KEY (`userId`) REFERENCES `userifttt` (`username`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
