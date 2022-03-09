package eclipse.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

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
	private JPanel headerPanel;
	private JPanel contentPane, formPanel, buttonPanel;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton loginButton, backBtn;
	private JLabel headerLabel, usernameLabel, passwordLabel;
	private Method method;

	public InitialLogin(Method method) {
		// auto
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// set graphical password method we are using
		setMethod(method);
		
		// create elements
		headerPanel = new JPanel();
		headerLabel = new JLabel("Login Form");
		formPanel = new JPanel();
		buttonPanel = new JPanel();
		usernameLabel = new JLabel("Username: ");
		textField = new JTextField(10);
		passwordLabel = new JLabel("Password: ");
		passwordField = new JPasswordField(10);
		
		// create login button
		loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		backBtn = new JButton("<");
		backBtn.addActionListener(this);
		
		// add features to window
		headerPanel.add(backBtn);
		headerPanel.add(headerLabel);
		formPanel.setLayout(new GridLayout(3,1,10,10));
		formPanel.add(usernameLabel);
		formPanel.add(textField);
		formPanel.add(passwordLabel);
		formPanel.add(passwordField);	
		buttonPanel.add(loginButton);
		contentPane.add(headerPanel, BorderLayout.NORTH);
		contentPane.add(formPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JButton btn = (JButton) event.getSource();

		if (btn.equals(loginButton)) {
			String username = textField.getText();
			Integer password = String.valueOf(passwordField.getPassword()).hashCode();
			String url = "jdbc:mysql://localhost:3306/gp_database";
			String dbname = "root";
			String dbpass = "";
			try {
				Connection connection = (Connection) DriverManager.getConnection(url,dbname,dbpass);
				PreparedStatement st = (PreparedStatement) connection.prepareStatement("Select userID from user where username=? and password=?");
				st.setString(1, username);
				st.setInt(2, password);
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					int userID = rs.getInt("userID");
					if (getMethod() == Method.COLOURGRID) {
						PreparedStatement cglSt = (PreparedStatement) connection.prepareStatement("Select patternPass from colour_grid_method where userID=?");
						cglSt.setInt(1, userID);
						ResultSet cglRs = cglSt.executeQuery();
						if (cglRs.next()) {
							System.out.println("Got grid method details");
							String patternPass = cglRs.getString("patternPass");
							new ColourGridLogin(patternPass).setVisible(true);
							dispose();
						} else {
							JOptionPane.showMessageDialog(loginButton, "User does not have colour grid method details.");
						}
					} else if (getMethod() == Method.IMAGEGRID) {
						PreparedStatement igSt = (PreparedStatement) connection.prepareStatement("Select gridSize, imageOne, imageTwo from image_grid_method where userID=?");
						igSt.setInt(1, userID);
						ResultSet igRs = igSt.executeQuery();
						if (igRs.next()) {
							InputStream ioIS = igRs.getBinaryStream(2);
							InputStream itIS = igRs.getBinaryStream(3);
							try {
								BufferedImage imgOne = ImageIO.read(ioIS);
								BufferedImage imgTwo = ImageIO.read(itIS);
								new ImageGridLogin(igRs.getInt(1), imgOne, imgTwo).setVisible(true);
								dispose();
							} catch (Exception e) {
								e.printStackTrace();
								System.err.println("Failed to create login page.");
							}
							
						} else {
							JOptionPane.showMessageDialog(loginButton, "User does not have image grid method details.");
						}
						
					} else if (getMethod() == Method.SIMPLE) {
						JOptionPane.showMessageDialog(loginButton, "Successfully logged in.");
						Welcome welcome = new Welcome();
						welcome.setVisible(true);
						dispose();
					} else if (getMethod() == Method.COIN) {
						// TODO Coin login
						PreparedStatement cpSt = (PreparedStatement) connection.prepareStatement("Select coinpass from coin_pass_method where userID=?");
						cpSt.setInt(1, userID);
						ResultSet cpRs = cpSt.executeQuery();
						if (cpRs.next()) {
							System.out.println("Got coin pass method details");
							String coinPass = cpRs.getString("coinpass");
							new CoinPassLogin(coinPass).setVisible(true);
							dispose();
						} else {
							JOptionPane.showMessageDialog(loginButton, "User does not have coin pass method details.");
						}
					} else if (getMethod() == Method.WHEEL) {
						PreparedStatement cpSt = (PreparedStatement) connection.prepareStatement("Select chosenColour from colour_wheel_method where userID=?");
						cpSt.setInt(1, userID);
						ResultSet cpRs = cpSt.executeQuery();
						if (cpRs.next()) {
							System.out.println("Got colour wheel method details");
							String chosenCol = cpRs.getString("chosenColour");
							new ColourWheelLogin(chosenCol).setVisible(true);
							dispose();
						} else {
							JOptionPane.showMessageDialog(loginButton, "User does not have coin pass method details.");
						}
					}
				} else {
					JOptionPane.showMessageDialog(loginButton, "Incorrect login details.");
				}
			} catch (SQLException sqlException){
				sqlException.printStackTrace();
				
			}
		} else if (btn.equals(backBtn)) {
			if (getMethod() == Method.COLOURGRID) {
				new ColourGridRegistration().setVisible(true);
				dispose();
			} else if (getMethod() == Method.IMAGEGRID) {
				new ImageGridRegistration().setVisible(true);
				dispose();
			} else if (getMethod() == Method.SIMPLE) {
				new SimpleRegistration().setVisible(true);
				dispose();				
			} else if (getMethod() == Method.COIN) {
				new CoinPassRegistration().setVisible(true);
				dispose();
			} else if (getMethod() == Method.WHEEL) {
				new ColourWheelRegistration().setVisible(true);
				dispose();
			}
		}
	}
	
	public Method getMethod() {
		return method;
	}
	
	public void setMethod(Method input) {
		method = input;
	}

}
