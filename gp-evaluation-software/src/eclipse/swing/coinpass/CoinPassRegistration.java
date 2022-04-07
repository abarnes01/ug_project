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
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import eclipse.sql.DatabaseRunner;
import eclipse.swing.InitialLogin;
import eclipse.swing.Method;
import eclipse.swing.Welcome;

public class CoinPassRegistration extends JFrame implements ActionListener {

	private static final long serialVersionUID = 575557513944314476L;
	private DatabaseRunner dbRunner;
	private JPanel contentPane, formPanel, buttonPanel, elementsPanel, mainPanel;
	private JLabel nameLbl, passwordLbl, coinPassLbl;
	private JButton registerBtn, backBtn;
	private JTextField nameField, coinPassField;
	private JPasswordField passField;
	private Integer iconEntered, numEntered, colEntered;
	private Map<ImageIcon, String> iconMap;
	private ArrayList<ImageIcon> iconArr;
	
	private static ArrayList<Color> colArr = new ArrayList<Color>( Arrays.asList(Color.RED, Color.BLUE, Color.PINK, Color.WHITE,
			Color.GREEN, Color.YELLOW, Color.BLACK, Color.ORANGE, Color.CYAN, Color.MAGENTA));
	private static Map<Color, String> colToStrMap;
	static {
		colToStrMap = new HashMap<>();
		colToStrMap.put(Color.RED, "red");
		colToStrMap.put(Color.BLUE, "blue");
		colToStrMap.put(Color.PINK, "pink");
		colToStrMap.put(Color.WHITE, "white");
		colToStrMap.put(Color.GREEN, "green");
		colToStrMap.put(Color.YELLOW, "yellow");
		colToStrMap.put(Color.BLACK, "black");
		colToStrMap.put(Color.ORANGE, "orange");
		colToStrMap.put(Color.CYAN, "cyan");
		colToStrMap.put(Color.MAGENTA, "magenta");
	}
	

	public CoinPassRegistration(DatabaseRunner dbRunner) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setTitle("Coin Password Registration");
		this.dbRunner = dbRunner;
		
		iconEntered = numEntered = colEntered = 0;
		
		formPanel = new JPanel();
		elementsPanel = new JPanel();
		buttonPanel = new JPanel();
		mainPanel = new JPanel();
		
		formPanel.setLayout(new GridLayout(3, 0));
		nameLbl = new JLabel("Username: ");
		passwordLbl = new JLabel("Password: ");
		coinPassLbl = new JLabel("Coin Pass: ");
		nameField = new JTextField(10);
		passField = new JPasswordField(10);
		coinPassField = new JTextField(10);
		coinPassField.setEditable(false);
		registerBtn = new JButton("Register");
		registerBtn.addActionListener(this);
		backBtn = new JButton("\u2190");
		backBtn.addActionListener(this);
		
		// get element J labels
		try {
			iconMap = new HashMap<>();
			iconArr = new ArrayList<ImageIcon>();
			for (int i = 0; i < 10; i++) {
				BufferedImage img = ImageIO.read(new File("Icons/" + Integer.toString(i) + ".png"));
				ImageIcon imgIcon = new ImageIcon(img);
				JLabel iconLabel = new JLabel(imgIcon);
				iconArr.add(imgIcon);
				iconMap.put(imgIcon, Integer.toString(i));
				iconLabel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						coinPassField.setText(coinPassField.getText() + ":" + iconMap.get(imgIcon) + ".png");
						iconEntered += 1;
					}
				});
				elementsPanel.add(iconLabel);
				
				
				JLabel numberLabel = new JLabel(Integer.toString(i+1));
				numberLabel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						coinPassField.setText(coinPassField.getText() + ":" + numberLabel.getText());
						numEntered += 1;
					}
				});
				elementsPanel.add(numberLabel);
				
				JLabel colourLabel = new JLabel(colToStrMap.get(colArr.get(i)));
				colourLabel.setForeground(colArr.get(i));
				colourLabel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						coinPassField.setText(coinPassField.getText() + ":" + colourLabel.getText());
						colEntered += 1;
					}
				});
				elementsPanel.add(colourLabel);
			}
			elementsPanel.setLayout(new GridLayout(10, 3));
			
		} catch (Exception e) {
			System.err.println("Unable to get elements.");
			e.printStackTrace();
		}
		formPanel.add(nameLbl);
		formPanel.add(nameField);
		formPanel.add(passwordLbl);
		formPanel.add(passField);
		formPanel.add(coinPassLbl);
		formPanel.add(coinPassField);
		mainPanel.add(formPanel);
		mainPanel.add(elementsPanel);
		buttonPanel.add(backBtn);
		buttonPanel.add(registerBtn);
		contentPane.add(mainPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		if (btn.equals(registerBtn)) {
			String username = nameField.getText();
			String password = String.valueOf(passField.getPassword());
			String coinpass = coinPassField.getText();
			String url = dbRunner.getDburl();
			String dbname = dbRunner.getDbname();
			String dbpass = dbRunner.getDbpass();
			String[] stringSplit = coinpass.split(":");
			
			if (username.isBlank() || password.isBlank() || coinpass.isBlank()) {
				JOptionPane.showMessageDialog(registerBtn, "Error: Username, password or Coin pass is empty.");
			} else if (stringSplit.length > 16) {
				JOptionPane.showMessageDialog(registerBtn, "Error: Coin pass is too long.");
				coinPassField.setText("");
				iconEntered = numEntered = colEntered = 0;
			} else if (iconEntered < 2 || numEntered < 2 || colEntered < 2) {
				JOptionPane.showMessageDialog(registerBtn, "Error: Two of each coin pass element needed.");
				coinPassField.setText("");
				iconEntered = numEntered = colEntered = 0;
			} else {
				try {
					Connection connection = DriverManager.getConnection(url,dbname,dbpass);
					insertCoinPassDetails(connection, username, password, coinpass);
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

	private void insertCoinPassDetails(Connection connection, String username, String password, String coinpass) throws Exception {
		PreparedStatement st = (PreparedStatement)connection.prepareStatement("Select username from user where username=?");
		st.setString(1, username);
		ResultSet rs = st.executeQuery();
		
		if (rs.next()) {
			JOptionPane.showMessageDialog(registerBtn, "User already exists. Inserting coin pass details only.");
		} else {
			String query = "INSERT INTO user(username,password) values('" + username + "','" + password.hashCode() + "')";
			Statement statement = connection.createStatement();
			int x = statement.executeUpdate(query);
			if(x == 0) {
				System.err.println("Error: User already exists.");
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
			JOptionPane.showMessageDialog(registerBtn, "Coin pass method for user already exists.");
		} else {
			String gridQuery = "INSERT INTO coin_pass_method(userID,coinpass) values(?, ?)";
			PreparedStatement gridst = (PreparedStatement)connection.prepareStatement(gridQuery);
			gridst.setInt(1, userID);
			gridst.setString(2, coinpass);
			int y = gridst.executeUpdate();
			if(y == 0) {
				System.err.println("Error: Coin pass method for user exists.");
			} else {
				new InitialLogin(dbRunner, Method.COIN).setVisible(true);
				dispose();
			}
		}
	}
}
