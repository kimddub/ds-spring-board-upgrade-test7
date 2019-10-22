DROP DATABASE IF EXISTS ds;

CREATE DATABASE ds;

USE ds;

# drop table article

CREATE TABLE article(
	id INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	regDate DATETIME NOT NULL,
	modData DATETIME NOT NULL,
	title VARCHAR(100) NOT NULL,
	`body` TEXT NOT NULL,
	boardId INT(10) NOT NULL,
	writer VARCHAR(100) NOT NULL,
	delState TINYINT(1) DEFAULT(0)
); 

# drop table articleFile

CREATE TABLE articleFile(
	id INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	regDate DATETIME NOT NULL,
	articleId INT(10) NOT NULL,
	prefix TEXT NOT NULL,
	originalFileName VARCHAR(100) NOT NULL,
	`type` VARCHAR(10) NOT NULL
); 

# drop table board

CREATE TABLE board(
	id INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	regDate DATETIME NOT NULL,
	`name` VARCHAR(100) NOT NULL,
	memberAuth INT(1) DEFAULT(0),
	adminAuth INT(1) DEFAULT(0)
); 