package eclipse.swing.colourwheel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

public class ColourWheelLogin extends JFrame implements ActionListener {

	private JPanel contentPane, headerPanel, formPanel, loginPanel, botmPanel;
	private JLabel headerLbl, passLbl;
	private JButton rotLftBtn, rotRgtBtn, loginBtn, entryBtn;
	private JPasswordField passField;
	private int width, height;
	private String chosenCol, wheelPass;
	private List<Color> colourList;
	private List<List<String>> charLists;
	private WheelCanvas wc;
	private Map<String, Color> colourMap;
	private String[] wpLetters;

	public ColourWheelLogin(String chosenCol, String wheelPass) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		width = 450;
		height = 300;
		setBounds(100, 100, width+10, (height*2));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		this.chosenCol = chosenCol;
		this.wheelPass = wheelPass;
		wpLetters = wheelPass.split("");
		
		headerPanel = new JPanel();
		headerLbl = new JLabel("Colour Wheel");
		headerPanel.add(headerLbl);
		
		formPanel = new JPanel();
		rotLftBtn = new JButton("Rotate anticlockwise");
		rotLftBtn.addActionListener(this);
		rotRgtBtn = new JButton("Rotate clockwise");
		rotRgtBtn.addActionListener(this);
		entryBtn = new JButton("Confirm");
		entryBtn.addActionListener(this);
		formPanel.add(rotLftBtn);
		formPanel.add(rotRgtBtn);
		formPanel.add(entryBtn);
		
		loginPanel = new JPanel();
		loginBtn = new JButton("Login");
		
		botmPanel = new JPanel();
		
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
		
		// TODO
		wc = new WheelCanvas(width, height, colourList, charLists);
		contentPane.add(headerPanel, BorderLayout.NORTH);
		contentPane.add(wc, BorderLayout.CENTER);
		botmPanel.add(formPanel);
		botmPanel.add(loginPanel);
		contentPane.add(botmPanel, BorderLayout.SOUTH);
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
					for (int j = 0; j < 8; j++) {
						System.out.println(charLists.get(i).get(j));
						if (charLists.get(i).get(j).equals("m")) {
							System.out.println("test work");
						}
					}
				}
			}
		} else if (btn.equals(loginBtn)) {
			
		}
	}

	
	
}
