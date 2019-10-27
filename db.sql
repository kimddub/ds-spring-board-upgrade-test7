DROP DATABASE IF EXISTS ds7;

CREATE DATABASE ds7;

USE ds7;


# drop table board;

CREATE TABLE board(
	id INT(10) AUTO_INCREMENT PRIMARY KEY,
	regDate DATETIME NOT NULL,
	`name` VARCHAR(100) NOT NULL,
	memberAuth INT(1) DEFAULT 0,
	adminAuth INT(1) DEFAULT 0
); 

INSERT INTO board (regDate, `name`, memberAuth, adminAuth)
VALUES (NOW(),'자유게시판',1,0),(NOW(),'공지사항',0,0);


# drop table memberAuth;

CREATE TABLE `memberAuth`(
	id INT(1) PRIMARY KEY,
	regDate DATETIME NOT NULL,
	`name` VARCHAR(10) NOT NULL,
	`exp` VARCHAR(1000)
); 

INSERT INTO memberAuth 
VALUES (0,NOW(),'Admin','Admin'),(1,NOW(),'L1','level 1'),(2,NOW(),'L2','level 2');


# SET FOREIGN_KEY_CHECKS = 0;
# drop table member;

CREATE TABLE `member`(
	id INT(10) AUTO_INCREMENT PRIMARY KEY,
	regDate DATETIME NOT NULL,
	loginId VARCHAR(100) NOT NULL,
	encryptedLoginPw TEXT NOT NULL,
	encryptingSalt TEXT NOT NULL,
	`name` VARCHAR(100) NOT NULL,
	authCode INT(1) DEFAULT 1,
	withdrawal TINYINT(1) DEFAULT 0,
	FOREIGN KEY (authCode) REFERENCES `memberAuth` (id) ON DELETE CASCADE
); 

# drop table article;

CREATE TABLE article(
	id INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	regDate DATETIME NOT NULL,
	modDate DATETIME NOT NULL,
	title VARCHAR(100) NOT NULL,
	`body` TEXT NOT NULL,
	boardId INT(10) NOT NULL,
	memberId INT(10) NOT NULL,
	writer VARCHAR(100) NOT NULL,
	delState TINYINT(1) DEFAULT 0,
	FOREIGN KEY (boardId) REFERENCES board (`id`) ON DELETE CASCADE,
	FOREIGN KEY (memberId) REFERENCES `member` (`id`) ON DELETE CASCADE
); 

INSERT INTO article (regDate, modDate, title, `body`, boardId, memberId, writer)
VALUES (NOW(),NOW(),'테스트1','테스트1',1,1,'김수빈');

# drop table articleFile;

CREATE TABLE articleFile(
	id INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	regDate DATETIME NOT NULL,
	articleId INT(10) UNSIGNED NOT NULL,
	prefix TEXT NOT NULL,
	originalFileName VARCHAR(100) NOT NULL,
	`type` VARCHAR(10) NOT NULL,
	delState TINYINT(1) DEFAULT 0,
	FOREIGN KEY (articleId) REFERENCES article (`id`) ON DELETE CASCADE
); 

# drop table CKEditorImg;

CREATE TABLE CKEditorImg(
	id INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	regDate DATETIME NOT NULL,
	prefix TEXT NOT NULL,
	originalFileName VARCHAR(100) NOT NULL,
	`type` VARCHAR(10) NOT NULL,
	delState TINYINT(1) DEFAULT 0
); 


SELECT *
FROM CKEditorImg;

SELECT *
FROM article
ORDER BY id DESC;

SELECT *
FROM `member`;

SELECT *
FROM articleFile;

# SET FOREIGN_KEY_CHECKS = 0;
# truncate `member`;

SELECT articleId, COUNT(*) AS COUNT
FROM articleFile
GROUP BY articleId;

SELECT a1.*, a2.onlyRegDate, 
	IFNULL((SELECT f.`count`
	FROM (SELECT articleId, COUNT(*) AS `count`
		FROM articleFile
		GROUP BY articleId) AS f 
	WHERE a1.id = f.articleId),0) AS fileCount
FROM article AS a1
LEFT JOIN (
	SELECT id, DATE_FORMAT(regDate,"%Y-%m-%d") AS 'onlyRegDate'
	FROM article
) AS a2
ON a1.id = a2.id
WHERE a1.delState != 1;

-- PRIMARY KEY(`code`),
-- UNIQUE KEY(id,siteCode,mediaCode),
-- FOREIGN KEY (siteCode) REFERENCES site (`code`) on delete cascade,


