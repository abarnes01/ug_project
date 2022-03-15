package eclipse.swing;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import eclipse.swing.coinpass.CoinPassRegistration;
import eclipse.swing.colourgrid.ColourGridRegistration;
import eclipse.swing.colourwheel.ColourWheelRegistration;
import eclipse.swing.imagegrid.ImageGridRegistration;
import eclipse.sql.*;

public class Welcome extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane, bottomPane;
	private Choice choice;
	private JEditorPane mainText;
	private JScrollPane scrollPane;
	private JButton registerBtn, loginBtn, resetDbBtn;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DatabaseRunner dbR = new DatabaseRunner();
					dbR.createDB();
					Welcome frame = new Welcome();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	public Welcome() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setTitle("Welcome");
		
		bottomPane = new JPanel();
		choice = new Choice();
		mainText = new JEditorPane();
		mainText.setContentType("text/html");
		scrollPane = new JScrollPane(mainText);
		registerBtn = new JButton("Register");
		registerBtn.addActionListener(this);
		loginBtn = new JButton("Login");
		loginBtn.addActionListener(this);
		resetDbBtn = new JButton("Reset DB");
		resetDbBtn.addActionListener(this);

		mainText.setBackground(SystemColor.window);
		mainText.setEditable(false);
		String WelcomeHtml = "<html><h1>Adam Barnes - CO3201 Computer Science Project</h1>"
				+ "<h3>Avoiding Shoulder Surfing via Graphical Passwords</h3>"
				+ "<p>In this application, you can test out 4 different graphical password approaches,"
				+ "to observe their effectiveness in avoiding shoulder surfing.</p>"
				+ "<h2>(Digraph) Image Grid Approach</h2>"
				+ "<p>The user selects two images for their password. In the login stage, there are different scenarios for login.</p>"
				+ "<p><strong>Scenario A:</strong> Both password images are diagonal to each other -> select either image with is of (P1x, P2y) or (P2x, P1y).</p>"
				+ "<p><strong>Scenario B:</strong> Both password images are on vertical vector -> select image below the chosen pass image.</p>"
				+ "<p><strong>Scenario C:</strong> Both password images are on horizontal vector -> select image to the right of the chosen pass image.</p>"
				+ "<h2>Colour Grid Approach</h2>"
				+ "<p>The user selects a colour and a 6 letter password. In the login stage, type the first letter of the colour the users password falls into.</p>"
				+ "<h2>Coin Password Approach</h2>"
				+ "<p>The users password is made up of 3 different elements: icon, number and colour. The user must choose at least one of each, and the password should be within 6 elements long.</p>"
				+ "<p>In the login stage, the user selects the 'coin' that their current element falls into, clicking confirm. This is done for each element of the password. If incorrect, the user starts again.</p>"
				+ "<h2>Colour Wheel Approach</h2>"
				+ "<p>The user selects a colour and a password that is of any letters, numbers, or '/' '.' characters.</p>"
				+ "<p>In the login stage, the user spins the wheel so that their colour is on the current character of their password, clicking confirm. There are 3 warnings given for incorrect entry.</p><br>"
				+ "<p>Each registered user can be connected one login of each graphical password.</p>"
				+ "<p>Please use the drop down to select the approach below.";
		mainText.setText(String.format(WelcomeHtml));
		scrollPane.setBorder(null);
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			   public void run() { 
			       scrollPane.getVerticalScrollBar().setValue(0);
			   }
		});
		
		bottomPane.add(choice);
		choice.add("Simple Textual Method");
		choice.add("Image Grid Method");
		choice.add("Colour Grid Method");
		choice.add("Coin Password Method");
		choice.add("Colour Wheel Method");
		bottomPane.add(registerBtn);
		bottomPane.add(loginBtn);
		bottomPane.add(resetDbBtn);
		
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(bottomPane, BorderLayout.SOUTH);
		setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JButton btn = (JButton) event.getSource();
		// Button to go to next page based on the option selected in choice
		if (btn.equals(registerBtn)) {
			if (choice.getItem(choice.getSelectedIndex()) == "Simple Textual Method") {
				new SimpleRegistration().setVisible(true);
				dispose();
			} else if (choice.getItem(choice.getSelectedIndex()) == "Image Grid Method") {
				new ImageGridRegistration().setVisible(true);
				dispose();
			} else if (choice.getItem(choice.getSelectedIndex()) == "Colour Grid Method") {
				new ColourGridRegistration().setVisible(true);
				dispose();
			} else if (choice.getItem(choice.getSelectedIndex()) == "Coin Password Method") {
				new CoinPassRegistration().setVisible(true);
				dispose();
			} else if (choice.getItem(choice.getSelectedIndex()) == "Colour Wheel Method") {
				new ColourWheelRegistration().setVisible(true);
				dispose();
			}
		} else if (btn.equals(loginBtn)) {
			if (choice.getItem(choice.getSelectedIndex()) == "Simple Textual Method") {
				new InitialLogin(Method.SIMPLE).setVisible(true);
				dispose();
			} else if (choice.getItem(choice.getSelectedIndex()) == "Image Grid Method") {
				new InitialLogin(Method.IMAGEGRID).setVisible(true);
				dispose();
			} else if (choice.getItem(choice.getSelectedIndex()) == "Colour Grid Method") {
				new InitialLogin(Method.COLOURGRID).setVisible(true);
				dispose();
			} else if (choice.getItem(choice.getSelectedIndex()) == "Coin Password Method") {
				new InitialLogin(Method.COIN).setVisible(true);
				dispose();
			} else if (choice.getItem(choice.getSelectedIndex()) == "Colour Wheel Method") {
				new InitialLogin(Method.WHEEL).setVisible(true);
				dispose();
			}
		} else if (btn.equals(resetDbBtn)) {
			DatabaseRunner dbRunner = new DatabaseRunner();
			try {
				dbRunner.dropDB();
				dbRunner.createDB();
				JOptionPane.showMessageDialog(resetDbBtn, "Database reinitialized.");
			} catch (Exception e) {
				System.err.println("Could not drop database.");
				e.printStackTrace();
			}
		}
	}

}
