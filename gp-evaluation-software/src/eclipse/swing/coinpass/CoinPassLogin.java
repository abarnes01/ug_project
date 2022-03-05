package eclipse.swing.coinpass;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

public class CoinPassLogin extends JFrame {

	private JPanel contentPane, headerPanel, coinPanel, formPanel, buttonPanel, mainPanel;
	private JLabel headerLabel, passEntryLabel;
	private JButton loginBtn, backBtn;
	private JPasswordField passEntryField;

	public CoinPassLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		headerPanel = new JPanel();
		coinPanel = new JPanel();
		formPanel = new JPanel();
		buttonPanel = new JPanel();
		mainPanel = new JPanel();
		
		headerLabel = new JLabel("Coin Password Login");
		passEntryLabel = new JLabel("Password: ");
		loginBtn = new JButton("Login");
		backBtn = new JButton("<");
		passEntryField = new JPasswordField(14);
		passEntryField.setEditable(false);
	}

}
