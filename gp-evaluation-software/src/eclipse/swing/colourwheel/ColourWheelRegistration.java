package eclipse.swing.colourwheel;

import java.awt.BorderLayout;
import java.awt.Color;
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

import eclipse.sql.DatabaseRunner;
import eclipse.swing.InitialLogin;
import eclipse.swing.Method;
import eclipse.swing.Welcome;

public class ColourWheelRegistration extends JFrame implements ActionListener {

	private static final long serialVersionUID = -5043498498119949928L;
	private DatabaseRunner dbRunner;
	private JPanel contentPane, formPanel, buttonPanel, coloursPanel, mainPanel;
	private JLabel nameLbl, passwordLbl, colChosenTxt, chosenCol, wheelPassLbl;
	private JButton registerBtn, backBtn;
	private JTextField nameField;
	private JPasswordField passField, wheelPassField;
	
	private static ArrayList<Color> colList = new ArrayList<Color>( Arrays.asList(Color.RED, Color.BLUE, Color.PINK,
			Color.GREEN, Color.YELLOW, Color.ORANGE, Color.CYAN, Color.MAGENTA));
	private static Map<Color, String> colToStrMap;
	static {
		colToStrMap = new HashMap<>();
		colToStrMap.put(Color.RED, "red");
		colToStrMap.put(Color.BLUE, "blue");
		colToStrMap.put(Color.PINK, "pink");
		colToStrMap.put(Color.GREEN, "green");
		colToStrMap.put(Color.YELLOW, "yellow");
		colToStrMap.put(Color.ORANGE, "orange");
		colToStrMap.put(Color.CYAN, "cyan");
		colToStrMap.put(Color.MAGENTA, "magenta");
	}

	public ColourWheelRegistration(DatabaseRunner dbRunner) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setTitle("Colour Wheel Registration");
		this.dbRunner = dbRunner;
		
		formPanel = new JPanel();
		coloursPanel = new JPanel();
		buttonPanel = new JPanel();
		mainPanel = new JPanel();
		
		formPanel.setLayout(new GridLayout(3, 0));
		nameLbl = new JLabel("Username: ");
		passwordLbl = new JLabel("Password: ");
		colChosenTxt = new JLabel("Selected colour: ");
		wheelPassLbl = new JLabel("Wheel Pass: ");
		chosenCol = new JLabel();
		nameField = new JTextField(10);
		passField = new JPasswordField(10);
		wheelPassField = new JPasswordField(10);
		registerBtn = new JButton("Register");
		registerBtn.addActionListener(this);
		backBtn = new JButton("\u2190");
		backBtn.addActionListener(this);
		
		formPanel.add(nameLbl);
		formPanel.add(nameField);
		formPanel.add(passwordLbl);
		formPanel.add(passField);
		formPanel.add(wheelPassLbl);
		formPanel.add(wheelPassField);
		formPanel.add(colChosenTxt);
		formPanel.add(chosenCol);
		formPanel.setLayout(new GridLayout(4, 1));
		
		int max = colList.size();
		
		for (int i = 0; i < max; i++) {
			String colourStr = colToStrMap.get(colList.get(i));
			JLabel colourLbl = new JLabel(colourStr);
			colourLbl.setForeground(colList.get(i));
			Border border = BorderFactory.createLineBorder(colList.get(i), 5);
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
		buttonPanel.add(backBtn);
		buttonPanel.add(registerBtn);
		contentPane.add(mainPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public Boolean containsValidChars(String input) {
		return input.matches("[a-zA-Z0-9./]*$");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		if (btn.equals(registerBtn)) {
			String username = nameField.getText();
			String password = String.valueOf(passField.getPassword());
			String colStr = chosenCol.getText();
			String wheelPass = String.valueOf(wheelPassField.getPassword());
			String url = dbRunner.getDburl();
			String dbname = dbRunner.getDbname();
			String dbpass = dbRunner.getDbpass();
			if (username.isBlank() || password.isBlank() || colStr.isBlank() || wheelPass.isBlank()) {
				JOptionPane.showMessageDialog(registerBtn, "Username, password, selected colour or wheel pass is empty.");
			} else if (!containsValidChars(wheelPass)) {
				JOptionPane.showMessageDialog(registerBtn, "Wheel pass must not contain special characters apart from . and /");
			} else {
				try {
					Connection connection = DriverManager.getConnection(url,dbname,dbpass);
					insertColourWheelDetails(connection, username, password, wheelPass, colStr);
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
	
	private void insertColourWheelDetails(Connection connection, String username, String password, String wheelPass, String colStr) throws Exception {
		PreparedStatement st = (PreparedStatement) connection.prepareStatement("Select username from user where username=?");
		st.setString(1, username);
		ResultSet rs = st.executeQuery();
		if (rs.next()) {
			JOptionPane.showMessageDialog(registerBtn, "User already exists: Registering colour wheel details only.");
		} else {
			String query = "INSERT INTO user(username,password) values('" + username + "','" + password.hashCode() + "')";
			Statement statement = connection.createStatement();
			int x = statement.executeUpdate(query);
			if(x == 0) {
				System.err.println("Error: User already exists.");
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
			JOptionPane.showMessageDialog(registerBtn, "Colour wheel method for user already exists.");
		} else {
			String gridQuery = "INSERT INTO colour_wheel_method(userID,chosenColour,wheelPass) values(?, ?, ?)";
			PreparedStatement gridst = (PreparedStatement)connection.prepareStatement(gridQuery);
			gridst.setInt(1, userID);
			gridst.setString(2, colStr);
			gridst.setString(3, wheelPass);
			int y = gridst.executeUpdate();
			if(y == 0) {
				System.err.println("Error: Colour wheel method for user already exists.");
			} else {
				new InitialLogin(dbRunner, Method.WHEEL).setVisible(true);
				dispose();
			}
		}
	}

}
