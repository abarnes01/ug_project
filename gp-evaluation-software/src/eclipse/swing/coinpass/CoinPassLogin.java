package eclipse.swing.coinpass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import eclipse.sql.DatabaseRunner;
import eclipse.swing.InitialLogin;
import eclipse.swing.Method;
import eclipse.swing.Welcome;

public class CoinPassLogin extends JFrame implements ActionListener {

	private static final long serialVersionUID = 7037648802373260841L;
	private DatabaseRunner dbRunner;
	private JPanel contentPane, coinPanel, buttonPanel;
	private JButton loginBtn, backBtn;
	private String coinPass, passEntry;
	private ArrayList<String> coinPassElements;
	private ArrayList<Color> colArr;
	private Map<String, BufferedImage> iconMap;
	private ArrayList<BufferedImage> iconArr;
	private ArrayList<Integer> numArr;
	private CoinPassSurfer surfer;

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
	
	public CoinPassLogin(DatabaseRunner dbRunner, String coinPass) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 340, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setTitle("Coin Pass Login");
		this.dbRunner = dbRunner;

		coinPassElements = new ArrayList<String>();
		setCoinPass(coinPass);
		setCoinPassElements(coinPass);
		
		coinPanel = new JPanel();
		buttonPanel = new JPanel();
		loginBtn = new JButton("Login");
		loginBtn.addActionListener(this);
		backBtn = new JButton("\u2190");
		backBtn.addActionListener(this);
		
		passEntry = "";
		
		makeCoins();
		coinPanel.setLayout(new GridLayout(0, 3, 0, 0));
		
		buttonPanel.add(backBtn);
		buttonPanel.add(loginBtn);
		
		contentPane.add(coinPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		surfer = new CoinPassSurfer();
		surfer.setVisible(true);
	}
	
	
	public void makeCoins() {
		System.out.println("Creating coins...");
		try {
			
			colArr = new ArrayList<Color>( Arrays.asList(Color.RED, Color.BLUE, Color.PINK, Color.WHITE,
					Color.GREEN, Color.YELLOW, Color.BLACK, Color.ORANGE, Color.CYAN, Color.MAGENTA));
			
			iconMap = new HashMap<>();
			
			iconArr = new ArrayList<BufferedImage>();
			
			numArr = new ArrayList<Integer>( Arrays.asList(1,2,3,4,5,6,7,8,9,10));
			
			for (int i = 0; i < 10; i++) {
				BufferedImage img = ImageIO.read(CoinPassLogin.class.getResource("/Icons/" + Integer.toString(i) + ".png"));
				iconArr.add(img);
				iconMap.put(Integer.toString(i) + ".png",img);
			}
			
			for (int i = 0; i < 10; i++) {
				Random rand = new Random();
				int randIcon = rand.nextInt(10-i);
				int randNum = rand.nextInt(10-i);
				int randCol = rand.nextInt(10-i);
				CoinCanvas coin = new CoinCanvas(20,20,iconArr.get(randIcon),colArr.get(randCol),numArr.get(randNum));
				BufferedImage iconViewed = iconArr.get(randIcon);
				String numViewed = Integer.toString(numArr.get(randNum));
				Color colViewed = colArr.get(randCol);
				coin.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						
						if (!coinPassElements.isEmpty()) {
							String str = coinPassElements.get(0);
							
							if (str.contains(".png") && iconMap.get(str) == iconViewed) {
								passEntry += ":" + str;
								coinPassElements.remove(0);
								surfer.updateSurfer(iconViewed, numViewed, colViewed);
							} else if (strToColMap.containsKey(str) && strToColMap.get(str) == colViewed) {
								passEntry += ":" + str;
								coinPassElements.remove(0);
								surfer.updateSurfer(iconViewed, numViewed, colViewed);
							} else if (str.equals(numViewed)) {
								passEntry += ":" + str;
								coinPassElements.remove(0);
								surfer.updateSurfer(iconViewed, numViewed, colViewed);
							} else {
								passEntry = "";
								coinPassElements = new ArrayList<String>();
								setCoinPassElements(coinPass);
								JOptionPane.showMessageDialog(coin, "Incorrect: Try again.");
								surfer.restartSurfer();
							}
						}
						coinPanel.removeAll();
						coinPanel.revalidate();
						coinPanel.repaint();
						makeCoins();
					}
				});
				iconArr.remove(randIcon);
				numArr.remove(randNum);
				colArr.remove(randCol);
				coinPanel.add(coin);
			}
			System.out.println("Coins created.");
		} catch (Exception e) {
			System.err.println("Error: Could not create coins.");
			e.printStackTrace();
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		
		if (btn.equals(loginBtn)) {
			if (passEntry.equals(getCoinPass())) {
				JOptionPane.showMessageDialog(loginBtn, "Successfully logged in.");
				surfer.surferEvaluate();
				new Welcome(dbRunner).setVisible(true);
				dispose();
			}
			
		} else if (btn.equals(backBtn)) {
			new InitialLogin(dbRunner, Method.COIN).setVisible(true);
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
