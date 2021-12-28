DELIMITER //
CREATE PROCEDURE insert_user()
BEGIN
	insert into user (username, password) values ('ab1049','password');
END //
DELIMITER ;