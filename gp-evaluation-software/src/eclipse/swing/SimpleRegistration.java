package eclipse.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
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
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class SimpleRegistration extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane, headerPanel, formPanel, buttonPanel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton registerButton;
	private JLabel headerLabel, usernameLabel, passwordLabel;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimpleRegistration frame = new SimpleRegistration();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SimpleRegistration() {
		
		// Auto
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// Create the features
		headerPanel = new JPanel();
		headerLabel = new JLabel("Simple Registration Form");
		formPanel = new JPanel();
		buttonPanel = new JPanel();
		usernameLabel = new JLabel("Username:");
		usernameField = new JTextField(10);
		passwordLabel = new JLabel("Password:");
		passwordField = new JPasswordField(10);

		
		// Create buttons
		registerButton = new JButton("Register");
		registerButton.addActionListener(this);
		
		// Set layout of form and grid constraints
		formPanel.setLayout(new GridLayout(3,1,10,10));
		
		// Set the features to the panels
		headerPanel.add(headerLabel);
		formPanel.add(usernameLabel);
		formPanel.add(usernameField);
		formPanel.add(passwordLabel);
		formPanel.add(passwordField);
		buttonPanel.add(registerButton);
		
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
		 * Register button action. Takes user name, password and tries to submit it to the database.
		 * Will give an error if either user name or password is blank or if the user already exists.
		 */
		if (btn.equals(registerButton)) {
			String username = usernameField.getText();
			String password = String.valueOf(passwordField.getPassword());
			String url = "jdbc:mysql://localhost:3306/gp_database";
			String dbname = "root";
			String dbpass = "Footyclone2001";
			if (username.isBlank() || password.isBlank()) {
				JOptionPane.showMessageDialog(registerButton, "Username or password is empty.");
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
						} else {
							new SimpleLogin().setVisible(true);
							dispose();
						}
					}
					connection.close();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}
	}

}
