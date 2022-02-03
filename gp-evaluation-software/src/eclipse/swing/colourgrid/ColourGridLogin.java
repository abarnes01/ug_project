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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.ArrayUtils;

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
		
		
		mainPanel = new JPanel();
		
		
		
		
		
		loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		
		headerPanel.add(headerLabel);
		formPanel.setLayout(new GridLayout(3,1,10,10));
		buttonPanel.add(loginButton);
		
		mainPanel.add(formPanel);
		
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
	
	public void makeGrid() {
		gridPanel = new JPanel();
		gridLayout = new GridLayout(6,6);
		// Array of all the colour tiles that need to be placed on the grid
		List<Color> coloursOnGrid = new ArrayList<Color>( Arrays.asList(Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED,
								Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE,
								Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK, Color.PINK,
								Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE,
								Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN,
								Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW));
		
		// in case patternPass exists
		try {
			// primitive char array of pattern pass turned into array list with object Character
			char[] rawPPCharArray = patternPass.toCharArray();
			List<Character> ppCharArray = new ArrayList<Character>();
			ppCharArray.addAll(Arrays.asList(ArrayUtils.toObject(rawPPCharArray)));
			
			// random generator
			Random rand = new Random();
			
			int randColourIndex = rand.nextInt(coloursOnGrid.size());
			
			Color patternPassColour = coloursOnGrid.get(randColourIndex);
			// ================= GRID LAYOUT ====================
			
			gridPanel.setLayout(gridLayout);
			
			for (int i = 1; i <= 36; i++) {
				
				JTextField tf = new JTextField(null, 2);
				
				randColourIndex = rand.nextInt(coloursOnGrid.size());
				
				tf.setBackground(coloursOnGrid.get(randColourIndex));
				
				if (coloursOnGrid.get(randColourIndex) == patternPassColour) {
					// set to random character of pattern pass and remove from pattern pass array
					int randPPIndex = rand.nextInt(ppCharArray.size());
					tf.setText(Character.toString(ppCharArray.get(randPPIndex)));
					ppCharArray.remove(randPPIndex);
				} else {
					tf.setText((Character.toString((char)rand.nextInt(26) + 'a')));
				}
				tf.setEditable(false);
				gridPanel.add(tf);
				coloursOnGrid.remove(randColourIndex);
			}
			
			
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		mainPanel.add(gridPanel);
	}

}

