package eclipse.swing;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.Security;
import java.awt.Label;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import java.awt.SystemColor;
import java.awt.Choice;

public class Welcome extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Welcome frame = new Welcome();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the frame.
	 */
	public Welcome() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel title = new JPanel();
		contentPane.add(title, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Welcome!");
		title.add(lblNewLabel);
		
		
		JTextArea txtrAdamBarnes = new JTextArea();
		txtrAdamBarnes.setBackground(SystemColor.window);
		txtrAdamBarnes.setLineWrap(true);
		txtrAdamBarnes.setEditable(false);
		txtrAdamBarnes.setWrapStyleWord(true);
		txtrAdamBarnes.setText("Adam Barnes - CO3201 Computer Science Project\nTitle: Avoiding Shoulder Surfing via Graphical Passwords\nIn this application, you can test out a few iterations of graphical passwords,\nand perform evaluative tools on them to see their performance and security.\nPlease select a graphical password to use below:");
		JScrollPane scrollPane = new JScrollPane(txtrAdamBarnes);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.setBorder(null);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		Choice choice = new Choice();
		panel.add(choice);
		
		choice.add("Textual Password");
		
		Button startPassword = new Button("Start");
		panel.add(startPassword);
		
		startPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String data = "Password selected: " + choice.getItem(choice.getSelectedIndex());
				txtrAdamBarnes.setText(data);
			}
		});
		
		
		
	}

}
