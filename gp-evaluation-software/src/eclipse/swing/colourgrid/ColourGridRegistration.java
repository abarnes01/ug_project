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

import eclipse.sql.DatabaseRunner;
import eclipse.swing.InitialLogin;
import eclipse.swing.Method;
import eclipse.swing.Welcome;

public class ColourGridRegistration extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private DatabaseRunner dbRunner;
	private JPanel contentPane, formPanel, BtnPanel, mainPanel;
	private JTextField usernameField;
	private JPasswordField passwordField, patternPField;
	private JButton registerBtn, backBtn;
	private JLabel usernameLbl, passwordLbl, patternPLbl;

	public ColourGridRegistration(DatabaseRunner dbRunner) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setTitle("Colour Grid Registration");
		this.dbRunner = dbRunner;
		
		// create the features
		mainPanel = new JPanel();
		formPanel = new JPanel();
		BtnPanel = new JPanel();
		usernameLbl = new JLabel("Username:");
		usernameField = new JTextField(10);
		passwordLbl = new JLabel("Password:");
		passwordField = new JPasswordField(10);
		patternPLbl = new JLabel("Pattern Pass (6 chars):");
		patternPField = new JPasswordField(6);
		
		// create buttons
		registerBtn = new JButton("Register");
		registerBtn.addActionListener(this);
		backBtn = new JButton("\u2190");
		backBtn.addActionListener(this);
		
		// set layout of form and grid constraints
		formPanel.setLayout(new GridLayout(3,1,10,10));
		
		// set the features to the panels
		formPanel.add(usernameLbl);
		formPanel.add(usernameField);
		formPanel.add(passwordLbl);
		formPanel.add(passwordField);
		formPanel.add(patternPLbl);
		formPanel.add(patternPField);
		BtnPanel.add(backBtn);
		BtnPanel.add(registerBtn);
		mainPanel.add(formPanel);
		contentPane.add(mainPanel, BorderLayout.CENTER);
		contentPane.add(BtnPanel, BorderLayout.SOUTH);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton btn = (JButton) event.getSource();
		if (btn.equals(registerBtn)) {
			String username = usernameField.getText();
			String password = String.valueOf(passwordField.getPassword());
			String patternPass = String.valueOf(patternPField.getPassword());
			String url = dbRunner.getDburl();
			String dbname = dbRunner.getDbname();
			String dbpass = dbRunner.getDbpass();
			if (username.isBlank() || password.isBlank() || patternPass.length() != 6) {
				JOptionPane.showMessageDialog(registerBtn, "Username or password is empty or pattern pass does not equal 6.");
			} else {
				try {
					Connection connection = DriverManager.getConnection(url,dbname,dbpass);
					insertColourGridDetails(connection, username, password, patternPass);
					connection.close();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		} else if (btn.equals(backBtn)) {
			new Welcome(dbRunner).setVisible(true);
			dispose();
		}
	}
	
	private void insertColourGridDetails(Connection connection, String username, String password, String patternPass) throws Exception {
		PreparedStatement st = (PreparedStatement) connection.prepareStatement("Select username from user where username=?");
		st.setString(1, username);
		ResultSet rs = st.executeQuery();
		if (rs.next()) {
			JOptionPane.showMessageDialog(registerBtn, "User already exists.");
		} else {
			String query = "INSERT INTO user(username,password) values('" + username + "','" + password.hashCode() + "')";
			Statement statement = connection.createStatement();
			int x = statement.executeUpdate(query);
			if(x == 0) {
				System.err.println("Error: User already exists. Insertion failed.");
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
			JOptionPane.showMessageDialog(registerBtn, "Colour grid method for user already exists.");
		} else {
			String gridQuery = "INSERT INTO colour_grid_method(userID,patternPass) values(?, ?)";
			PreparedStatement gridst = (PreparedStatement)connection.prepareStatement(gridQuery);
			gridst.setInt(1, userID);
			gridst.setString(2, patternPass);
			int y = gridst.executeUpdate();
			if(y == 0) {
				System.err.println("Error: Colour grid details for user already exists. Insertion failed.");
			} else {
				new InitialLogin(dbRunner, Method.COLOURGRID).setVisible(true);
				dispose();
			}
		}
	}

}
