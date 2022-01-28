package eclipse.swing.colourgrid;

import java.awt.BorderLayout;
import java.awt.Color;
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

import eclipse.swing.Method;

public class ColourGridLogin extends JFrame implements ActionListener{

	private JPanel contentPane, formPanel, buttonPanel, gridPanel, mainPanel;
	private static final long serialVersionUID = 1L;
	private JPanel headerPanel;
	private JTextField textField;
	private JPasswordField passwordField, pColourField;
	private JButton loginButton;
	private JLabel headerLabel, usernameLabel, passwordLabel, pColourLabel;
	private GridLayout gridLayout;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ColourGridLogin frame = new ColourGridLogin();
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
	public ColourGridLogin() {
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
		
		gridPanel = new JPanel();
		gridLayout = new GridLayout();
		
		mainPanel = new JPanel();
		
		Color[] colors = {Color.RED, Color.BLUE, Color.PINK, Color.WHITE, Color.GREEN, Color.YELLOW};
		
		// ================= GRID LAYOUT ====================
		
		gridPanel.setLayout(gridLayout);
		for (int i = 1; i <= 20; i++) {
			JTextField tf = new JTextField(String.valueOf(i),4);
			int random_int = (int)Math.floor(Math.random()*(5-1+1)+1);
			tf.setBackground(colors[random_int]);
			gridPanel.add(tf);
		}
		
		// ================= GRID LAYOUT ====================
		
		
		loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		
		headerPanel.add(headerLabel);
		formPanel.setLayout(new GridLayout(3,1,10,10));
		formPanel.add(usernameLabel);
		formPanel.add(textField);
		formPanel.add(passwordLabel);
		formPanel.add(passwordField);	
		buttonPanel.add(loginButton);
		
		mainPanel.add(formPanel);
		mainPanel.add(gridPanel);
		
		contentPane.add(headerPanel, BorderLayout.NORTH);
		contentPane.add(mainPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		//setResizable(false);
		
	}
	
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
				PreparedStatement st = (PreparedStatement) connection.prepareStatement("Select userID, username, password from user where username=? and password=?");
				st.setString(1, username);
				st.setInt(2, password);
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					System.out.println("Successful");
					PreparedStatement nextst = (PreparedStatement) connection.prepareStatement("Select patternPass from colour_grid_method where userID=?");
					int userID = rs.getInt("userID");
					System.out.println(userID);
					nextst.setInt(1, userID);
					ResultSet newrs = nextst.executeQuery();
					if (newrs.next()) {
						System.out.println("Got grid method details");
						String patternPass = newrs.getString("patternPass");
					} else {
						JOptionPane.showMessageDialog(loginButton, "User does not have colour grid method details.");
					}
				} else {
					JOptionPane.showMessageDialog(loginButton, "Incorrect login details.");
				}
			} catch (SQLException sqlException){
				sqlException.printStackTrace();
				
			}
		}
	}

}

