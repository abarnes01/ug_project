package eclipse.swing.imagegrid;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ImageGridLogin extends JFrame implements MouseListener {

	private BufferedImage imageOne, imageTwo;
	private Integer gridSize;
	private JPanel contentPane, gridPanel;
	private GridLayout gridLayout;
	private ArrayList<BufferedImage> bufImages;
	private JLabel[][] imageList;
	private PassImage P1, P2, I1, I2;

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
		
		contentPane.add(gridPanel, BorderLayout.CENTER);
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
			
			Integer gridSizeSqr = gridSize*gridSize;
			
			// fill array with more random images
			for (int i = 0; i < gridSizeSqr-2; i++) {
				URL url = new URL("https://picsum.photos/50");
				BufferedImage randImg = ImageIO.read(url.openStream());
				bufImages.add(randImg);
			}
			
			imageList = new JLabel[gridSize][gridSize];
			
			boolean gotImgOne = false;
			boolean gotImgTwo = false;
			
			// print the whole grid with our two images randomly placed
			for (int i = 0; i < gridSizeSqr; i++) {
				int x = i % gridSize;
				int y = i / gridSize;
				Random rand = new Random();
				int randNum = rand.nextInt(gridSizeSqr-i);
				BufferedImage randImg = bufImages.get(randNum);
				if (randImg == imageOne) {
					I1 = new PassImage(x, y);
				} else if (randImg == imageTwo) {
					I2 = new PassImage(x, y);
				}
				JLabel imgLabel = new JLabel(new ImageIcon(randImg));
				imgLabel.addMouseListener(this);
				imageList[x][y] = imgLabel;
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
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		JLabel label = (JLabel)e.getSource();
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				// get this label position
				if (imageList[x][y] == label) {
					
					// horizontal line
					if (I1.x == I2.x) {
						
						// at edge
						if (I1.y+1 == gridSize) {
							P1 = new PassImage(I1.x, 0);
							P2 = new PassImage(I2.x, I2.y+1);
						} else if (I2.y+1 == gridSize) {
							P1 = new PassImage(I1.x, I1.y+1);
							P2 = new PassImage(I2.x, 0);
						} else {
							P1 = new PassImage(I1.x, I1.y+1);
							P2 = new PassImage(I2.x, I2.y+1);
						}
						
					}
					// vertical line
					else if (I1.y == I2.y) {
						
						// at edge
						if (I1.x+1 == gridSize) {
							P1 = new PassImage(0, I1.y);
							P2 = new PassImage(I2.x+1, I2.y);
						} else if (I2.x+1 == gridSize) {
							P1 = new PassImage(I1.x+1, I1.y);
							P2 = new PassImage(0, I2.y);
						} else {
							P1 = new PassImage(I1.x+1, I1.y);
							P2 = new PassImage(I2.x+1, I2.y);
						}
						
					}
					// different x y coordinates
					else {
						P1 = new PassImage(I1.x, I2.y);
						P2 = new PassImage(I2.x, I1.y);
					}
					
//					System.out.println("I1.x: " + I1.x + " y: " + I1.y);
//					System.out.println("I2.x: " + I2.x + " y: " + I2.y);
//					System.out.println("P1.x: " + P1.x + " y: " + P1.y);
//					System.out.println("P2.x: " + P2.x + " y: " + P2.y);
					
					// check if this image is a pass image
					if ((x == P1.x) && (y == P1.y)) {
						JOptionPane.showMessageDialog(label, "Correct pass image one");
					} else if ((x == P2.x) && (y == P2.y)) {
						JOptionPane.showMessageDialog(label, "Correct pass image two");
					}
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
