package eclipse.swing.grid;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import eclipse.swing.SimpleLogin;

public class GridRegistration extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane, headerPanel, formPanel, buttonPanel;
	private JLabel headerLabel, usernameLabel, passwordLabel, imageLabel, gridSizeLabel;
	private JTextField usernameField, gridSizeField;
	private JPasswordField passwordField;
	private JButton registerButton, browseImageButton;
	private JFileChooser fileChooser;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GridRegistration frame = new GridRegistration();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GridRegistration() {
		
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
		imageLabel = new JLabel("No Image Uploaded");
		
		// Browse Image Button
		browseImageButton = new JButton("Browse");
		browseImageButton.addActionListener(this);
		
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
		formPanel.add(imageLabel);
		formPanel.add(browseImageButton);
		formPanel.add(gridSizeLabel);
		formPanel.add(gridSizeField);
		buttonPanel.add(registerButton);
		
		contentPane.add(formPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		setResizable(false);
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
			String imagePath = imageLabel.getText();
			String url = "jdbc:mysql://localhost:3306/gp_database";
			String dbname = "root";
			String dbpass = "Footyclone2001";
			if (username.isBlank() || password.isBlank() || gridSizeStr.isBlank() || imageLabel.getText() == "No Image Uploaded") {
				JOptionPane.showMessageDialog(registerButton, "Error: Username, password, image or grid size is empty.");
			} else if (!testProperInt(gridSizeStr)) {
				JOptionPane.showMessageDialog(registerButton, "Error: Grid Size is not a number.");
			} else if ((Integer.parseInt(gridSizeStr) < 4) || (Integer.parseInt(gridSizeStr) > 20)) {
				JOptionPane.showMessageDialog(registerButton, "Error: Grid Size is smaller than 4 or bigger than 20.");
			} else {
				try {
					gridSize = Integer.parseInt(gridSizeStr);
					
					Connection connection = DriverManager.getConnection(url,dbname,dbpass);
					
					PreparedStatement st = (PreparedStatement) connection.prepareStatement("Select username from user where username=?");
					
					st.setString(1, username);
					ResultSet rs = st.executeQuery();
					
					if (rs.next()) {
						JOptionPane.showMessageDialog(registerButton, "User already exists.");
					} else {
						// ============== Image to database prep ===============
						
						byte[] rawBytes = null;
						FileInputStream fileInputStream = null;
						
						File fObj = new File(imagePath);
						fileInputStream = new FileInputStream(fObj);
						
						Integer imageLength = Integer.parseInt(String.valueOf(fObj.length()));
						
						rawBytes = new byte[imageLength];
						
						fileInputStream.read(rawBytes, 0, imageLength);
						
						// ============== Image to database prep ===============
						
						String query = "INSERT INTO user(username,password) values('" + username + "','" + password.hashCode() + "')";
						Statement statement = connection.createStatement();
						int x = statement.executeUpdate(query);
						if(x == 0) {
							JOptionPane.showMessageDialog(registerButton, "User already exists. 2nd box");
						} 
						
						PreparedStatement nextst = (PreparedStatement) connection.prepareStatement("Select userID from user where username=?");
						
						nextst.setString(1, username);
						ResultSet newrs = nextst.executeQuery();
						newrs.next();
						int userID = newrs.getInt("userID");
						
						System.out.print(userID);
						
						String gridQuery = "INSERT INTO grid_method(userID,grid_size,image) values('" + userID + "','" + gridSize + "','" + rawBytes + "')";
						int y = statement.executeUpdate(gridQuery);
						if(y == 0) {
							JOptionPane.showMessageDialog(registerButton, "Grid method for user already exists.");
						} else {
							new SimpleLogin().setVisible(true);
							dispose();
						}
					}
					connection.close();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		} else if (btn.equals(browseImageButton)) {
			fileChooser = new JFileChooser("C:\\", FileSystemView.getFileSystemView());
			fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "tif", "gif", "bmp"));
			int returnVal = fileChooser.showOpenDialog(formPanel);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String fileName = fileChooser.getSelectedFile().getName();
				String extension = fileName.substring(fileName.lastIndexOf("."));
				if (extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".tif") || extension.equalsIgnoreCase(".gif") || extension.equalsIgnoreCase(".bmp")) {
					imageLabel.setText(fileChooser.getSelectedFile().getPath());
				} else {
					JOptionPane.showMessageDialog(this, "File selected is not an Image", "Error", JOptionPane.ERROR_MESSAGE);  
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
