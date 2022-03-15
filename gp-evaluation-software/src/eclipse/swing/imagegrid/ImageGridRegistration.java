package eclipse.swing.imagegrid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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

public class ImageGridRegistration extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private DatabaseRunner dbRunner;
	private JPanel contentPane, formPanel, buttonPanel, imagesPanel, mainPanel;
	private JLabel usernameLabel, passwordLabel, gridSizeLabel, imageSelectLabel;
	private JTextField usernameField, gridSizeField, imageSelectField;
	private JPasswordField passwordField;
	private JButton registerButton, backBtn, rndmImgBtn, prstImgBtn;
	private ArrayList<BufferedImage> bufImages;
	private Boolean genRndmImg, genPrstImg;
	private BufferedImage imageOne, imageTwo;

	public ImageGridRegistration(DatabaseRunner dbRunner) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setTitle("Grid Method Registration");
		this.dbRunner = dbRunner;
		
		genRndmImg = genPrstImg = false;
		
		// Create the features
		formPanel = new JPanel();
		buttonPanel = new JPanel();
		usernameLabel = new JLabel("Username:");
		usernameField = new JTextField(10);
		passwordLabel = new JLabel("Password:");
		passwordField = new JPasswordField(10);
		imageSelectLabel = new JLabel("Pass Images: ");
		imageSelectField = new JTextField(10);
		imageSelectField.setEditable(false);
		rndmImgBtn = new JButton("Random Images");
		rndmImgBtn.setOpaque(true);
		rndmImgBtn.addActionListener(this);
		prstImgBtn = new JButton("Preset Images");
		prstImgBtn.setOpaque(true);
		prstImgBtn.addActionListener(this);
		imagesPanel = new JPanel();
		mainPanel = new JPanel();
		
		// Grid Size select
		gridSizeLabel = new JLabel("Grid Size (3-8)");
		gridSizeField = new JTextField(10);
		
		// Register Button
		registerButton = new JButton("Register");
		registerButton.addActionListener(this);
		backBtn = new JButton("\u2190");
		backBtn.addActionListener(this);
		
		// Set layout of form and grid constraints
		formPanel.setLayout(new GridLayout(0,2));
		
		// Set the features to the panels
		formPanel.add(usernameLabel);
		formPanel.add(usernameField);
		formPanel.add(passwordLabel);
		formPanel.add(passwordField);
		formPanel.add(gridSizeLabel);
		formPanel.add(gridSizeField);
		formPanel.add(imageSelectLabel);
		formPanel.add(imageSelectField);
		formPanel.add(rndmImgBtn);
		formPanel.add(prstImgBtn);
		
		mainPanel.add(formPanel);
		mainPanel.add(imagesPanel);
		
		buttonPanel.add(backBtn);
		buttonPanel.add(registerButton);
		
		contentPane.add(mainPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		genRandomImages();
	}
	
	public void genRandomImages() {
		// add images to array and print out
		bufImages = new ArrayList<BufferedImage>();
		try {
			// add 6 images to array
			for (int i = 1; i <= 6; i++) {
				URL url = new URL("https://picsum.photos/50");
				BufferedImage image = ImageIO.read(url.openStream());
				bufImages.add(image);
				JLabel imgLabel = new JLabel(new ImageIcon(image));
				imgLabel.setText(String.valueOf(i));
				Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
				imgLabel.setBorder(border);
				imgLabel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						if (imageOne == null) {
							imageOne = image;
							imageSelectField.setText(imgLabel.getText());
						} else if (imageTwo == null) {
							imageTwo = image;
							imageSelectField.setText(imageSelectField.getText() + " and " + imgLabel.getText());
						} else {
							int ans = JOptionPane.showConfirmDialog(imgLabel, "Reset chosen images and use this as image one?");
							if (ans == JOptionPane.YES_OPTION) {
								imageOne = image;
								imageTwo = null;
								imageSelectField.setText(imgLabel.getText());
							}
						}
					}
				});
				imagesPanel.add(imgLabel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton btn = (JButton) event.getSource();
		if (btn.equals(registerButton)) {
			String username = usernameField.getText();
			String password = String.valueOf(passwordField.getPassword());
			String gridSizeStr = gridSizeField.getText();
			Integer gridSize;
			String url = dbRunner.getDburl();
			String dbname = dbRunner.getDbname();
			String dbpass = dbRunner.getDbpass();
			if (username.isBlank() || password.isBlank() || gridSizeStr.isBlank() || imageSelectField.getText().isBlank() || (!genRndmImg && !genPrstImg)) {
				JOptionPane.showMessageDialog(registerButton, "Error: Username, password, image or grid size is empty.");
			} else if (!testProperInt(gridSizeStr)) {
				JOptionPane.showMessageDialog(registerButton, "Error: Grid Size is not a number.");
			} else if ((Integer.parseInt(gridSizeStr) < 3) || (Integer.parseInt(gridSizeStr) > 8)) {
				JOptionPane.showMessageDialog(registerButton, "Error: Grid Size is smaller than 3 or bigger than 8.");
			} else {
				try {
					gridSize = Integer.parseInt(gridSizeStr);
					
					Connection connection = DriverManager.getConnection(url,dbname,dbpass);
					
					PreparedStatement st = (PreparedStatement)connection.prepareStatement("Select username from user where username=?");
					
					st.setString(1, username);
					ResultSet rs = st.executeQuery();
					
					if (rs.next()) {
						JOptionPane.showMessageDialog(registerButton, "User already exists. Inserting image grid details only.");
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
					
					PreparedStatement checkdbst = (PreparedStatement) connection.prepareStatement("Select userID from image_grid_method where userID=?");
					checkdbst.setInt(1, userID);
					ResultSet checkdbrs = checkdbst.executeQuery();
					if (checkdbrs.next()) {
						JOptionPane.showMessageDialog(registerButton, "Image grid method for user already exists.");
					} else {
						String gridQuery = "INSERT INTO image_grid_method(userID,gridSize,imageOne,imageTwo,randomOrPreset) values(?, ?, ?, ?, ?)";
						PreparedStatement gridst = (PreparedStatement)connection.prepareStatement(gridQuery);
						
						ByteArrayOutputStream baosOne = new ByteArrayOutputStream();
						ImageIO.write(imageOne, "jpg", baosOne);
						InputStream isOne = new ByteArrayInputStream(baosOne.toByteArray());
						
						ByteArrayOutputStream baosTwo = new ByteArrayOutputStream();
						ImageIO.write(imageTwo, "jpg", baosTwo);
						InputStream isTwo = new ByteArrayInputStream(baosTwo.toByteArray());
						
						gridst.setInt(1, userID);
						gridst.setInt(2, gridSize);
						gridst.setBlob(3, isOne);
						gridst.setBlob(4, isTwo);
						
						if (genRndmImg) {
							gridst.setString(5, "random");
						} else if (genPrstImg) {
							gridst.setString(5, "preset");
						} else {
							System.err.println("Error. Random or preset not set.");
						}
						
						int y = gridst.executeUpdate();
						if(y == 0) {
							JOptionPane.showMessageDialog(registerButton, "Image grid method for user already exists. 2nd box");
						} else {
							new InitialLogin(dbRunner, Method.IMAGEGRID).setVisible(true);
							dispose();
						}
					}
					connection.close();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		} else if (btn.equals(backBtn)) {
			new Welcome(dbRunner).setVisible(true);
			dispose();
		} else if (btn.equals(rndmImgBtn)) {
			genRndmImg = true;
			genPrstImg = false;
			rndmImgBtn.setBackground(Color.PINK);
			prstImgBtn.setBackground(null);
		} else if (btn.equals(prstImgBtn)) {
			genRndmImg = false;
			genPrstImg = true;
			rndmImgBtn.setBackground(null);
			prstImgBtn.setBackground(Color.PINK);
		}
	}
	
	boolean testProperInt(String txt) {
		try {
			Integer.parseInt(txt);
			return true;
		} catch (NumberFormatException exception) {
			return false;
		}
	}
	
	
}
