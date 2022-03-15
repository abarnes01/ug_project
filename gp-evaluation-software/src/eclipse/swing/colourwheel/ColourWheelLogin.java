package eclipse.swing.colourwheel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import eclipse.sql.DatabaseRunner;
import eclipse.swing.Welcome;

public class ColourWheelLogin extends JFrame implements ActionListener {

	private static final long serialVersionUID = -6168526356154212099L;
	private DatabaseRunner dbRunner;
	private JPanel contentPane, headerPanel, formPanel;
	private JLabel headerLbl;
	private JButton rotLftBtn, rotRgtBtn, loginBtn, entryBtn;
	private int width, height;
	private String chosenCol;
	private List<Color> colourList;
	private List<List<String>> charLists;
	private WheelCanvas wc;
	private Map<String, Color> colourMap;
	private String[] wpLetters;
	private int passCount;
	private int passLength;
	private int warningCount;

	public ColourWheelLogin(DatabaseRunner dbRunner, String chosenCol, String wheelPass) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		width = 450;
		height = 300;
		setBounds(100, 100, width+10, (height*2));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		this.dbRunner = dbRunner;
		
		this.chosenCol = chosenCol;
		wpLetters = wheelPass.split("");
		
		passCount = 0;
		passLength = wpLetters.length;
		warningCount = 0;
		
		headerPanel = new JPanel();
		headerLbl = new JLabel("Colour Wheel");
		headerPanel.add(headerLbl);
		
		// get rotate icons
		try {
			BufferedImage rotRgt = ImageIO.read(new File("Icons/clockwise.png"));
			BufferedImage rotLft = ImageIO.read(new File("Icons/anticlockwise.png"));
			rotLftBtn = new JButton(new ImageIcon(rotLft));
			rotRgtBtn = new JButton(new ImageIcon(rotRgt));
		} catch (IOException e) {
			e.printStackTrace();
			rotLftBtn = new JButton("Anticlockwise");
			rotRgtBtn = new JButton("Clockwise");
		}
		
		formPanel = new JPanel();
		rotLftBtn.addActionListener(this);
		rotRgtBtn.addActionListener(this);
		entryBtn = new JButton("Confirm");
		entryBtn.addActionListener(this);
		loginBtn = new JButton("Login");
		loginBtn.addActionListener(this);
		formPanel.add(rotLftBtn);
		formPanel.add(rotRgtBtn);
		formPanel.add(entryBtn);
		formPanel.add(loginBtn);
		
		colourMap = new HashMap<>();
		colourMap.put("red", Color.RED);
		colourMap.put("blue", Color.BLUE);
		colourMap.put("pink", Color.PINK);
		colourMap.put("white", Color.WHITE);
		colourMap.put("green", Color.GREEN);
		colourMap.put("yellow", Color.YELLOW);
		colourMap.put("black", Color.BLACK);
		colourMap.put("orange", Color.ORANGE);
		colourMap.put("cyan", Color.CYAN);
		colourMap.put("magenta", Color.MAGENTA);
		
		colourList = new ArrayList<Color>( Arrays.asList(Color.RED, Color.BLUE, Color.PINK,
	    		  Color.GREEN, Color.YELLOW, Color.ORANGE, Color.CYAN, Color.MAGENTA));
		Collections.shuffle(colourList, new Random());
		
		charLists = new ArrayList<List<String>>();
		
		String all64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789./";
		List<String> charsSplit = new ArrayList<String>(Arrays.asList(all64Chars.split("")));
		Collections.shuffle(charsSplit, new Random());
		
		for (int i = 0; i < 8; i++) {
			List<String> currentChars = new ArrayList<String>();
			currentChars.addAll(charsSplit.subList(8*i, 8*(i+1)));
			charLists.add(currentChars);
		}
		
		wc = new WheelCanvas(width, height, colourList, charLists);
		contentPane.add(headerPanel, BorderLayout.NORTH);
		contentPane.add(wc, BorderLayout.CENTER);
		contentPane.add(formPanel, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		
		if (btn.equals(rotLftBtn)) {
			Collections.rotate(colourList, 1);
			wc.repaint();
		} else if (btn.equals(rotRgtBtn)) {
			Collections.rotate(colourList, -1);
			wc.repaint();
		} else if (btn.equals(entryBtn)) {
			for (int i = 0; i < 8; i++) {
				if (colourMap.get(chosenCol) == colourList.get(i)) {
					Boolean isCorrect = false;
					if (passCount < passLength) {
						for (int j = 0; j < 8; j++) {
							if (charLists.get(i).get(j).equals(wpLetters[passCount])) {
								System.out.println("Correct.");
								passCount += 1;
								isCorrect = true;
								break;
							}
						}
					}
					if (!isCorrect) {
						warningCount += 1;
						if (warningCount == 3) {
							JOptionPane.showMessageDialog(entryBtn, "Too many warnings: Login failed.");
							new Welcome(dbRunner).setVisible(true);
							dispose();
						} else {
							JOptionPane.showMessageDialog(entryBtn, "Incorrect: Warning " + warningCount + ".");
						}
					} 
				}
			}
		} else if (btn.equals(loginBtn)) {
			if (passCount == passLength) {
				JOptionPane.showMessageDialog(loginBtn, "Successfully logged in.");
			} else {
				JOptionPane.showMessageDialog(loginBtn, "Incorrect password.");
			}
		}
	}

	
	
}
