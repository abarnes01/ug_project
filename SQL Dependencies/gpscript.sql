drop database gp_database;
create database gp_database;
use gp_database;

create table user
(
	userID int NOT NULL AUTO_INCREMENT,
	username varchar(250) NOT NULL,
    password int NOT NULL,
    primary key (userID)
);

create table image_grid_method (
	userID int NOT NULL,
    grid_size int NOT NULL,
    imageOne LONGBLOB,
    imageTwo LONGBLOB,
    foreign key (userID) references user(userID)
);

create table colour_grid_method (
	userID int NOT NULL,
    patternPass varchar(250) NOT NULL,
    foreign key (userID) references user(userID)
);

INSERT INTO user(username, password) values("abarnes01", "password");