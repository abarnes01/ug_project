package eclipse.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import eclipse.swing.colourgrid.ColourGridLogin;
import eclipse.swing.colourgrid.ColourGridRegistration;
import eclipse.swing.imagegrid.ImageGridLogin;
import eclipse.swing.imagegrid.ImageGridRegistration;

public class SimpleLogin extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private JPanel headerPanel;
	private JPanel contentPane, formPanel, buttonPanel;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton loginButton, backBtn;
	private JLabel headerLabel, usernameLabel, passwordLabel;
	private Method method;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimpleLogin frame = new SimpleLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SimpleLogin() {
		// auto
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
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
	
	/* ============ ActionPerformed ============
	 * Overridden action performed for handling all button click events in this window
	 * @param event ActionEvent object
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton btn = (JButton) event.getSource();
		/*
		 * Login Button action.
		 * Will give an error if the login details are incorrect.
		 * Correct details will prompt the next page with evaluation.
		 */
		if (btn.equals(loginButton)) {
			String username = textField.getText();
			Integer password = String.valueOf(passwordField.getPassword()).hashCode();
			String url = "jdbc:mysql://localhost:3306/gp_database";
			String dbname = "root";
			String dbpass = "Footyclone2001";
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
							ColourGridLogin cgl = new ColourGridLogin();
							cgl.setPatternPass(patternPass);
							cgl.makeGrid();
							cgl.setVisible(true);
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
								
								ImageGridLogin igl = new ImageGridLogin();
								igl.setImages(imgOne, imgTwo);
								igl.setGridSize(igRs.getInt(1));
								igl.makeGrid();
								igl.setVisible(true);
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
