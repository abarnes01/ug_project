package eclipse.testing;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.imageio.ImageIO;

import org.junit.Test;

import eclipse.sql.DatabaseRunner;
import eclipse.swing.InitialLogin;
import eclipse.swing.Method;
import eclipse.swing.imagegrid.ImageGridLogin;
import eclipse.swing.imagegrid.ImageGridRegistration;

public class ImageGridTest {
	DatabaseRunner dbTestRunner = new DatabaseRunner("jdbc:mysql://localhost:3306/", "root", "");
	ImageGridRegistration igRegTestFrame;
	ImageGridLogin igLoginTestFrame;
	InitialLogin initLoginTestFrame;
	
	@Test
	public void testValidRegistration() throws Exception {
		igRegTestFrame = new ImageGridRegistration(dbTestRunner);
		dbTestRunner.dropDB();
		dbTestRunner.createDB();
		String username = "username";
		String password = "password";
		Integer gridSize = 4;
		BufferedImage imgOne = ImageIO.read(ImageGridLogin.class.getResource("/Images/1.jpg"));
		BufferedImage imgTwo = ImageIO.read(ImageGridLogin.class.getResource("/Images/2.jpg"));
		igRegTestFrame.setGenRndmImg(true);
		
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertTrue(igRegTestFrame.insertImageGridDetails(connection, username, password, gridSize, imgOne, imgTwo));
		connection.close();
	}
	
	@Test
	public void testInvalidRegistration() throws Exception {
		igRegTestFrame = new ImageGridRegistration(dbTestRunner);
		dbTestRunner.dropDB();
		dbTestRunner.createDB();
		String username = "username";
		String password = "password";
		Integer gridSize = 4;
		BufferedImage imgOne = ImageIO.read(ImageGridLogin.class.getResource("/Images/1.jpg"));
		BufferedImage imgTwo = ImageIO.read(ImageGridLogin.class.getResource("/Images/2.jpg"));
		igRegTestFrame.setGenRndmImg(true);
		
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertTrue(igRegTestFrame.insertImageGridDetails(connection, username, password, gridSize, imgOne, imgTwo));
		String username2 = "username";
		String password2 = "password";
		Integer gridSize2 = 4;
		BufferedImage imgOne2 = ImageIO.read(ImageGridLogin.class.getResource("/Images/1.jpg"));
		BufferedImage imgTwo2 = ImageIO.read(ImageGridLogin.class.getResource("/Images/2.jpg"));
		igRegTestFrame.setGenRndmImg(true);
		assertFalse(igRegTestFrame.insertImageGridDetails(connection, username2, password2, gridSize2, imgOne2, imgTwo2));
		connection.close();
	}
	
	@Test
	public void testInvalidInitialLogin() throws Exception {
		dbTestRunner.dropDB();
		dbTestRunner.createDB();
		initLoginTestFrame = new InitialLogin(dbTestRunner, Method.IMAGEGRID);
		initLoginTestFrame.setUserID(1);
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertFalse(initLoginTestFrame.checkImageGridDetails(connection));
		connection.close();
	}
	
	@Test
	public void testValidInitialLogin() throws Exception {
		testValidRegistration();
		initLoginTestFrame = new InitialLogin(dbTestRunner, Method.IMAGEGRID);
		initLoginTestFrame.setUserID(1);
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		assertTrue(initLoginTestFrame.checkImageGridDetails(connection));
		connection.close();
	}
	
	@Test
	public void testMakeGrid() throws Exception {
		initLoginTestFrame = new InitialLogin(dbTestRunner, Method.IMAGEGRID);
		initLoginTestFrame.setUserID(1);
		Connection connection = DriverManager.getConnection(dbTestRunner.getDburl(),dbTestRunner.getDbname(),dbTestRunner.getDbpass());
		initLoginTestFrame.checkImageGridDetails(connection);
		connection.close();
		assertTrue(initLoginTestFrame.getIgLogin().makeGrid());
	}
}
