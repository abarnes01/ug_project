package eclipse.swing.coinpass;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class CoinCanvas extends JComponent {

	private static final long serialVersionUID = 950319645344773905L;
	private int width, height;
	private BufferedImage icon;
	private Color col;
	private Integer num;
	private AffineTransform at;
	
	public CoinCanvas(int width, int height, BufferedImage icon, Color col, Integer num) {
		this.width = width;
		this.height = height;
		this.icon = icon;
		this.col = col;
		this.num = num;
	}
	
	protected void paintComponent(Graphics g) {
		// g2d more functionality 
		Graphics2D g2d = (Graphics2D) g;
		
		// smoothen graphic
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// keep hold of original transform
		at = g2d.getTransform();
		
		Ellipse2D.Double circle = new Ellipse2D.Double(width, height, 80, 80);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fill(circle);
		g2d.setColor(col);
		g2d.draw(circle);
		g2d.drawImage(icon, (int)circle.getCenterX()-15, (int)circle.getCenterY()-17, (int)(width*1.5), (int)(height*1.5), null);
		g2d.drawString(Integer.toString(num), (int)circle.getCenterX()-5, (int)circle.getCenterY()+25);
	}
	
}
