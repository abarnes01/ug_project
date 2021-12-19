package eclipse.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
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

public class SimpleRegistration extends JFrame {

	private JPanel contentPane;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton registerButton;


	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public SimpleRegistration() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel headerPanel = new JPanel();
		contentPane.add(headerPanel, BorderLayout.NORTH);
		
		JLabel lblHeaderLabel = new JLabel("Registration Form");
		headerPanel.add(lblHeaderLabel);
		
		JPanel formPanel = new JPanel();
		contentPane.add(formPanel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Username:");
		formPanel.add(lblNewLabel);
		
		usernameField = new JTextField();
		formPanel.add(usernameField);
		usernameField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		formPanel.add(lblNewLabel_1);
		
		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		formPanel.add(passwordField);
		
		registerButton = new JButton("Register");
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String username = usernameField.getText();
				String password = String.valueOf(passwordField.getPassword());
				String url = "jdbc:mysql://localhost:3306/gp_database";
				String dbname = "root";
				String dbpass = "Footyclone2001";
				
				
				try {
					Connection connection = DriverManager.getConnection(url,dbname,dbpass);
					String query = "INSERT INTO user(username,password) values('" + username + "','" + password + "')";
					
					Statement statement = connection.createStatement();
					int x = statement.executeUpdate(query);
					if(x == 0) {
						JOptionPane.showMessageDialog(registerButton, "User already exists");
					} else {
						JOptionPane.showMessageDialog(registerButton, "User created");
					}
					connection.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		formPanel.add(registerButton);
	}

}
