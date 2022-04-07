package eclipse.swing.colourwheel;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import eclipse.sql.DatabaseRunner;
import eclipse.swing.InitialLogin;
import eclipse.swing.Method;
import eclipse.swing.Welcome;

public class ColourWheelLogin extends JFrame implements ActionListener {

	private static final long serialVersionUID = -6168526356154212099L;
	private DatabaseRunner dbRunner;
	private JPanel contentPane, formPanel;
	private JButton rotLftBtn, rotRgtBtn, loginBtn, entryBtn, backBtn;
	private int width, height;
	private String chosenCol;
	private List<Color> colList = new ArrayList<Color>( Arrays.asList(Color.RED, Color.BLUE, Color.PINK,
  		  Color.GREEN, Color.YELLOW, Color.ORANGE, Color.CYAN, Color.MAGENTA));
	private List<List<String>> charLists;
	private WheelCanvas wc;
	private String[] wpLetters;
	private int passCount;
	private int passLength;
	private int warningCount;
	
	private static String colWheelLoginResultHtml = "<html><h1>Colour Wheel Login (Successful Login)</h1>"
			+ "<p>If the shoulder surfer knows the algorithm, they would still first need to know the users selected colour, <br>"
			+ " and even then would need to brute force clicking confirm on the right selections of random text each time.</p>";
	private static String all64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789./";
	private static Map<String, Color> strToColMap;
	static {
		strToColMap = new HashMap<>();
		strToColMap.put("red", Color.RED);
		strToColMap.put("blue", Color.BLUE);
		strToColMap.put("pink", Color.PINK);
		strToColMap.put("white", Color.WHITE);
		strToColMap.put("green", Color.GREEN);
		strToColMap.put("yellow", Color.YELLOW);
		strToColMap.put("black", Color.BLACK);
		strToColMap.put("orange", Color.ORANGE);
		strToColMap.put("cyan", Color.CYAN);
		strToColMap.put("magenta", Color.MAGENTA);
	}

	public ColourWheelLogin(DatabaseRunner dbRunner, String chosenCol, String wheelPass) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		width = 450;
		height = 300;
		setBounds(100, 100, width+10, (height*2));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setTitle("Colour Wheel Login");
		this.dbRunner = dbRunner;
		
		this.chosenCol = chosenCol;
		wpLetters = wheelPass.split("");
		
		passCount = 0;
		passLength = wpLetters.length;
		warningCount = 0;
		
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
		backBtn = new JButton("\u2190");
		backBtn.addActionListener(this);
		rotLftBtn.addActionListener(this);
		rotRgtBtn.addActionListener(this);
		entryBtn = new JButton("Confirm");
		entryBtn.addActionListener(this);
		loginBtn = new JButton("Login");
		loginBtn.addActionListener(this);
		formPanel.add(backBtn);
		formPanel.add(rotLftBtn);
		formPanel.add(rotRgtBtn);
		formPanel.add(entryBtn);
		formPanel.add(loginBtn);
		
		Collections.shuffle(colList, new Random());
		
		charLists = new ArrayList<List<String>>();
		List<String> charsSplit = new ArrayList<String>(Arrays.asList(all64Chars.split("")));
		Collections.shuffle(charsSplit, new Random());
		
		for (int i = 0; i < 8; i++) {
			List<String> currentChars = new ArrayList<String>();
			currentChars.addAll(charsSplit.subList(8*i, 8*(i+1)));
			charLists.add(currentChars);
		}
		
		wc = new WheelCanvas(width, height, colList, charLists);
		contentPane.add(wc, BorderLayout.CENTER);
		contentPane.add(formPanel, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		
		if (btn.equals(rotLftBtn)) {
			Collections.rotate(colList, 1);
			wc.repaint();
		} else if (btn.equals(rotRgtBtn)) {
			Collections.rotate(colList, -1);
			wc.repaint();
		} else if (btn.equals(entryBtn)) {
			for (int i = 0; i < 8; i++) {
				if (strToColMap.get(chosenCol) == colList.get(i)) {
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
				JOptionPane.showMessageDialog(loginBtn, String.format(colWheelLoginResultHtml));
				new Welcome(dbRunner).setVisible(true);
				dispose();
			} else {
				JOptionPane.showMessageDialog(loginBtn, "Incorrect password.");
				new InitialLogin(dbRunner, Method.WHEEL).setVisible(true);
				dispose();
			}
		} else if (btn.equals(backBtn)) {
			new InitialLogin(dbRunner, Method.WHEEL).setVisible(true);
			dispose();
		}
	}
}
