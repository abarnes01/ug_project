package eclipse.swing;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import eclipse.sql.DatabaseRunner;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class SimpleRegistration extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private GridBagConstraints gbc = new GridBagConstraints();
	private DatabaseRunner dbRunner;
	private JPanel contentPane, formPanel, buttonPanel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton registerBtn, backBtn;
	private JLabel usernameLbl, passLbl;

	public SimpleRegistration(DatabaseRunner dbRunner) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setTitle("Simple Registration Form");
		this.dbRunner = dbRunner;
		
		// Create the features
		formPanel = new JPanel();
		buttonPanel = new JPanel();
		usernameLbl = new JLabel("Username:");
		usernameField = new JTextField(10);
		passLbl = new JLabel("Password:");
		passwordField = new JPasswordField(10);

		// Create buttons
		registerBtn = new JButton("Register");
		registerBtn.addActionListener(this);
		backBtn = new JButton("\u2190");
		backBtn.addActionListener(this);
		
		formPanel.setLayout(new GridBagLayout());
		
		// Set the features to the panels
		gbc.gridx = 0; gbc.gridy = 0;
		formPanel.add(usernameLbl, gbc);
		
		gbc.gridx = 1; gbc.gridy = 0;
		formPanel.add(usernameField, gbc);
		
		gbc.gridx = 0; gbc.gridy = 1;
		formPanel.add(passLbl, gbc);
		
		gbc.gridx = 1; gbc.gridy = 1;
		formPanel.add(passwordField, gbc);
		
		buttonPanel.add(backBtn);
		buttonPanel.add(registerBtn);
		
		contentPane.add(formPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton btn = (JButton) event.getSource();
		/*
		 * Connect to database via driver,
		 * check if user exists,
		 * if not then insert the values given by the user
		 */
		if (btn.equals(registerBtn)) {
			String username = usernameField.getText();
			String password = String.valueOf(passwordField.getPassword());
			String url = dbRunner.getDburl();
			String dbname = dbRunner.getDbname();
			String dbpass = dbRunner.getDbpass();
			if (username.isBlank() || password.isBlank()) {
				JOptionPane.showMessageDialog(registerBtn, "Username or password is empty.");
			} else {
				try {
					Connection connection = DriverManager.getConnection(url,dbname,dbpass);
					PreparedStatement st = (PreparedStatement) connection.prepareStatement("Select username from user where username=?");
					st.setString(1, username);
					ResultSet rs = st.executeQuery();
					if (rs.next()) {
						JOptionPane.showMessageDialog(registerBtn, "User already exists.");
					} else {
						insertSimpleDetails(connection, username, password);
					}
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
	
	private void insertSimpleDetails(Connection connection, String username, String password) throws Exception {
		String query = "INSERT INTO user(username,password) values('" + username + "','" + password.hashCode() + "')";
		Statement statement = connection.createStatement();
		int x = statement.executeUpdate(query);
		if(x == 0) {
			JOptionPane.showMessageDialog(registerBtn, "User already exists.");
		} else {
			new InitialLogin(dbRunner, Method.SIMPLE).setVisible(true);
			dispose();
		}
	}
}
