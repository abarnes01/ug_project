package eclipse.swing;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import eclipse.sql.DatabaseRunner;
import eclipse.swing.coinpass.CoinPassLogin;
import eclipse.swing.coinpass.CoinPassRegistration;
import eclipse.swing.colourgrid.ColourGridLogin;
import eclipse.swing.colourgrid.ColourGridRegistration;
import eclipse.swing.colourwheel.ColourWheelLogin;
import eclipse.swing.colourwheel.ColourWheelRegistration;
import eclipse.swing.imagegrid.ImageGridLogin;
import eclipse.swing.imagegrid.ImageGridRegistration;

public class InitialLogin extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private DatabaseRunner dbRunner;
	private GridBagConstraints gbc = new GridBagConstraints();
	private JPanel contentPane, formPanel, btnPanel;
	private JTextField textField;
	private JPasswordField passField;
	private JButton loginBtn, backBtn;
	private JLabel usernameLbl, passLbl;
	private Method method;
	private int userID;
	private long startTime, stopTime;
	private CoinPassLogin cpLogin;
	private ColourGridLogin cgLogin;
	private ColourWheelLogin cwLogin;
	private ImageGridLogin igLogin;
	
	private static Map<Method, String> methodToStrMap;
	static {
		methodToStrMap = new HashMap<>();
		methodToStrMap.put(Method.SIMPLE, "Simple Login");
		methodToStrMap.put(Method.COLOURGRID, "Colour Grid Initial Login");
		methodToStrMap.put(Method.IMAGEGRID, "(Digraph) Image Grid Initial Login");
		methodToStrMap.put(Method.COIN, "Coin Pass Initial Login");
		methodToStrMap.put(Method.WHEEL, "Colour Wheel Initial Login");
	}

	public InitialLogin(DatabaseRunner dbRunner, Method method) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		this.dbRunner = dbRunner;
		
		// set title based on selected GP method
		setTitle(methodToStrMap.get(method));
		
		// set graphical password method we are using
		setMethod(method);
		
		// create elements
		formPanel = new JPanel();
		btnPanel = new JPanel();
		usernameLbl = new JLabel("Username: ");
		textField = new JTextField(10);
		passLbl = new JLabel("Password: ");
		passField = new JPasswordField(10);
		
		// create login button
		loginBtn = new JButton("Login");
		loginBtn.addActionListener(this);
		backBtn = new JButton("Registration");
		backBtn.addActionListener(this);
		
		formPanel.setLayout(new GridBagLayout());
		gbc.gridx = 0; gbc.gridy = 0;
		formPanel.add(usernameLbl, gbc);
		gbc.gridx = 1; gbc.gridy = 0;
		formPanel.add(textField, gbc);
		gbc.gridx = 0; gbc.gridy = 1;
		formPanel.add(passLbl, gbc);
		gbc.gridx = 1; gbc.gridy = 1;
		formPanel.add(passField, gbc);
		
		btnPanel.add(backBtn);
		btnPanel.add(loginBtn);
		contentPane.add(formPanel, BorderLayout.CENTER);
		contentPane.add(btnPanel, BorderLayout.SOUTH);
		startTime = System.nanoTime();
	}
	
	public Method getMethod() {
		return method;
	}
	
	public void setMethod(Method input) {
		method = input;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JButton btn = (JButton) event.getSource();
		if (btn.equals(loginBtn)) {
			String username = textField.getText();
			Integer password = String.valueOf(passField.getPassword()).hashCode();
			String url = dbRunner.getDburl();
			String dbname = dbRunner.getDbname();
			String dbpass = dbRunner.getDbpass();
			/*
			 * Connect to database via driver, 
			 * for each method do a check to see if user and the method login details exist
			 * if so then go to selected GP login
			 */
			try {
				Connection connection = (Connection) DriverManager.getConnection(url,dbname,dbpass);
				PreparedStatement st = (PreparedStatement) connection.prepareStatement("Select userID from user where username=? and password=?");
				st.setString(1, username);
				st.setInt(2, password);
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					userID = rs.getInt("userID");
					if (getMethod() == Method.SIMPLE) {
						stopTime = System.nanoTime()-startTime;
						long seconds = TimeUnit.SECONDS.convert(stopTime, TimeUnit.NANOSECONDS);
						String simpleLoginResultHtml = "<html><h1>Simple Login</h1>"
								+ "<p>Time taken to login: " + seconds + " seconds. </p>"
								+ "<p>This provides little shoulder surfing resistance.</p><br>"
								+ "<p>The keystroke entry and number of keystrokes <br>can be observed to reveal the password. </p>";
						JOptionPane.showMessageDialog(loginBtn, String.format(simpleLoginResultHtml));
						Welcome welcome = new Welcome(dbRunner);
						welcome.setVisible(true);
						dispose();
					} else if (getMethod() == Method.COLOURGRID) {
						checkColourGridDetails(connection);
					} else if (getMethod() == Method.IMAGEGRID) {
						checkImageGridDetails(connection);
					} else if (getMethod() == Method.COIN) {
						checkCoinPassDetails(connection);
					} else if (getMethod() == Method.WHEEL) {
						checkColourWheelDetails(connection);
					}
				} else {
					JOptionPane.showMessageDialog(loginBtn, "Incorrect login details.");
				}
			} catch (SQLException sqlException){
				sqlException.printStackTrace();
				
			}
		} else if (btn.equals(backBtn)) {
			if (getMethod() == Method.COLOURGRID) {
				new ColourGridRegistration(dbRunner).setVisible(true);
				dispose();
			} else if (getMethod() == Method.IMAGEGRID) {
				new ImageGridRegistration(dbRunner).setVisible(true);
				dispose();
			} else if (getMethod() == Method.SIMPLE) {
				new SimpleRegistration(dbRunner).setVisible(true);
				dispose();				
			} else if (getMethod() == Method.COIN) {
				new CoinPassRegistration(dbRunner).setVisible(true);
				dispose();
			} else if (getMethod() == Method.WHEEL) {
				new ColourWheelRegistration(dbRunner).setVisible(true);
				dispose();
			}
		}
	}
	
	public Boolean checkImageGridDetails(Connection connection) throws SQLException {
		PreparedStatement igSt = (PreparedStatement) connection.prepareStatement("Select gridSize, imageOne, imageTwo, randomOrPreset from image_grid_method where userID=?");
		igSt.setInt(1, userID);
		ResultSet igRs = igSt.executeQuery();
		if (igRs.next()) {
			InputStream ioIS = igRs.getBinaryStream(2);
			InputStream itIS = igRs.getBinaryStream(3);
			try {
				BufferedImage imgOne = ImageIO.read(ioIS);
				BufferedImage imgTwo = ImageIO.read(itIS);
				igLogin = new ImageGridLogin(dbRunner, igRs.getInt(1), imgOne, imgTwo, igRs.getString(4));
				igLogin.setVisible(true);
				dispose();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Failed to create login page.");
				return false;
			}
		} else {
			JOptionPane.showMessageDialog(loginBtn, "User does not have image grid method details.");
			return false;
		}
	}
	
	public Boolean checkColourGridDetails(Connection connection) throws SQLException {
		PreparedStatement cglSt = (PreparedStatement) connection.prepareStatement("Select patternPass from colour_grid_method where userID=?");
		cglSt.setInt(1, userID);
		ResultSet cglRs = cglSt.executeQuery();
		if (cglRs.next()) {
			System.out.println("Got grid method details");
			String patternPass = cglRs.getString("patternPass");
			cgLogin = new ColourGridLogin(dbRunner, patternPass);
			cgLogin.setVisible(true);
			dispose();
			return true;
		} else {
			JOptionPane.showMessageDialog(loginBtn, "User does not have colour grid method details.");
			return false;
		}
	}
	
	public Boolean checkCoinPassDetails(Connection connection) throws SQLException {
		PreparedStatement cpSt = (PreparedStatement) connection.prepareStatement("Select coinpass from coin_pass_method where userID=?");
		cpSt.setInt(1, userID);
		ResultSet cpRs = cpSt.executeQuery();
		if (cpRs.next()) {
			System.out.println("Got coin pass method details");
			String coinPass = cpRs.getString("coinpass");
			cpLogin = new CoinPassLogin(dbRunner, coinPass);
			cpLogin.setVisible(true);
			dispose();
			return true;
		} else {
			JOptionPane.showMessageDialog(loginBtn, "User does not have coin pass method details.");
			return false;
		}
	}
	
	public Boolean checkColourWheelDetails(Connection connection) throws SQLException{
		PreparedStatement cpSt = (PreparedStatement) connection.prepareStatement("Select chosenColour, wheelPass from colour_wheel_method where userID=?");
		cpSt.setInt(1, userID);
		ResultSet cpRs = cpSt.executeQuery();
		if (cpRs.next()) {
			System.out.println("Got colour wheel method details");
			String chosenCol = cpRs.getString("chosenColour");
			String wheelPass = cpRs.getString("wheelPass");
			cwLogin = new ColourWheelLogin(dbRunner, chosenCol, wheelPass);
			cwLogin.setVisible(true);
			dispose();
			return true;
		} else {
			JOptionPane.showMessageDialog(loginBtn, "User does not have colour wheel method details.");
			return false;
		}
	}

	public final int getUserID() {
		return userID;
	}

	public final void setUserID(int userID) {
		this.userID = userID;
	}

	public final CoinPassLogin getCpLogin() {
		return cpLogin;
	}

	public final ColourGridLogin getCgLogin() {
		return cgLogin;
	}

	public final ColourWheelLogin getCwLogin() {
		return cwLogin;
	}

	public final ImageGridLogin getIgLogin() {
		return igLogin;
	}
	
	
	
	
	
}

