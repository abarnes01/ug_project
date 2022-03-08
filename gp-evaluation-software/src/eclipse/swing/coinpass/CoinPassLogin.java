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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;

import eclipse.swing.InitialLogin;
import eclipse.swing.Method;

public class CoinPassLogin extends JFrame implements ActionListener {

	private static final long serialVersionUID = 7037648802373260841L;
	private JPanel contentPane, headerPanel, coinPanel, buttonPanel;
	private JLabel headerLabel;
	private JButton loginBtn, backBtn;
	private JPasswordField passEntryField;
	private String coinPass, passEntry;
	private ArrayList<String> coinPassElements;
	private ArrayList<Color> colourArray;
	private Map<String, ImageIcon> iconMap;
	private Map<String, Color> colourMap;
	private ArrayList<ImageIcon> iconArray;
	private ArrayList<Integer> numArray;

	public CoinPassLogin(String coinPass) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		coinPassElements = new ArrayList<String>();
		setCoinPass(coinPass);
		setCoinPassElements(coinPass);
		
		headerPanel = new JPanel();
		coinPanel = new JPanel();
		buttonPanel = new JPanel();
		
		headerLabel = new JLabel("Coin Password Login");
		loginBtn = new JButton("Login");
		loginBtn.addActionListener(this);
		backBtn = new JButton("<");
		backBtn.addActionListener(this);
		
		passEntry = "";
		
		makeCoins();
		coinPanel.setLayout(new GridLayout(0, 3, 20, 20));
		
		headerPanel.add(backBtn);
		headerPanel.add(headerLabel);
		
		buttonPanel.add(loginBtn);
		
		contentPane.add(headerPanel, BorderLayout.NORTH);
		contentPane.add(coinPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	
	public void makeCoins() {
		// TODO 10 j labels, each with 1 of 10 icons, 1 of 10 numbers, 1 of 10 colours
		
		System.out.println("Creating coins.");
		try {
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
				int randIcon = rand.nextInt(10-i);
				int randNum = rand.nextInt(10-i);
				int randCol = rand.nextInt(10-i);
				JLabel coin = new JLabel(iconArray.get(randIcon));
				coin.setText(Integer.toString(numArray.get(randNum)));
				Border border = BorderFactory.createLineBorder(colourArray.get(randCol), 5);
				coin.setForeground(colourArray.get(randCol));
				iconArray.remove(randIcon);
				numArray.remove(randNum);
				colourArray.remove(randCol);
				coin.setBorder(border);
				coin.setBackground(Color.LIGHT_GRAY);
				coin.setOpaque(true);
				coin.setSize(5, 5);
				coin.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						
						if (!coinPassElements.isEmpty()) {
							String str = coinPassElements.get(0);
							
							ImageIcon iconViewed = (ImageIcon)coin.getIcon();
							String numViewed = coin.getText();
							Color colViewed = coin.getForeground();
							
							if (str.contains(".png") && iconMap.get(str) == iconViewed) {
								passEntry += ":" + str;
								coinPassElements.remove(0);
							} else if (colourMap.containsKey(str) && colourMap.get(str) == colViewed) {
								passEntry += ":" + str;
								coinPassElements.remove(0);
							} else if (str.equals(numViewed)) {
								passEntry += ":" + str;
								coinPassElements.remove(0);
							} else {
								passEntry = "";
								coinPassElements = new ArrayList<String>();
								setCoinPassElements(coinPass);
								JOptionPane.showMessageDialog(coin, "Incorrect: Try again.");
							}

							System.out.println("Currently looking at element: " + str);
							System.out.println(passEntry);
						}
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
			if (passEntry.equals(getCoinPass())) {
				JOptionPane.showMessageDialog(loginBtn, "Successfully logged in.");
			}
			
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
