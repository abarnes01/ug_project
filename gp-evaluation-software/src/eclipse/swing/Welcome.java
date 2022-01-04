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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import eclipse.swing.grid.GridRegistration;

public class Welcome extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane, title, panel;
	private Choice choice;
	private JTextArea txtrAdamBarnes;
	private JLabel lblNewLabel;
	private JScrollPane scrollPane;
	private JButton startPassword;
	
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

	public Welcome() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
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
		startPassword = new JButton("Start");
		startPassword.addActionListener(this);
		
		title.add(lblNewLabel);
		txtrAdamBarnes.setBackground(SystemColor.window);
		txtrAdamBarnes.setLineWrap(true);
		txtrAdamBarnes.setEditable(false);
		txtrAdamBarnes.setWrapStyleWord(true);
		txtrAdamBarnes.setText("Adam Barnes - CO3201 Computer Science Project\nTitle: Avoiding Shoulder Surfing via Graphical Passwords\nIn this application, you can test out a few iterations of graphical passwords,\nand perform evaluative tools on them to see their performance and security.\nPlease select a graphical password to use below:");
		scrollPane.setBorder(null);
		
		panel.add(choice);
		choice.add("Simple Textual Method");
		choice.add("Grid Method");
		panel.add(startPassword);
		
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
		if (btn.equals(startPassword)) {
			String data = "Password selected: " + choice.getItem(choice.getSelectedIndex());
			if (choice.getItem(choice.getSelectedIndex()) == "Simple Textual Method") {
				new SimpleRegistration().setVisible(true);
				dispose();
			} else if (choice.getItem(choice.getSelectedIndex()) == "Grid Method") {
				// grid method page
				new GridRegistration().setVisible(true);
				dispose();
			}
		}
	}

}
