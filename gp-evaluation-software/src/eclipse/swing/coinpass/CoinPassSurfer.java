package eclipse.swing.coinpass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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

	public CoinPassSurfer(int x, int y, int width) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setLocation(2*(x+width), y/2);
		setTitle("Coin Pass Shoulder Surfer");
		
		mainPane = new JPanel();
		bottomPane = new JPanel();
		
		passLenLbl = new JLabel("K length: ");
		passLength = 0;

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
		passLength = 0;
		passLenLbl.setText("K length: ");
		mainPane.removeAll();
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
