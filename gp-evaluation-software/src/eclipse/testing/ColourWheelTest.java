package eclipse.testing;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

import eclipse.sql.DatabaseRunner;
import eclipse.swing.InitialLogin;
import eclipse.swing.Method;
import eclipse.swing.colourwheel.ColourWheelLogin;
import eclipse.swing.colourwheel.ColourWheelRegistration;

public class ColourWheelTest {
	DatabaseRunner dbTestRunner = new DatabaseRunner("jdbc:mysql://localhost:3306/", "root", "");
	ColourWheelRegistration cwRegTestFrame;
	ColourWheelLogin cwLoginTestFrame;
	InitialLogin initLoginTestFrame;
	
	@Test
	public void testValidRegistration() throws Exception {
		cwRegTestFrame = new ColourWheelRegistration(dbTestRunner);
		dbTestRunner.dropDB();
		dbTestRunner.createDB();
		String username = "username";
		String password = "password";
		String colStr = "orange";
		String wheelPass = "car.los";
		
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertTrue(cwRegTestFrame.insertColourWheelDetails(connection, username, password, wheelPass, colStr));
		connection.close();
	}
	
	@Test
	public void testInvalidRegistration() throws Exception {
		cwRegTestFrame = new ColourWheelRegistration(dbTestRunner);
		dbTestRunner.dropDB();
		dbTestRunner.createDB();
		String username = "username";
		String password = "password";
		String colStr = "orange";
		String wheelPass = "car.los";
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertTrue(cwRegTestFrame.insertColourWheelDetails(connection, username, password, wheelPass, colStr));
		
		// attempt to register another user
		String username2 = "username";
		String password2 = "password";
		String colStr2 = "orange";
		String wheelPass2 = "car.los";
		
		assertFalse(cwRegTestFrame.insertColourWheelDetails(connection, username2, password2, wheelPass2, colStr2));
		connection.close();
	}
	
	@Test
	public void testInvalidInitialLogin() throws Exception {
		dbTestRunner.dropDB();
		dbTestRunner.createDB();
		initLoginTestFrame = new InitialLogin(dbTestRunner, Method.WHEEL);
		initLoginTestFrame.setUserID(1);
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertFalse(initLoginTestFrame.checkColourWheelDetails(connection));
		connection.close();
	}
	
	@Test
	public void testValidInitialLogin() throws Exception {
		testValidRegistration();
		initLoginTestFrame = new InitialLogin(dbTestRunner, Method.WHEEL);
		initLoginTestFrame.setUserID(1);
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertTrue(initLoginTestFrame.checkColourWheelDetails(connection));
		connection.close();
	}
}
