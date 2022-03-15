package eclipse.swing;

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
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class SimpleRegistration extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane, formPanel, buttonPanel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton registerBtn, backBtn;
	private JLabel usernameLbl, passLbl;

	public SimpleRegistration() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setTitle("Simple Registration Form");
		
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
		
		// Set the features to the panels
		formPanel.add(usernameLbl);
		formPanel.add(usernameField);
		formPanel.add(passLbl);
		formPanel.add(passwordField);
		buttonPanel.add(backBtn);
		buttonPanel.add(registerBtn);
		
		contentPane.add(formPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		setResizable(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton btn = (JButton) event.getSource();

		if (btn.equals(registerBtn)) {
			String username = usernameField.getText();
			String password = String.valueOf(passwordField.getPassword());
			String url = "jdbc:mysql://localhost:3306/gp_database";
			String dbname = "root";
			String dbpass = "";
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
						String query = "INSERT INTO user(username,password) values('" + username + "','" + password.hashCode() + "')";
						Statement statement = connection.createStatement();
						int x = statement.executeUpdate(query);
						if(x == 0) {
							JOptionPane.showMessageDialog(registerBtn, "User already exists. 2nd box");
						} else {
							new InitialLogin(Method.SIMPLE).setVisible(true);
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
