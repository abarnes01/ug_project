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
		cgRegTestFrame.getUsernameField().setText("username");
		cgRegTestFrame.getPasswordField().setText("password");
		cgRegTestFrame.getPatternPField().setText("carlos");
		String username = cgRegTestFrame.getUsernameField().getText();
		String password = String.valueOf(cgRegTestFrame.getPasswordField().getPassword());
		String patternPass = String.valueOf(cgRegTestFrame.getPatternPField().getPassword());
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertTrue(cgRegTestFrame.insertColourGridDetails(connection, username, password, patternPass));
		connection.close();
	}
	
	@Test
	public void testInvalidRegistration() throws Exception {
		cgRegTestFrame = new ColourGridRegistration(dbTestRunner);
		dbTestRunner.dropDB();
		dbTestRunner.createDB();
		cgRegTestFrame.getUsernameField().setText("username");
		cgRegTestFrame.getPasswordField().setText("password");
		cgRegTestFrame.getPatternPField().setText("carlos");
		String username = cgRegTestFrame.getUsernameField().getText();
		String password = String.valueOf(cgRegTestFrame.getPasswordField().getPassword());
		String patternPass = String.valueOf(cgRegTestFrame.getPatternPField().getPassword());
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertTrue(cgRegTestFrame.insertColourGridDetails(connection, username, password, patternPass));
		connection.close();
		
		// attempt to register another user
		cgRegTestFrame.getUsernameField().setText("username");
		cgRegTestFrame.getPasswordField().setText("password");
		cgRegTestFrame.getPatternPField().setText("carlos");
		String username2 = cgRegTestFrame.getUsernameField().getText();
		String password2 = String.valueOf(cgRegTestFrame.getPasswordField().getPassword());
		String patternPass2 = String.valueOf(cgRegTestFrame.getPatternPField().getPassword());
		Connection connection2 = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertFalse(cgRegTestFrame.insertColourGridDetails(connection2, username2, password2, patternPass2));
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
