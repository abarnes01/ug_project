package eclipse.swing.imagegrid;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;

import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import eclipse.swing.Welcome;

public class ImageGridLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private BufferedImage imageOne, imageTwo;
	private Integer gridSize;
	private JPanel contentPane, gridPanel;
	private GridLayout gridLayout;
	private ArrayList<BufferedImage> bufImages;
	private JLabel[][] labelList;
	private BufferedImage[][] imageList;
	private ArrayList<BufferedImage> possibleImages;
	private PassImage P1, P2, I1, I2;
	//private long startTime;

	public ImageGridLogin(Integer gs, BufferedImage iO, BufferedImage iT) {
		setGridSize(gs);
		setImages(iO, iT);
		possibleImages = new ArrayList<BufferedImage>();
		
		// auto
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// create initial grid
		gridPanel = new JPanel();
		contentPane.add(gridPanel, BorderLayout.CENTER);
		//startTime = System.nanoTime();
		makeGrid();
	}
	
	public void setImages(BufferedImage one, BufferedImage two) {
		imageOne = one;
		imageTwo = two;
	}
	
	public void setGridSize(Integer size) {
		gridSize = size;
	}
	
	public BufferedImage getImageOne() {
		return imageOne;
	}
	
	public BufferedImage getImageTwo() {
		return imageTwo;
	}
	
	public Integer getGridSize() {
		return gridSize;
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
	
	public void makeGrid() {
		System.out.println("Making grid...");
		
		bufImages = new ArrayList<BufferedImage>();
		
		try {
			
			// add our two images to an array of random images. Save these coordinates
			// display all images randomly in JLabels. Add a clicker
			
			bufImages.add(imageOne);
			bufImages.add(imageTwo);
			
			Integer gridSizeSqr = gridSize*gridSize;
			
			// fill array with more random images
			for (int i = 0; i < gridSizeSqr-2; i++) {
				URL url = new URL("https://picsum.photos/50");
				BufferedImage randImg = ImageIO.read(url.openStream());
				for (int j = 0; j < bufImages.size(); j++) {					
					while (buffImgsEqual(bufImages.get(j), randImg)) {
						URL temp = new URL("https://picsum.photos/50");
						randImg = ImageIO.read(temp.openStream());
						System.err.println("Duplicate");
					}
				}
				bufImages.add(randImg);
			}
			
			labelList = new JLabel[gridSize][gridSize];
			imageList = new BufferedImage[gridSize][gridSize];
			
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
				imgLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						Boolean recreate = true;
						JLabel label = (JLabel)e.getSource();
						for (int x = 0; x < gridSize; x++) {
							for (int y = 0; y < gridSize; y++) {
								// get this label position
								if (labelList[x][y] == label) {
									
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
									
//									System.out.println("I1.x: " + I1.x + " y: " + I1.y);
//									System.out.println("I2.x: " + I2.x + " y: " + I2.y);
//									System.out.println("P1.x: " + P1.x + " y: " + P1.y);
//									System.out.println("P2.x: " + P2.x + " y: " + P2.y);
									
									// check if this image is a pass image
									if ((x == P1.x) && (y == P1.y)) {
										
										if (possibleImages.isEmpty()) {
											for (int i = 0; i < gridSize; i++) {
												if (labelList[i][P1.y] != label) {
													possibleImages.add(imageList[i][P1.y]);
													System.out.println("added x: " + i + " y: " + P1.y);
												}
												if (labelList[P1.x][i] != label) {
													possibleImages.add(imageList[P1.x][i]);
													System.out.println("added x: " + P1.x + " y: " + i);
												}
											}
										} else {
											
											ArrayList<BufferedImage> currentImages = new ArrayList<BufferedImage>();
											for (int i = 0; i < gridSize; i++) {
												if (labelList[i][P1.y] != label) {
													currentImages.add(imageList[i][P1.y]);
												}
												if (labelList[P1.x][i] != label) {
													currentImages.add(imageList[P1.x][i]);
												}
											}
											
											ArrayList<BufferedImage> matches = new ArrayList<BufferedImage>();
											for (int i = 0; i < currentImages.size(); i++) {
												for (int j = 0; j < possibleImages.size(); j++) {
													if (buffImgsEqual(currentImages.get(i),possibleImages.get(j))) {
														matches.add(possibleImages.get(j));
														//continue;
													}
												}
											}
											possibleImages.clear();
											possibleImages = matches;
											if (possibleImages.size() <= 2) {
												System.out.println("Shoulder surfer found images.");
												Welcome welcome = new Welcome();
												welcome.setVisible(true);
												recreate = false;
												dispose();
											}
										}
										
										gridPanel.removeAll();
										gridPanel.revalidate();
										gridPanel.repaint();
										contentPane.removeAll();
										contentPane.revalidate();
										contentPane.repaint();
										
										if (recreate) {
											makeGrid();											
										}
										contentPane.add(gridPanel, BorderLayout.CENTER);
										
										
										System.out.println(possibleImages.size());
										

									} else if ((x == P2.x) && (y == P2.y)) {
										if (possibleImages.isEmpty()) {
											for (int i = 0; i < gridSize; i++) {
												if (labelList[i][P2.y] != label) {
													possibleImages.add(imageList[i][P2.y]);
													System.out.println("added x: " + i + " y: " + P2.y);
												}
												if (labelList[P2.x][i] != label) {
													possibleImages.add(imageList[P2.x][i]);
													System.out.println("added x: " + P2.x + " y: " + i);
												}
											}
										} else {
											
											ArrayList<BufferedImage> currentImages = new ArrayList<BufferedImage>();
											for (int i = 0; i < gridSize; i++) {
												if (labelList[i][P2.y] != label) {
													currentImages.add(imageList[i][P2.y]);
												}
												if (labelList[P2.x][i] != label) {
													currentImages.add(imageList[P2.x][i]);
												}
											}
											
											ArrayList<BufferedImage> matches = new ArrayList<BufferedImage>();
											for (int i = 0; i < currentImages.size(); i++) {
												for (int j = 0; j < possibleImages.size(); j++) {
													if (buffImgsEqual(currentImages.get(i),possibleImages.get(j))) {
														matches.add(possibleImages.get(j));
														//continue;
													}
												}
											}
											possibleImages.clear();
											possibleImages = matches;
											if (possibleImages.size() <= 2) {
												System.out.println("Found our images");
												Welcome welcome = new Welcome();
												welcome.setVisible(true);
												recreate = false;
												dispose();
											}
										}
										
										gridPanel.removeAll();
										gridPanel.revalidate();
										gridPanel.repaint();
										contentPane.removeAll();
										contentPane.revalidate();
										contentPane.repaint();
										if (recreate) {
											makeGrid();											
										}
										contentPane.add(gridPanel, BorderLayout.CENTER);
										System.out.println(possibleImages.size());
									}
								}
							}
						}
					}
				});
				
				labelList[x][y] = imgLabel;
				imageList[x][y] = randImg;
				
				gridPanel.add(imgLabel);
				randImg.flush();
				bufImages.remove(randNum);
			}
			
			gridLayout = new GridLayout(gridSize, gridSize);
			gridPanel.setLayout(gridLayout);
			System.out.println("Grid made.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
