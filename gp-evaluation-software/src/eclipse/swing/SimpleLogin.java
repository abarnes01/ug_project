package eclipse.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
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
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SimpleLogin extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel headerPanel;
	private JPanel formPanel;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton loginButton;

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public SimpleLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		headerPanel = new JPanel();
		contentPane.add(headerPanel, BorderLayout.NORTH);
		
		JLabel lblHeaderLabel = new JLabel("Login Form");
		headerPanel.add(lblHeaderLabel);
		
		formPanel = new JPanel();
		contentPane.add(formPanel, BorderLayout.CENTER);
		
		JLabel lblUsername = new JLabel("Username: ");
		formPanel.add(lblUsername);
		
		textField = new JTextField();
		formPanel.add(textField);
		
		JLabel lblPassword = new JLabel("Password: ");
		formPanel.add(lblPassword);
		
		passwordField = new JPasswordField();
		formPanel.add(passwordField);
		
		textField.setColumns(10);
		passwordField.setColumns(10);
		
		loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = textField.getText();
				String password = passwordField.getPassword().toString();
				System.out.print(password);
				String url = "jdbc:mysql://localhost:3306/gp_database";
				String dbname = "root";
				String dbpass = "Footyclone2001";
				
				try {
					Connection connection = (Connection) DriverManager.getConnection(url,dbname,dbpass);
					PreparedStatement st = (PreparedStatement) connection.prepareStatement("Select * from user where username=?");
					
					st.setString(1, username);
					ResultSet rs = st.executeQuery();
			
					
					System.out.print(username);
					System.out.print(password);
					
					if (rs.next()) {
						System.out.println("Successful");
					} else {
						System.out.println("Failed");
					}
					
				} catch (SQLException sqlException){
					sqlException.printStackTrace();
					
				}
			}
		});
		formPanel.add(loginButton);
		
	}

}
