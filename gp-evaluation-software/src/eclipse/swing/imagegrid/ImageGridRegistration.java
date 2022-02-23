package eclipse.swing.imagegrid;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;

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

import eclipse.swing.Method;
import eclipse.swing.SimpleLogin;

public class ImageGridRegistration extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane, headerPanel, formPanel, buttonPanel, imagesPanel, mainPanel;
	private JLabel headerLabel, usernameLabel, passwordLabel, gridSizeLabel, imageSelectLabel;
	private JTextField usernameField, gridSizeField, imageSelectField;
	private JPasswordField passwordField;
	private JButton registerButton;
	private ArrayList<BufferedImage> bufImages;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageGridRegistration frame = new ImageGridRegistration();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ImageGridRegistration() {
		
		// Auto
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// Create the features
		headerPanel = new JPanel();
		headerLabel = new JLabel("Grid Method Registration Form");
		formPanel = new JPanel();
		buttonPanel = new JPanel();
		usernameLabel = new JLabel("Username:");
		usernameField = new JTextField(10);
		passwordLabel = new JLabel("Password:");
		passwordField = new JPasswordField(10);
		imageSelectLabel = new JLabel("Enter the number of the two images you want to select, with a space in between.");
		imageSelectField = new JTextField(10);
		imagesPanel = new JPanel();
		mainPanel = new JPanel();
		
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
				imagesPanel.add(imgLabel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Grid Size select
		gridSizeLabel = new JLabel("Grid Size (4-20)");
		gridSizeField = new JTextField(10);
		
		// Register Button
		registerButton = new JButton("Register");
		registerButton.addActionListener(this);
		
		// Set layout of form and grid constraints
		formPanel.setLayout(new GridLayout(4,0));
		
		// Set the features to the panels
		contentPane.add(headerPanel, BorderLayout.NORTH);
		headerPanel.add(headerLabel);
		
		formPanel.add(usernameLabel);
		formPanel.add(usernameField);
		formPanel.add(passwordLabel);
		formPanel.add(passwordField);
		formPanel.add(gridSizeLabel);
		formPanel.add(gridSizeField);
		formPanel.add(imageSelectLabel);
		formPanel.add(imageSelectField);
		
		mainPanel.add(formPanel);
		mainPanel.add(imagesPanel);
		
		buttonPanel.add(registerButton);
		
		contentPane.add(mainPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/* ============ ActionPerformed ============
	 * Overridden action performed for handling all button click events in this window
	 * @param event ActionEvent object
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton btn = (JButton) event.getSource();
		/*
		 * Register button action. Takes user name, password and tries to submit it to the database.
		 * Will give an error if either user name or password is blank or if the user already exists.
		 */
		if (btn.equals(registerButton)) {
			String username = usernameField.getText();
			String password = String.valueOf(passwordField.getPassword());
			String gridSizeStr = gridSizeField.getText();
			Integer gridSize;
			String url = "jdbc:mysql://localhost:3306/gp_database";
			String dbname = "root";
			String dbpass = "Footyclone2001";
			if (username.isBlank() || password.isBlank() || gridSizeStr.isBlank() || imageSelectField.getText().isBlank()) {
				JOptionPane.showMessageDialog(registerButton, "Error: Username, password, image or grid size is empty.");
			} else if (!testProperInt(gridSizeStr)) {
				JOptionPane.showMessageDialog(registerButton, "Error: Grid Size is not a number.");
			} else if ((Integer.parseInt(gridSizeStr) < 4) || (Integer.parseInt(gridSizeStr) > 20)) {
				JOptionPane.showMessageDialog(registerButton, "Error: Grid Size is smaller than 4 or bigger than 20.");
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
						String gridQuery = "INSERT INTO image_grid_method(userID,gridSize,imageOne,imageTwo) values(?, ?, ?, ?)";
						PreparedStatement gridst = (PreparedStatement)connection.prepareStatement(gridQuery);
						
						String[] split = imageSelectField.getText().split("\\s+");
						BufferedImage imageOne = bufImages.get(Integer.parseInt(split[0])-1);
						BufferedImage imageTwo = bufImages.get(Integer.parseInt(split[1])-1);
						
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
						int y = gridst.executeUpdate();
						if(y == 0) {
							JOptionPane.showMessageDialog(registerButton, "Image grid method for user already exists. 2nd box");
						} else {
							SimpleLogin sl = new SimpleLogin();
							sl.setMethod(Method.IMAGEGRID);
							sl.setVisible(true);
							dispose();
						}
					}
					connection.close();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
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
