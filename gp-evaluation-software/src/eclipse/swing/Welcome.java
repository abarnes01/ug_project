package eclipse.swing;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import eclipse.swing.coinpass.CoinPassRegistration;
import eclipse.swing.colourgrid.ColourGridRegistration;
import eclipse.swing.imagegrid.ImageGridRegistration;
import eclipse.sql.*;

public class Welcome extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane, title, panel;
	private Choice choice;
	private JTextArea txtrAdamBarnes;
	private JLabel lblNewLabel;
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
		
		title = new JPanel();
		panel = new JPanel();
		choice = new Choice();
		txtrAdamBarnes = new JTextArea();
		lblNewLabel = new JLabel("Welcome!");
		scrollPane = new JScrollPane(txtrAdamBarnes);
		registerBtn = new JButton("Register");
		registerBtn.addActionListener(this);
		loginBtn = new JButton("Login");
		loginBtn.addActionListener(this);
		resetDbBtn = new JButton("Reset DB");
		resetDbBtn.addActionListener(this);
		
		title.add(lblNewLabel);
		txtrAdamBarnes.setBackground(SystemColor.window);
		txtrAdamBarnes.setLineWrap(true);
		txtrAdamBarnes.setEditable(false);
		txtrAdamBarnes.setWrapStyleWord(true);
		txtrAdamBarnes.setText("Adam Barnes - CO3201 Computer Science Project\nTitle: Avoiding Shoulder Surfing via Graphical Passwords\nIn this application, you can test out a few iterations of graphical passwords,\nand perform evaluative tools on them to see their performance and security.\nPlease select a graphical password to use below:");
		scrollPane.setBorder(null);
		
		panel.add(choice);
		choice.add("Simple Textual Method");
		choice.add("Image Grid Method");
		choice.add("Colour Grid Method");
		choice.add("Coin Password Method");
		panel.add(registerBtn);
		panel.add(loginBtn);
		panel.add(resetDbBtn);
		
		contentPane.add(title, BorderLayout.NORTH);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(panel, BorderLayout.SOUTH);
		setResizable(false);
	}

	/* ============ ActionPerformed ============
	 * Overridden action performed for handling all button click events in this window
	 * @param event ActionEvent object
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton btn = (JButton) event.getSource();
		// Button to go to next page based on the option selected in choice
		if (btn.equals(registerBtn)) {
			String data = "Password selected: " + choice.getItem(choice.getSelectedIndex());
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
			}
		} else if (btn.equals(loginBtn)) {
			String data = "Password selected: " + choice.getItem(choice.getSelectedIndex());
			if (choice.getItem(choice.getSelectedIndex()) == "Simple Textual Method") {
				SimpleLogin sl = new SimpleLogin();
				sl.setMethod(Method.SIMPLE);
				sl.setVisible(true);
				dispose();
			} else if (choice.getItem(choice.getSelectedIndex()) == "Image Grid Method") {
				SimpleLogin sl = new SimpleLogin();
				sl.setMethod(Method.IMAGEGRID);
				sl.setVisible(true);
				dispose();
			} else if (choice.getItem(choice.getSelectedIndex()) == "Colour Grid Method") {
				SimpleLogin sl = new SimpleLogin();
				sl.setMethod(Method.COLOURGRID);
				sl.setVisible(true);
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
