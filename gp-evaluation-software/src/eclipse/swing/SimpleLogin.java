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
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SimpleLogin extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private JPanel headerPanel;
	private JPanel contentPane, formPanel, buttonPanel;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JLabel headerLabel, usernameLabel, passwordLabel;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		headerPanel = new JPanel();
		headerLabel = new JLabel("Login Form");
		formPanel = new JPanel();
		buttonPanel = new JPanel();
		usernameLabel = new JLabel("Username: ");
		textField = new JTextField(10);
		passwordLabel = new JLabel("Password: ");
		passwordField = new JPasswordField(10);
		
		loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		
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
				PreparedStatement st = (PreparedStatement) connection.prepareStatement("Select username, password from user where username=? and password=?");
				st.setString(1, username);
				st.setInt(2, password);
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					System.out.println("Successful");
				} else {
					JOptionPane.showMessageDialog(loginButton, "Incorrect login details.");
				}
			} catch (SQLException sqlException){
				sqlException.printStackTrace();
				
			}
		}
	}

}
