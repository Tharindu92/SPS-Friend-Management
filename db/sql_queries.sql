CREATE TABLE `users` (
  `iduser` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`iduser`),
  UNIQUE KEY `username_UNIQUE` (`username`)
)

CREATE TABLE `friendships` (
  `iduser` int(11) NOT NULL,
  `idfriend` int(11) NOT NULL,
  `blocked` bit(1) DEFAULT b'0',
  `idfriendship` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`idfriendship`),
  KEY `iduser_idx` (`iduser`),
  KEY `idfriend_idx` (`idfriend`),
  CONSTRAINT `idfriend` FOREIGN KEY (`idfriend`) REFERENCES `users` (`iduser`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `iduser` FOREIGN KEY (`iduser`) REFERENCES `users` (`iduser`) ON DELETE CASCADE ON UPDATE CASCADE
)