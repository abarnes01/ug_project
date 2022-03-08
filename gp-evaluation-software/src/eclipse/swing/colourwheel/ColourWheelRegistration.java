package eclipse.swing.colourwheel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import eclipse.swing.InitialLogin;
import eclipse.swing.Method;
import eclipse.swing.Welcome;

public class ColourWheelRegistration extends JFrame implements ActionListener {

	private JPanel contentPane, headerPanel, formPanel, buttonPanel, coloursPanel, mainPanel;
	private JLabel headerLabel, nameLabel, passwordLabel, colChosenTxt, chosenCol;
	private JButton registerButton, backBtn;
	private JTextField nameField;
	private JPasswordField passField;
	private Map<Color, String> colourMap;
	private ArrayList<Color> colourList;

	public ColourWheelRegistration() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		headerPanel = new JPanel();
		formPanel = new JPanel();
		coloursPanel = new JPanel();
		buttonPanel = new JPanel();
		mainPanel = new JPanel();
		
		headerLabel = new JLabel("Coin Password Registration");
		formPanel.setLayout(new GridLayout(3, 0));
		nameLabel = new JLabel("Username: ");
		passwordLabel = new JLabel("Password: ");
		colChosenTxt = new JLabel("Selected colour: ");
		chosenCol = new JLabel();
		nameField = new JTextField(10);
		passField = new JPasswordField(10);
		registerButton = new JButton("Register");
		registerButton.addActionListener(this);
		backBtn = new JButton("<");
		backBtn.addActionListener(this);
		
		headerPanel.add(backBtn);
		headerPanel.add(headerLabel);
		
		formPanel.add(nameLabel);
		formPanel.add(nameField);
		formPanel.add(passwordLabel);
		formPanel.add(passField);
		formPanel.add(colChosenTxt);
		formPanel.add(chosenCol);
		
		colourMap = new HashMap<>();
		colourMap.put(Color.RED, "red");
		colourMap.put(Color.BLUE, "blue");
		colourMap.put(Color.PINK, "pink");
		colourMap.put(Color.GREEN, "green");
		colourMap.put(Color.YELLOW, "yellow");
		colourMap.put(Color.ORANGE, "orange");
		colourMap.put(Color.CYAN, "cyan");
		colourMap.put(Color.MAGENTA, "magenta");
		
		colourList = new ArrayList<Color>( Arrays.asList(Color.RED, Color.BLUE, Color.PINK,
				Color.GREEN, Color.YELLOW, Color.ORANGE, Color.CYAN, Color.MAGENTA));
		
		int max = colourList.size();
		
		for (int i = 0; i < max; i++) {
			String colourStr = colourMap.get(colourList.get(i));
			JLabel colourLbl = new JLabel(colourStr);
			colourLbl.setForeground(colourList.get(i));
			Border border = BorderFactory.createLineBorder(colourList.get(i), 5);
			colourLbl.setBorder(border);
			colourLbl.setBackground(Color.BLACK);
			colourLbl.setOpaque(true);
			colourLbl.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					System.out.println("clicked");
					chosenCol.setText(colourStr);
					chosenCol.setForeground(colourLbl.getForeground());
					Border border = BorderFactory.createLineBorder(colourLbl.getForeground(), 5);
					chosenCol.setBorder(border);
					chosenCol.setBackground(Color.BLACK);
					chosenCol.setOpaque(true);
				}
			});
			coloursPanel.add(colourLbl);
		}
		coloursPanel.setLayout(new GridLayout(2, 4));
		mainPanel.add(formPanel);
		mainPanel.add(coloursPanel);
		buttonPanel.add(registerButton);
		
		contentPane.add(headerPanel, BorderLayout.NORTH);
		contentPane.add(mainPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		
		if (btn.equals(registerButton)) {
			// TODO check if register button
			String username = nameField.getText();
			String password = String.valueOf(passField.getPassword());
			String colStr = chosenCol.getText();
			String url = "jdbc:mysql://localhost:3306/gp_database";
			String dbname = "root";
			String dbpass = "";
			if (username.isBlank() || password.isBlank() || colStr.isBlank()) {
				JOptionPane.showMessageDialog(registerButton, "Username, password or selected colour is empty.");
			} else {
				try {
					Connection connection = DriverManager.getConnection(url,dbname,dbpass);
					PreparedStatement st = (PreparedStatement) connection.prepareStatement("Select username from user where username=?");
					st.setString(1, username);
					ResultSet rs = st.executeQuery();
					if (rs.next()) {
						JOptionPane.showMessageDialog(registerButton, "User already exists: registering colour wheel details only.");
					} else {
						String query = "INSERT INTO user(username,password) values('" + username + "','" + password.hashCode() + "')";
						Statement statement = connection.createStatement();
						int x = statement.executeUpdate(query);
						if(x == 0) {
							JOptionPane.showMessageDialog(registerButton, "User already exists. 2nd box");
						}
					}
					
					
					PreparedStatement nextst = (PreparedStatement) connection.prepareStatement("Select userID from user where username=?");
					nextst.setString(1, username);
					ResultSet newrs = nextst.executeQuery();
					newrs.next();
					int userID = newrs.getInt("userID");
					
					PreparedStatement checkdbst = (PreparedStatement) connection.prepareStatement("Select userID from colour_wheel_method where userID=?");
					checkdbst.setInt(1, userID);
					ResultSet checkdbrs = checkdbst.executeQuery();
					if (checkdbrs.next()) {
						JOptionPane.showMessageDialog(registerButton, "Colour wheel method for user already exists.");
					} else {
						String gridQuery = "INSERT INTO colour_wheel_method(userID,chosenColour) values(?, ?)";
						PreparedStatement gridst = (PreparedStatement)connection.prepareStatement(gridQuery);
						gridst.setInt(1, userID);
						gridst.setString(2, colStr);
						int y = gridst.executeUpdate();
						if(y == 0) {
							JOptionPane.showMessageDialog(registerButton, "Colour wheel method for user already exists.");
						} else {
							new InitialLogin(Method.WHEEL).setVisible(true);
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
