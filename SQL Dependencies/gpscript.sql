drop database gp_database;
create database gp_database;
use gp_database;

create table user
(
	userID int NOT NULL AUTO_INCREMENT,
	username varchar(250) NOT NULL,
    password varchar(250) NOT NULL,
    PRIMARY KEY (userID)
);

INSERT INTO user(username, password) values("abarnes01", "password");