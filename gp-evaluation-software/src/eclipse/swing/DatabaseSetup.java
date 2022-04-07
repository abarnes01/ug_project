package eclipse.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import eclipse.sql.DatabaseRunner;

public class DatabaseSetup extends JFrame implements ActionListener {

	private static final long serialVersionUID = -4272288257272974729L;
	private JPanel contentPane, formPanel, btnPanel;
	private JLabel dbUrlLbl, dbNameLbl, dbPassLbl;
	private JTextField dbUrlField, dbNameField;
	private JPasswordField dbPassField;
	private JButton confirmBtn;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DatabaseSetup frame = new DatabaseSetup();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public DatabaseSetup() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 250);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setTitle("Database Setup");
		
		formPanel = new JPanel();
		btnPanel = new JPanel();
		
		dbUrlLbl = new JLabel("DB Url: ");
		dbNameLbl = new JLabel("DB Name: ");
		dbPassLbl = new JLabel("DB Password: ");
		dbUrlField = new JTextField(25);
		dbNameField = new JTextField(20);
		dbPassField = new JPasswordField(20);
		
		// example text to help user
		dbUrlField.setText("jdbc:mysql://localhost:3306/gp_database");
		dbNameField.setText("root");
		dbPassField.setText("");
		
		formPanel.add(dbUrlLbl);
		formPanel.add(dbUrlField);
		formPanel.add(dbNameLbl);
		formPanel.add(dbNameField);
		formPanel.add(dbPassLbl);
		formPanel.add(dbPassField);
		
		confirmBtn = new JButton("Confirm");
		confirmBtn.addActionListener(this);
		btnPanel.add(confirmBtn);
		
		contentPane.add(formPanel, BorderLayout.CENTER);
		contentPane.add(btnPanel, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton)e.getSource();
		if (btn.equals(confirmBtn)) {
			DatabaseRunner dbRunner = new DatabaseRunner(dbUrlField.getText(), dbNameField.getText(), String.valueOf(dbPassField.getPassword()));
			new Welcome(dbRunner).setVisible(true);
			dispose();
		}
	}

}
