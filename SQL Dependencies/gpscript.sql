drop database gp_database;
create database gp_database;
use gp_database;

create table user
(
	userID int NOT NULL,
	username varchar(250) NOT NULL,
    password varchar(250) NOT NULL,
    PRIMARY KEY (userID)
);