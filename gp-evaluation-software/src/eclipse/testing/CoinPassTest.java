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
		cpRegTestFrame.getNameField().setText("username");
		cpRegTestFrame.getPassField().setText("password");
		cpRegTestFrame.getCoinPassField().setText(":0.png:2:blue:1.png:3:red");
		String username = cpRegTestFrame.getNameField().getText();
		String password = String.valueOf(cpRegTestFrame.getPassField().getPassword());
		String coinpass = cpRegTestFrame.getCoinPassField().getText();
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertTrue(cpRegTestFrame.insertCoinPassDetails(connection, username, password, coinpass));
		connection.close();
	}
	
	@Test
	public void testInvalidRegistration() throws Exception {
		cpRegTestFrame = new CoinPassRegistration(dbTestRunner);
		dbTestRunner.dropDB();
		dbTestRunner.createDB();
		cpRegTestFrame.getNameField().setText("username");
		cpRegTestFrame.getPassField().setText("password");
		cpRegTestFrame.getCoinPassField().setText(":0.png:2:blue:1.png:3:red");
		String username = cpRegTestFrame.getNameField().getText();
		String password = String.valueOf(cpRegTestFrame.getPassField().getPassword());
		String coinpass = cpRegTestFrame.getCoinPassField().getText();
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertTrue(cpRegTestFrame.insertCoinPassDetails(connection, username, password, coinpass));
		connection.close();

		// attempt to register another user
		cpRegTestFrame.getNameField().setText("username");
		cpRegTestFrame.getPassField().setText("password");
		cpRegTestFrame.getCoinPassField().setText(":4.png:6:green:7.png:4:orange");
		String username2 = cpRegTestFrame.getNameField().getText();
		String password2 = String.valueOf(cpRegTestFrame.getPassField().getPassword());
		String coinpass2 = cpRegTestFrame.getCoinPassField().getText();
		Connection connection2 = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertFalse(cpRegTestFrame.insertCoinPassDetails(connection2, username2, password2, coinpass2));
		connection2.close();
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
