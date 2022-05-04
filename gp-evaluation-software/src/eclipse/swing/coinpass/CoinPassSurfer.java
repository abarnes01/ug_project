package eclipse.swing.coinpass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class CoinPassSurfer extends JFrame implements ActionListener {

	class Coin {
		
		BufferedImage viewedImg;
		String viewedNum;
		Color viewedCol;
		
		public Coin(BufferedImage viewedImg, String viewedNum, Color viewedCol) {
			this.viewedImg = viewedImg;
			this.viewedNum = viewedNum;
			this.viewedCol = viewedCol;
		}
	}
	
	private static final long serialVersionUID = 7491248350836673542L;
	private JPanel contentPane, mainPane, bottomPane;
	private JButton closeBtn;
	private Integer k;
	private Integer errorCount;
	private ArrayList<Coin> viewedCoins = new ArrayList<Coin>();
	
	private static Map<Color, String> colourToStrMap;
	static {
		colourToStrMap = new HashMap<>();
		colourToStrMap.put(Color.RED, "red");
		colourToStrMap.put(Color.BLUE, "blue");
		colourToStrMap.put(Color.PINK, "pink");
		colourToStrMap.put(Color.WHITE, "white");
		colourToStrMap.put(Color.GREEN, "green");
		colourToStrMap.put(Color.YELLOW, "yellow");
		colourToStrMap.put(Color.BLACK, "black");
		colourToStrMap.put(Color.ORANGE, "orange");
		colourToStrMap.put(Color.CYAN, "cyan");
		colourToStrMap.put(Color.MAGENTA, "magenta");
	}

	public CoinPassSurfer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setLocation(800, 100);
		setTitle("Coin Pass Shoulder Surfer");
		
		mainPane = new JPanel();
		bottomPane = new JPanel();
		
		errorCount = 0;
		k = 0;

		closeBtn = new JButton("Close");
		closeBtn.addActionListener(this);
		
		bottomPane.add(closeBtn);
		try {
			bottomPane.add(generateSurferImg());
		} catch (Exception e) {
			System.err.println("Error: Could not retrieve surfer image.");
		}
		
		contentPane.add(mainPane, BorderLayout.CENTER);
		contentPane.add(bottomPane, BorderLayout.SOUTH);
	}
	
	public void updateSurfer(BufferedImage viewedImg, String viewedNum, Color viewedCol) {
		JPanel viewedElements = new JPanel();
		Coin coin = new Coin(viewedImg, viewedNum, viewedCol);
		// compare current coin at this index with the currently viewed elements
		// remove any non-repeating elements through use of try catch clauses
		try {
			coin = viewedCoins.get(k);
			try {
				if (!buffImgsEqual(coin.viewedImg, viewedImg)) {
					coin.viewedImg = null;
					System.out.println("ping1");
				}
			} catch (NullPointerException e) {}
			
			try {
				if (!(coin.viewedNum.equals(viewedNum))) {
					coin.viewedNum = null;
				} 
			} catch (NullPointerException e){}
			
			try {
				if (!(coin.viewedCol.equals(viewedCol))) {
					coin.viewedCol = null;
				}
			} catch (NullPointerException e) {}
			
		} catch (IndexOutOfBoundsException e) {
			viewedCoins.add(coin);
		}
		
		try {
			JLabel img = new JLabel(new ImageIcon(coin.viewedImg));
			viewedElements.add(img);
		} catch (Exception e) {
			
		}
		try {
			JLabel num = new JLabel(coin.viewedNum);
			viewedElements.add(num);
		} catch (Exception e) {
			
		}
		try {
			JLabel col = new JLabel(colourToStrMap.get(coin.viewedCol));
			col.setForeground(coin.viewedCol);
			viewedElements.add(col);
		} catch (Exception e) {
			
		}
		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
		viewedElements.setBorder(border);
		viewedElements.setBackground(Color.LIGHT_GRAY);
		k += 1;
		mainPane.add(viewedElements);
		mainPane.revalidate();
		mainPane.repaint();
	}
	
	public void restartSurfer() {
		errorCount += 1;
		k = 0;
		mainPane.removeAll();
		mainPane.revalidate();
		mainPane.repaint();
	}
	
	public void surferEvaluate() {
		mainPane.removeAll();
		JEditorPane evaPane = new JEditorPane();
		evaPane.setContentType("text/html");
		String evaText = "<html><h2>What the shoulder surfer can gather</h2>"
				+ "<p>Whether they are aware of the algorithm or not, they can see which coin you are pressing and the elements involved.</p>"
				+ "<p>There are 3 elements for each coin, and 10 inputs for each element, so for password of length k = 1, there are 30 combinations for the password."
				+ "In this example, the password length is k = " + k + ". This means the number of combinations is (10*3)^" + k + " = " + (int)Math.pow(10*3, k) + ".</p>"
				+ "<h3>Incorrect input</h3>"
				+ "<p>Multiple login attempts help to uncover the elements one by one of the password. Incorrect input, forcing the user to enter again, means the coins on display will be reshuffled."
				+ " This gives the shoulder surfer another chance to wittle down the elements being selected.</p>"
				+ "<p>In this example, you made " + errorCount + " errors.</p>";
		evaPane.setText(String.format(evaText));
		evaPane.setBackground(SystemColor.window);
		evaPane.setEditable(false);
		mainPane.add(evaPane);
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
		mainPane.add(Box.createRigidArea(new Dimension(0,5)));
		mainPane.revalidate();
		mainPane.repaint();
	}
	
	public JLabel generateSurferImg() throws Exception {
		BufferedImage shoSurBI = ImageIO.read(CoinPassSurfer.class.getResource("/Images/shouldersurfer.png"));
		Image img = shoSurBI.getScaledInstance(100, 45, java.awt.Image.SCALE_SMOOTH);
		JLabel shoulderSurferLbl = new JLabel(new ImageIcon(img));
		return shoulderSurferLbl;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton)e.getSource();
		if (btn.equals(closeBtn)) {
			int ans = JOptionPane.showConfirmDialog(closeBtn, "Do you wish to close the shoulder surfer?");
			if (ans == JOptionPane.YES_OPTION) {
				dispose();
			}
		}
	}
	
	public Boolean buffImgsEqual(BufferedImage a, BufferedImage b) {
		for (int x = 0; x < a.getWidth(); x++) {
			for (int y = 0; y < a.getHeight(); y++) {
				if (a.getRGB(x,y) != b.getRGB(x, y)) {
					return false;
				}
			}
		}
		return true;
	}
}
