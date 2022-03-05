package eclipse.swing.coinpass;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

public class CoinPassLogin extends JFrame implements ActionListener {

	private JPanel contentPane, headerPanel, coinPanel, formPanel, buttonPanel, mainPanel;
	private JLabel headerLabel, passEntryLabel;
	private JButton loginBtn, backBtn;
	private JPasswordField passEntryField;
	private String coinPass;

	public CoinPassLogin(String coinPass) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		setCoinPass(coinPass);
		
		headerPanel = new JPanel();
		coinPanel = new JPanel();
		formPanel = new JPanel();
		buttonPanel = new JPanel();
		mainPanel = new JPanel();
		
		headerLabel = new JLabel("Coin Password Login");
		passEntryLabel = new JLabel("Password: ");
		loginBtn = new JButton("Login");
		loginBtn.addActionListener(this);
		backBtn = new JButton("<");
		backBtn.addActionListener(this);
		passEntryField = new JPasswordField(14);
		passEntryField.setEditable(false);
	}
	
	public void setCoinPass(String input) {
		coinPass = input;
	}
	
	public String getCoinPass() {
		return coinPass;
	}
	
	public void makeCoins() {
		// TODO 10 j labels, each with 1 of 10 icons, 1 of 10 numbers, 1 of 10 colours
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO login checks whether pass field is same as entered stuff
	}

}
