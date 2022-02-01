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
	private JPasswordField pColourField;
	private JButton loginButton;
	private JLabel headerLabel, pColourLabel;
	private GridLayout gridLayout;
	private String patternPass;

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

		if (btn.equals(loginButton)) {
			String url = "jdbc:mysql://localhost:3306/gp_database";
			String dbname = "root";
			String dbpass = "Footyclone2001";
			/*
			 * try {
			 * 
			 * } catch (SQLException sqlException){ sqlException.printStackTrace();
			 * 
			 * }
			 */
		}
	}
	
	public void setPatternPass(String str) {
		patternPass = str;
	} 

}

