package eclipse.swing.colourgrid;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import eclipse.swing.InitialLogin;
import eclipse.swing.Method;
import eclipse.swing.Welcome;

public class ColourGridRegistration extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane, headerPanel, formPanel, buttonPanel, mainPanel;
	private JTextField usernameField;
	private JPasswordField passwordField, patternPField;
	private JButton registerButton, backBtn;
	private JLabel headerLabel, usernameLabel, passwordLabel, patternPLabel;

	public ColourGridRegistration() {
		// auto
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// create the features
		headerPanel = new JPanel();
		headerLabel = new JLabel("Colour Grid Registration Form");
		mainPanel = new JPanel();
		formPanel = new JPanel();
		buttonPanel = new JPanel();
		usernameLabel = new JLabel("Username:");
		usernameField = new JTextField(10);
		passwordLabel = new JLabel("Password:");
		passwordField = new JPasswordField(10);
		patternPLabel = new JLabel("Pattern Pass (6 chars):");
		patternPField = new JPasswordField(6);
		
		// create buttons
		registerButton = new JButton("Register");
		registerButton.addActionListener(this);
		backBtn = new JButton("<");
		backBtn.addActionListener(this);
		
		// set layout of form and grid constraints
		formPanel.setLayout(new GridLayout(3,1,10,10));
		
		// set the features to the panels
		headerPanel.add(backBtn);
		headerPanel.add(headerLabel);
		formPanel.add(usernameLabel);
		formPanel.add(usernameField);
		formPanel.add(passwordLabel);
		formPanel.add(passwordField);
		formPanel.add(patternPLabel);
		formPanel.add(patternPField);
		buttonPanel.add(registerButton);
		mainPanel.add(formPanel);
		contentPane.add(headerPanel, BorderLayout.NORTH);
		contentPane.add(mainPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		//setResizable(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton btn = (JButton) event.getSource();
		if (btn.equals(registerButton)) {
			String username = usernameField.getText();
			String password = String.valueOf(passwordField.getPassword());
			String patternPass = String.valueOf(patternPField.getPassword());
			String url = "jdbc:mysql://localhost:3306/gp_database";
			String dbname = "root";
			String dbpass = "";
			if (username.isBlank() || password.isBlank() || patternPass.length() != 6) {
				JOptionPane.showMessageDialog(registerButton, "Username or password is empty or pattern pass does not equal 6.");
			} else {
				try {
					Connection connection = DriverManager.getConnection(url,dbname,dbpass);
					PreparedStatement st = (PreparedStatement) connection.prepareStatement("Select username from user where username=?");
					st.setString(1, username);
					ResultSet rs = st.executeQuery();
					if (rs.next()) {
						JOptionPane.showMessageDialog(registerButton, "User already exists.");
					} else {
						String query = "INSERT INTO user(username,password) values('" + username + "','" + password.hashCode() + "')";
						Statement statement = connection.createStatement();
						int x = statement.executeUpdate(query);
						if(x == 0) {
							JOptionPane.showMessageDialog(registerButton, "User already exists. 2nd box");
						}
					}
					PreparedStatement nextst = (PreparedStatement) connection.prepareStatement("Select userID from user where username=?");
					nextst.setString(1, username);
					ResultSet newrs = nextst.executeQuery();
					newrs.next();
					int userID = newrs.getInt("userID");
					
					PreparedStatement checkdbst = (PreparedStatement) connection.prepareStatement("Select userID from colour_grid_method where userID=?");
					checkdbst.setInt(1, userID);
					ResultSet checkdbrs = checkdbst.executeQuery();
					if (checkdbrs.next()) {
						JOptionPane.showMessageDialog(registerButton, "Colour grid method for user already exists.");
					} else {
						String gridQuery = "INSERT INTO colour_grid_method(userID,patternPass) values(?, ?)";
						PreparedStatement gridst = (PreparedStatement)connection.prepareStatement(gridQuery);
						gridst.setInt(1, userID);
						gridst.setString(2, patternPass);
						int y = gridst.executeUpdate();
						if(y == 0) {
							JOptionPane.showMessageDialog(registerButton, "Colour grid method for user already exists.");
						} else {
							new InitialLogin(Method.COLOURGRID).setVisible(true);
							dispose();
						}
					}
					connection.close();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		} else if (btn.equals(backBtn)) {
			new Welcome().setVisible(true);
			dispose();
		}
	}

}
