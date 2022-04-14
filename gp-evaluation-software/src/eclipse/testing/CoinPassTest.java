package eclipse.testing;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;

import eclipse.swing.coinpass.CoinPassRegistration; 
import eclipse.swing.coinpass.CoinPassLogin;
import eclipse.sql.DatabaseRunner;
import eclipse.swing.InitialLogin;
import eclipse.swing.Method;
import org.junit.Test;

public class CoinPassTest {
	DatabaseRunner dbTestRunner = new DatabaseRunner("jdbc:mysql://localhost:3306/", "root", "");
	CoinPassRegistration cpRegTestFrame;
	CoinPassLogin cpLoginTestFrame;
	InitialLogin initLoginTestFrame;
	
	@Test
	public void testValidRegistration() throws Exception {
		cpRegTestFrame = new CoinPassRegistration(dbTestRunner);
		dbTestRunner.dropDB();
		dbTestRunner.createDB();
		String username = "username";
		String password = "password";
		String coinpass = ":0.png:2:blue:1.png:3:red";
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertTrue(cpRegTestFrame.insertCoinPassDetails(connection, username, password, coinpass));
		connection.close();
	}
	
	@Test
	public void testInvalidRegistration() throws Exception {
		cpRegTestFrame = new CoinPassRegistration(dbTestRunner);
		dbTestRunner.dropDB();
		dbTestRunner.createDB();
		String username = "username";
		String password = "password";
		String coinpass = ":0.png:2:blue:1.png:3:red";
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertTrue(cpRegTestFrame.insertCoinPassDetails(connection, username, password, coinpass));

		// attempt to register another user
		String username2 = "username";
		String password2 = "password";
		String coinpass2 = ":8.png:5:blue:4.png:5:green";
		assertFalse(cpRegTestFrame.insertCoinPassDetails(connection, username2, password2, coinpass2));
		connection.close();
	}
	
	@Test
	public void testInvalidInitialLogin() throws Exception {
		dbTestRunner.dropDB();
		dbTestRunner.createDB();
		initLoginTestFrame = new InitialLogin(dbTestRunner, Method.COIN);
		initLoginTestFrame.setUserID(1);
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertFalse(initLoginTestFrame.checkCoinPassDetails(connection));
		connection.close();
	}
	
	@Test
	public void testValidInitialLogin() throws Exception {
		testValidRegistration();
		initLoginTestFrame = new InitialLogin(dbTestRunner, Method.COIN);
		initLoginTestFrame.setUserID(1);
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertTrue(initLoginTestFrame.checkCoinPassDetails(connection));
		connection.close();
	}
	
	@Test
	public void testMakeCoins() throws Exception {
		initLoginTestFrame = new InitialLogin(dbTestRunner, Method.COIN);
		initLoginTestFrame.setUserID(1);
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		initLoginTestFrame.checkCoinPassDetails(connection);
		connection.close();
		assertTrue(initLoginTestFrame.getCpLogin().makeCoins());
	}
	
}
