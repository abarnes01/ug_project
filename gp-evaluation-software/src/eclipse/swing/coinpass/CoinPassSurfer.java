package eclipse.swing.coinpass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class CoinPassSurfer extends JFrame implements ActionListener {

	private static final long serialVersionUID = 7491248350836673542L;
	private JPanel contentPane, mainPane, bottomPane;
	private JLabel shoulderSurferImg, passLenLbl;
	private JButton closeBtn;
	private Map<Color, String> colourToStrMap;
	private Integer passLength;
	private Integer errorCount;

	public CoinPassSurfer(int x, int y, int width) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setLocation((int)(2.5*(x+width)), (int)(y/2.5));
		setTitle("Coin Pass Shoulder Surfer");
		
		mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
		mainPane.add(Box.createRigidArea(new Dimension(0,5)));
		bottomPane = new JPanel();
		
		passLenLbl = new JLabel("K length: ");
		passLength = 0;
		errorCount = 0;

		closeBtn = new JButton("Close");
		closeBtn.addActionListener(this);
		
		mainPane.add(passLenLbl);
		bottomPane.add(closeBtn);
		
		contentPane.add(mainPane, BorderLayout.CENTER);
		contentPane.add(bottomPane, BorderLayout.SOUTH);
		
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
	
	public void updateSurfer(BufferedImage viewedImg, String viewedNum, Color viewedCol) {
		JLabel img = new JLabel(new ImageIcon(viewedImg));
		JLabel num = new JLabel(viewedNum);
		JLabel col = new JLabel(colourToStrMap.get(viewedCol));
		col.setForeground(viewedCol);
		JPanel viewedElements = new JPanel();
		viewedElements.add(img);
		viewedElements.add(num);
		viewedElements.add(col);
		passLength += 1;
		passLenLbl.setText("K length: " + Integer.toString(passLength));
		mainPane.add(viewedElements);
		mainPane.revalidate();
		mainPane.repaint();
	}
	
	public void restartSurfer() {
		// TODO other elements reset
		errorCount += 1;
		passLength = 0;
		passLenLbl.setText("K length: ");
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
				+ "<p>There are 3 elements for each coin, so for password of length k = 1, there are 3 combinations for the password." 
				+ " The password must be of length 3 at least however, and the number of combinations for this is 3^3 = 27.</p>"
				+ "<p>In this example, the password length is k = " + passLength + ". This means the number of combinations is 3^" + passLength + " = " + (int)Math.pow(3, passLength) + ".</p>"
				+ "<h3>Incorrect input</h3>"
				+ "<p>Multiple login attempts help to uncover the elements one by one of the password. Incorrect input, forcing the user to enter again, means the coins on display will be reshuffled."
				+ " This gives the shoulder surfer another chance to wittle down the elements being selected.</p>"
				+ "<p>In this example, you made " + errorCount + " errors.</p>";
		evaPane.setText(String.format(evaText));
		evaPane.setBackground(SystemColor.window);
		evaPane.setEditable(false);
		mainPane.add(evaPane);
		mainPane.revalidate();
		mainPane.repaint();
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
}
