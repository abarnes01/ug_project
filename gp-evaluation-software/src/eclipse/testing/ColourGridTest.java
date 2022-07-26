package eclipse.testing;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

import eclipse.sql.DatabaseRunner;
import eclipse.swing.InitialLogin;
import eclipse.swing.Method;
import eclipse.swing.colourgrid.ColourGridLogin;
import eclipse.swing.colourgrid.ColourGridRegistration;

public class ColourGridTest {
	DatabaseRunner dbTestRunner = new DatabaseRunner("jdbc:mysql://localhost:3306/", "root", "");
	ColourGridRegistration cgRegTestFrame;
	ColourGridLogin cgLoginTestFrame;
	InitialLogin initLoginTestFrame;
	
	@Test
	public void testValidRegistration() throws Exception {
		cgRegTestFrame = new ColourGridRegistration(dbTestRunner);
		dbTestRunner.dropDB();
		dbTestRunner.createDB();
		String username = "username";
		String password = "password";
		String patternPass = "carlos";
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertTrue(cgRegTestFrame.insertColourGridDetails(connection, username, password, patternPass));
		connection.close();
	}
	
	@Test
	public void testInvalidRegistration() throws Exception {
		cgRegTestFrame = new ColourGridRegistration(dbTestRunner);
		dbTestRunner.dropDB();
		dbTestRunner.createDB();
		String username = "username";
		String password = "password";
		String patternPass = "carlos";
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertTrue(cgRegTestFrame.insertColourGridDetails(connection, username, password, patternPass));
		
		// attempt to register another user
		String username2 = "username";
		String password2 = "password";
		String patternPass2 = "andrew";
		assertFalse(cgRegTestFrame.insertColourGridDetails(connection, username2, password2, patternPass2));
		connection.close();
	}
	
	@Test
	public void testInvalidInitialLogin() throws Exception {
		dbTestRunner.dropDB();
		dbTestRunner.createDB();
		initLoginTestFrame = new InitialLogin(dbTestRunner, Method.COLOURGRID);
		initLoginTestFrame.setUserID(1);
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertFalse(initLoginTestFrame.checkColourGridDetails(connection));
		connection.close();
	}
	
	@Test
	public void testValidInitialLogin() throws Exception {
		testValidRegistration();
		initLoginTestFrame = new InitialLogin(dbTestRunner, Method.COLOURGRID);
		initLoginTestFrame.setUserID(1);
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertTrue(initLoginTestFrame.checkColourGridDetails(connection));
		connection.close();
	}
	
	@Test
	public void testMakeGrid() throws Exception {
		initLoginTestFrame = new InitialLogin(dbTestRunner, Method.COLOURGRID);
		initLoginTestFrame.setUserID(1);
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		initLoginTestFrame.checkColourGridDetails(connection);
		connection.close();
		assertTrue(initLoginTestFrame.getCgLogin().makeGrid());
	}
	
}
