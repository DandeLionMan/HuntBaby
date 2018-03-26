	CREATE TABLE `users` (`id` int(11) NOT NULL AUTO_INCREMENT,`username` varchar(45) NOT NULL, `password` varchar(100) NOT NULL, `weixin` varchar(100) ,  client int(6) DEFAULT '1',  `image` varchar(200) DEFAULT '', `submitcountbusin` int(5) DEFAULT '5', `submitcountmoney` int(11) DEFAULT '5',`submitcountrent` int(5) DEFAULT '5', `submitcountcheat` int(5) DEFAULT '1', `submitcountmaterial` int(5) DEFAULT '1',`datetime` date DEFAULT NULL, `successbusin` int(6) DEFAULT '0' ,`role` varchar(45) NOT NULL DEFAULT 'ROLE_USER', `enabled` varchar(45) NOT NULL DEFAULT '1', PRIMARY KEY (`id`), UNIQUE KEY `username_UNIQUE` (`username`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

    CREATE TABLE `authorities` (  `id` int(11) NOT NULL AUTO_INCREMENT,      `username` varchar(45) NOT NULL,      `authority` varchar(45) NOT NULL,      PRIMARY KEY (`id`),      UNIQUE KEY `username_UNIQUE` (`username`,`authority`)) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

    CREATE TABLE `news` (`id` int(11) NOT NULL AUTO_INCREMENT, `title` varchar(50) DEFAULT NULL, `img` varchar(45) DEFAULT NULL, `content` varchar(100) DEFAULT NULL, `datetime` date DEFAULT NULL, `user_id` int(11) NOT NULL, PRIMARY KEY (`id`) ,KEY `user_id` (`user_id`), FOREIGN KEY (`user_id`) REFERENCES users(`id`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    CREATE TABLE `cheat` (`id` int(11) NOT NULL AUTO_INCREMENT , `cheatwechat` varchar(45) NOT NULL , `label` int(11) DEFAULT '0' , `cheatcount` int(11) DEFAULT '0' , `cheat1` int(6) DEFAULT '0', `cheat2` int(6) DEFAULT '0', `cheat3` int(6) DEFAULT '0', `cheat4` int(6) DEFAULT '0', `cheat5` int(6) DEFAULT '0', `cheat6` int(6) DEFAULT '0', `cheat7` int(6) DEFAULT '0',`datetime` date DEFAULT NULL ,`thumb1` varchar(200) DEFAULT '',`thumb2` varchar(200) DEFAULT '',`thumb3` varchar(200) DEFAULT '',`detail`varchar(150) DEFAULT '' ,PRIMARY KEY (`id`))ENGINE=InnoDB DEFAULT CHARSET=utf8; 
  
    CREATE TABLE `cheatitem` (`id` int(11) NOT NULL AUTO_INCREMENT , `cheatwechat` varchar(45) NOT NULL , `label` int(11) DEFAULT '0' , `datetime` varchar(100) DEFAULT NULL ,`thumb1` varchar(200) DEFAULT '',`thumb2` varchar(200) DEFAULT '',`thumb3` varchar(200) DEFAULT '',`detail`varchar(150) DEFAULT ''  , `status` int(5) DEFAULT '0', `count` int(5) DEFAULT '0' ,PRIMARY KEY (`id`))ENGINE=InnoDB DEFAULT CHARSET=utf8; 

    CREATE TABLE `material` (`id` int(11) NOT NULL AUTO_INCREMENT, `user_id` int(11) , `wechat` varchar(20) DEFAULT '', `buywechat` varchar(20) DEFAULT '',  `materialindex` int(5) DEFAULT '0' , `count` int(5) DEFAULT '0' ,  `materialprice` double(11,2) DEFAULT '0.0', `materiaforpricesell` double(11,2) DEFAULT '0.0', `materiaforpricebuy` double(11,2) DEFAULT '0.0',`service`int(2) DEFAULT '0' , `datetime` varchar(30) DEFAULT '', `detail` varchar(200) DEFAULT '' , `materialforresult`int(11) DEFAULT '0', PRIMARY KEY (`id`) ,KEY `user_id` (`user_id`), FOREIGN KEY (`user_id`) REFERENCES users(`id`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
    CREATE TABLE `account` (`id` int(11) NOT NULL AUTO_INCREMENT, `userid` int(11) ,`platform` int(6) NOT NULL ,`wechat` varchar(20) NOT NULL ,`buywechat` varchar(20) DEFAULT '' ,`nickname`varchar(45) NOT NULL,`accountforprice` double(11,2) DEFAULT '0.0', `accountforpricesell` double(11,2) DEFAULT '0.0',`accountforpricebuy` double(11,2) DEFAULT '0.0',`server`int(2) DEFAULT '0' , `diamond`int(6) DEFAULT '0' ,`anthortype`int(2) DEFAULT '0', `recharge`int(3) DEFAULT '0',`redress`int(6) DEFAULT '0' ,`ordnance`int(6) DEFAULT '0' ,`macordnance`int(6) DEFAULT '0' ,`vip`int(2) NOT NULL,`level`int(2)NOT NULL,`isconvert`int(2) DEFAULT '0' ,`ishat`int(2) DEFAULT '0', `hattime`int(5) DEFAULT '0', `goldmoney`int(11) DEFAULT '0',`backmoney`int(11) DEFAULT '0', `redpackage`int(11) DEFAULT '0', `goldscale`int(11) DEFAULT '0', `ismaterial`int(4) DEFAULT '0',`iswarrant`int(4) DEFAULT '0',`thumb1` varchar(200) DEFAULT '',`thumb2` varchar(200) DEFAULT '',`thumb3` varchar(200) DEFAULT '', `thumb4` varchar(200) DEFAULT '', `thumb5` varchar(200) DEFAULT '',`datetime` varchar(50) DEFAULT '',`detail`varchar(150) DEFAULT '',`accountforresult`int(11) DEFAULT '0', `report` varchar(150) DEFAULT '', PRIMARY KEY (`id`), KEY `userid` (`userid`), FOREIGN KEY (`userid`) REFERENCES users(`id`) )ENGINE=InnoDB DEFAULT CHARSET=utf8; 
    
    CREATE TABLE `core` (`id` int(11) NOT NULL AUTO_INCREMENT, `newprice` double(11,2) DEFAULT '0.0', `version` varchar(10) DEFAULT '1.0', `versionurl` varchar(100) DEFAULT NULL, `versionprompt1` varchar(100) DEFAULT NULL, `versionprompt2` varchar(100) DEFAULT NULL,PRIMARY KEY (`id`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
    INSERT INTO users(username, password,weixin, image,submitcountbusin,submitcountmoney,submitcountrent, enabled) VALUES ('admin', '$2a$10$OB3Ni.NElsl5i1q6Acj8sOBAoMtoM3wmHjubaX/CZrddC5y5wfQje', '','' ,'5','5','10', 1)
    
    INSERT INTO users(username, password,weixin, image,submitcountbusin,submitcountmoney,submitcountrent,submitcountcheat , submitcountmaterial,datetime, role , enabled) VALUES ('15252007007', 'xxx', '','' ,'5','5','5','1','10' ,null, 'ROLE_ADMIN',1)
	
    INSERT INTO authorities(username, authority) VALUES ('admin','ROLE_USER')
	INSERT INTO authorities(username, authority) VALUES ('admin','ROLE_ADMIN')
	INSERT INTO authorities(username, authority) VALUES ('15252007007','ROLE_USER');
	INSERT INTO authorities(username, authority) VALUES ('15252007007','ROLE_ADMIN');
	INSERT INTO authorities(username, authority) VALUES ('17190181105','ROLE_ADMIN');
	INSERT INTO authorities(username, authority) VALUES ('13668159397','ROLE_ADMIN');
	INSERT INTO authorities(username, authority) VALUES ('13551169829','ROLE_ADMIN');
	
	INSERT INTO users(username, password,weixin, image,submitcountbusin,submitcountmoney,submitcountrent,submitcountcheat , submitcountmaterial,datetime, enabled) VALUES ('17190181105', 'xxx', '','' ,'5','5','5','1','10' ,null,1)
	INSERT INTO users(username, password,weixin, image,submitcountbusin,submitcountmoney,submitcountrent,submitcountcheat , submitcountmaterial,datetime, enabled) VALUES ('13668159397', 'xxx', '','' ,'5','5','5','1','10' ,null,1)
	
	DELETE FROM users WHERE username= '15252007007';

	ALTER TABLE users ADD COLUMN sign varchar(200) DEFAULT '';`datetime` varchar(50) DEFAULT ''
	ALTER TABLE users ADD COLUMN datetime varchar(50) DEFAULT '';
	ALTER TABLE users DROP COLUMN datetime ; 
	ALTER TABLE users ADD COLUMN client int(6) DEFAULT '1' after weixin; 
	ALTER TABLE users ADD COLUMN successbusin int(6) DEFAULT '0' after datetime; 
	ALTER TABLE users ADD COLUMN role varchar(645) DEFAULT 'ROLE_USER' after successbusin; 
	truncate table users;
	ALTER TABLE account ADD COLUMN platform int(6) DEFAULT '0' after wechat; 
	ALTER TABLE account CHANGE user_id userid int(6) DEFAULT '0' ; 
	
	UPDATE users SET role = 'ROLE_ADMIN' WHERE id = 1;