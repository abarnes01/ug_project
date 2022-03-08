CREATE DATABASE IF NOT EXISTS gp_database;
use gp_database;
create table if not exists user
(
	userID int NOT NULL AUTO_INCREMENT,
	username varchar(250) NOT NULL,
    password int NOT NULL,
    primary key (userID)
);

create table if not exists image_grid_method (
	userID int NOT NULL,
    gridSize int NOT NULL,
    imageOne LONGBLOB,
    imageTwo LONGBLOB,
    foreign key (userID) references user(userID)
);

create table if not exists colour_grid_method (
	userID int NOT NULL,
    patternPass varchar(250) NOT NULL,
    foreign key (userID) references user(userID)
);

create table if not exists coin_pass_method (
	userID int NOT NULL,
    coinpass varchar(250) NOT NULL,
    foreign key (userID) references user(userID)
);

create table if not exists colour_wheel_method (
	userID int NOT NULL,
    chosenColour varchar(250) NOT NULL,
    foreign key (userID) references user(userID)
);