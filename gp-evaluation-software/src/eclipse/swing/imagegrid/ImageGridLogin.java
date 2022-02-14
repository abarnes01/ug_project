package eclipse.swing.imagegrid;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ImageGridLogin extends JFrame implements ActionListener {

	private BufferedImage imageOne, imageTwo;
	private Integer gridSize;
	private JPanel contentPane, gridPanel, buttonPanel;
	private JButton loginButton;
	private GridLayout gridLayout;
	private ArrayList<BufferedImage> bufImages;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageGridLogin frame = new ImageGridLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ImageGridLogin() {
		// auto
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		gridPanel = new JPanel();
		buttonPanel = new JPanel();
		loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		
		contentPane.add(gridPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public void setImages(BufferedImage one, BufferedImage two) {
		imageOne = one;
		imageTwo = two;
	}
	
	public void setGridSize(Integer size) {
		gridSize = size;
	}
	
	public void makeGrid() {
		
		bufImages = new ArrayList<BufferedImage>();
		
		try {
			
			bufImages.add(imageOne);
			bufImages.add(imageTwo);
			
			// fill array with more random images
			for (int i = 0; i < (gridSize*gridSize)-2; i++) {
				URL url = new URL("https://picsum.photos/50");
				BufferedImage randImg = ImageIO.read(url.openStream());
				bufImages.add(randImg);
			}
			
			// print the whole grid with our two images randomly placed
			for (int i = 0; i < (gridSize*gridSize); i++) {
				Random rand = new Random();
				int randNum = rand.nextInt((gridSize*gridSize)-i);
				BufferedImage randImg = bufImages.get(randNum);
				JLabel imgLabel = new JLabel(new ImageIcon(randImg));
				gridPanel.add(imgLabel);
				randImg.flush();
				bufImages.remove(randNum);
			}
			
			gridLayout = new GridLayout(gridSize, gridSize);
			gridPanel.setLayout(gridLayout);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
