package eclipse.swing.coinpass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import eclipse.swing.InitialLogin;
import eclipse.swing.Method;
import eclipse.swing.Welcome;

public class CoinPassRegistration extends JFrame implements ActionListener {

	private static final long serialVersionUID = 575557513944314476L;
	private JPanel contentPane, headerPanel, formPanel, buttonPanel, elementsPanel, mainPanel;
	private JLabel headerLabel, nameLabel, passwordLabel, coinPassLabel;
	private JButton registerButton, backBtn;
	private JTextField nameField, coinPassField;
	private JPasswordField passField;
	

	public CoinPassRegistration() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 900);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		headerPanel = new JPanel();
		formPanel = new JPanel();
		elementsPanel = new JPanel();
		buttonPanel = new JPanel();
		mainPanel = new JPanel();
		
		headerLabel = new JLabel("Coin Password Registration");
		formPanel.setLayout(new GridLayout(3, 0));
		nameLabel = new JLabel("Username: ");
		passwordLabel = new JLabel("Password: ");
		coinPassLabel = new JLabel("Coin Pass: ");
		nameField = new JTextField(10);
		passField = new JPasswordField(10);
		coinPassField = new JTextField(10);
		coinPassField.setEditable(false);
		registerButton = new JButton("Register");
		registerButton.addActionListener(this);
		backBtn = new JButton("<");
		backBtn.addActionListener(this);
		
		// get element J labels
		
		try {
			
			Map<Color, String> colourMap = new HashMap<>();
			colourMap.put(Color.RED, "red");
			colourMap.put(Color.BLUE, "blue");
			colourMap.put(Color.PINK, "pink");
			colourMap.put(Color.WHITE, "white");
			colourMap.put(Color.GREEN, "green");
			colourMap.put(Color.YELLOW, "yellow");
			colourMap.put(Color.BLACK, "black");
			colourMap.put(Color.ORANGE, "orange");
			colourMap.put(Color.CYAN, "cyan");
			colourMap.put(Color.MAGENTA, "magenta");
			
			List<Color> colourList = new ArrayList<Color>( Arrays.asList(Color.RED, Color.BLUE, Color.PINK, Color.WHITE,
					Color.GREEN, Color.YELLOW, Color.BLACK, Color.ORANGE, Color.CYAN, Color.MAGENTA));
			
			Map<ImageIcon, String> iconMap = new HashMap<>();
			
			for (int i = 0; i < 10; i++) {
				//File pathToFile = new File("./Icons/" + Integer.toString(i) + ".png");
				BufferedImage img = ImageIO.read(new File("Icons/" + Integer.toString(i) + ".png"));
				ImageIcon imgIcon = new ImageIcon(img);
				JLabel iconLabel = new JLabel(imgIcon);
				iconMap.put(imgIcon, Integer.toString(i));
				iconLabel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						coinPassField.setText(coinPassField.getText() + ":" + iconMap.get(imgIcon) + ".png");
					}
				});
				elementsPanel.add(iconLabel);
				
				
				JLabel numberLabel = new JLabel(Integer.toString(i+1));
				numberLabel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						coinPassField.setText(coinPassField.getText() + ":" + numberLabel.getText());
					}
				});
				elementsPanel.add(numberLabel);
				
				
				JLabel colourLabel = new JLabel(colourMap.get(colourList.get(i)));
				colourLabel.setForeground(colourList.get(i));
				colourLabel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						coinPassField.setText(coinPassField.getText() + ":" + colourLabel.getText());
					}
				});
				elementsPanel.add(colourLabel);
			}
			
			elementsPanel.setLayout(new GridLayout(10, 3));
			
		} catch (Exception e) {
			System.err.println("Unable to get elements");
			e.printStackTrace();
		}
		
		headerPanel.add(backBtn);
		headerPanel.add(headerLabel);
		formPanel.add(nameLabel);
		formPanel.add(nameField);
		formPanel.add(passwordLabel);
		formPanel.add(passField);
		formPanel.add(coinPassLabel);
		formPanel.add(coinPassField);
		mainPanel.add(formPanel);
		mainPanel.add(elementsPanel);
		buttonPanel.add(registerButton);
		
		contentPane.add(headerPanel, BorderLayout.NORTH);
		contentPane.add(mainPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		
		if (btn.equals(registerButton)) {
			String username = nameField.getText();
			String password = String.valueOf(passField.getPassword());
			String coinpass = coinPassField.getText();
			String url = "jdbc:mysql://localhost:3306/gp_database";
			String dbname = "root";
			String dbpass = "Footyclone2001";
			
			if (username.isBlank() || password.isBlank() || coinpass.isBlank()) {
				JOptionPane.showMessageDialog(registerButton, "Error: Username, password or Coin pass is empty.");
			} else {
				try {
					Connection connection = DriverManager.getConnection(url,dbname,dbpass);
					
					PreparedStatement st = (PreparedStatement)connection.prepareStatement("Select username from user where username=?");
					
					st.setString(1, username);
					ResultSet rs = st.executeQuery();
					
					if (rs.next()) {
						JOptionPane.showMessageDialog(registerButton, "User already exists. Inserting coin pass details only.");
					} else {
						String query = "INSERT INTO user(username,password) values('" + username + "','" + password.hashCode() + "')";
						Statement statement = connection.createStatement();
						int x = statement.executeUpdate(query);
						if(x == 0) {
							JOptionPane.showMessageDialog(registerButton, "User already exists. 2nd box");
						} 
					}
					PreparedStatement useridst = (PreparedStatement) connection.prepareStatement("Select userID from user where username=?");
					useridst.setString(1, username);
					ResultSet useridrs = useridst.executeQuery();
					useridrs.next();
					int userID = useridrs.getInt("userID");
					
					PreparedStatement checkdbst = (PreparedStatement) connection.prepareStatement("Select userID from coin_pass_method where userID=?");
					checkdbst.setInt(1, userID);
					ResultSet checkdbrs = checkdbst.executeQuery();
					if (checkdbrs.next()) {
						JOptionPane.showMessageDialog(registerButton, "Coin pass method for user already exists.");
					} else {
						String gridQuery = "INSERT INTO coin_pass_method(userID,coinpass) values(?, ?)";
						PreparedStatement gridst = (PreparedStatement)connection.prepareStatement(gridQuery);
						
						gridst.setInt(1, userID);
						gridst.setString(2, coinpass);
						int y = gridst.executeUpdate();
						if(y == 0) {
							JOptionPane.showMessageDialog(registerButton, "Coin pass method for user already exists. 2nd box");
						} else {
							new InitialLogin(Method.COIN).setVisible(true);
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
