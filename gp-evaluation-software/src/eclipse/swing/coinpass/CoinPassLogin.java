package eclipse.swing.coinpass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;

import eclipse.swing.InitialLogin;
import eclipse.swing.Method;

public class CoinPassLogin extends JFrame implements ActionListener {

	private static final long serialVersionUID = 7037648802373260841L;
	private JPanel contentPane, headerPanel, coinPanel, formPanel, buttonPanel, mainPanel;
	private JLabel headerLabel, passEntryLabel;
	private JButton loginBtn, backBtn;
	private JPasswordField passEntryField;
	private String coinPass;
	private ArrayList<String> coinPassElements;
	private ArrayList<Color> colourArray;
	private Map<String, ImageIcon> iconMap;
	private Map<Color, String> colourMap;
	private ArrayList<ImageIcon> iconArray;
	private ArrayList<Integer> numArray;

	public CoinPassLogin(String coinPass) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		coinPassElements = new ArrayList<String>();
		setCoinPass(coinPass);
		setCoinPassElements(coinPass);
		
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
		passEntryField.setText(":");
		passEntryField.setEditable(false);
		
		makeCoins();
		coinPanel.setLayout(new GridLayout(0, 3));
		
		headerPanel.add(backBtn);
		headerPanel.add(headerLabel);
		
		formPanel.add(passEntryLabel);
		formPanel.add(passEntryField);
		
		mainPanel.add(coinPanel);
		mainPanel.add(formPanel);
		
		buttonPanel.add(loginBtn);
		
		contentPane.add(headerPanel, BorderLayout.NORTH);
		contentPane.add(mainPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	
	public void makeCoins() {
		// TODO 10 j labels, each with 1 of 10 icons, 1 of 10 numbers, 1 of 10 colours
		
		System.out.println("Creating coins.");
		try {
			colourMap = new HashMap<>();
			colourMap.put(Color.RED, "red");
			colourMap.put(Color.BLUE, "blue");
			colourMap.put(Color.PINK, "pink");
			colourMap.put(Color.WHITE, "white");
			colourMap.put(Color.GREEN, "green");
			colourMap.put(Color.YELLOW, "yellow");
			colourMap.put(Color.BLACK, "black");
			colourMap.put(Color.ORANGE, "orange");
			colourMap.put(Color.CYAN, "cyan");
			colourMap.put(Color.MAGENTA, "magenta");
			
			colourArray = new ArrayList<Color>( Arrays.asList(Color.RED, Color.BLUE, Color.PINK, Color.WHITE,
					Color.GREEN, Color.YELLOW, Color.BLACK, Color.ORANGE, Color.CYAN, Color.MAGENTA));
			
			iconMap = new HashMap<>();
			
			iconArray = new ArrayList<ImageIcon>();
			
			numArray = new ArrayList<Integer>( Arrays.asList(1,2,3,4,5,6,7,8,9,10));
			
			for (int i = 0; i < 10; i++) {
				BufferedImage img = ImageIO.read(new File("Icons/" + Integer.toString(i) + ".png"));
				ImageIcon imgIcon = new ImageIcon(img);
				iconArray.add(imgIcon);
				iconMap.put(Integer.toString(i) + ".png",imgIcon);
			}
			
			for (int i = 0; i < 10; i++) {
				Random rand = new Random();
				int randNum = rand.nextInt(10-i);
				JLabel coin = new JLabel(iconArray.get(randNum));
				coin.setText(Integer.toString(numArray.get(randNum)));
				Border border = BorderFactory.createLineBorder(colourArray.get(randNum), 5);
				coin.setForeground(colourArray.get(randNum));
				iconArray.remove(randNum);
				numArray.remove(randNum);
				colourArray.remove(randNum);
				coin.setBorder(border);
				
				coin.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						String str = coinPassElements.get(0);
						if (str.contains(".png") && iconMap.get(str) == (ImageIcon)coin.getIcon()) {
							passEntryField.setText(String.valueOf(passEntryField.getPassword()) + str + ":");
							coinPassElements.remove(0);
						} else if (StringUtils.isNumeric(str) && str == coin.getText()) {
							passEntryField.setText(String.valueOf(passEntryField.getPassword()) + str + ":");
							coinPassElements.remove(0);
						} else if (colourArray.contains(str) && str == colourMap.get(coin.getForeground())) {
							passEntryField.setText(String.valueOf(passEntryField.getPassword()) + str + ":");
							coinPassElements.remove(0);
						}
						System.out.println(String.valueOf(passEntryField.getPassword()));
						coinPanel.removeAll();
						coinPanel.revalidate();
						coinPanel.repaint();
						makeCoins();
					}
				});
				
				coinPanel.add(coin);
			}
		} catch (Exception e) {
			System.err.println("Error: Could not create coins.");
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		
		if (btn.equals(loginBtn)) {
			// TODO login checks whether pass field is same as entered stuff
			
		} else if (btn.equals(backBtn)) {
			new InitialLogin(Method.COIN).setVisible(true);
			dispose();
		}
	}
	
	public void setCoinPass(String input) {
		coinPass = input;
	}
	
	public String getCoinPass() {
		return coinPass;
	}
	
	public void setCoinPassElements(String input) {
		String[] coinPassSplit = input.split(":");
		for (int i = 1; i < coinPassSplit.length; i++) {
			coinPassElements.add(coinPassSplit[i]);
		}
	}
	
	public ArrayList<String> getCoinPassElements() {
		return coinPassElements;
	}

}
