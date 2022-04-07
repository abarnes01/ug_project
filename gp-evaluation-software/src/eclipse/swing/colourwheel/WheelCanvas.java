package eclipse.swing.colourwheel;

import java.awt.*;
import java.awt.geom.*;
import java.util.List;

import javax.swing.*;

public class WheelCanvas extends JComponent {
	
	private static final long serialVersionUID = 3344140439295479481L;
	private int width;
	private List<Color> colourList;
	private List<List<String>> charLists;
	
	public WheelCanvas(int w, List<Color> cols, List<List<String>> chls) {
		width = w;
		colourList = cols;
		charLists = chls;
	}

	protected void paintComponent(Graphics g) {
		// g2d more functionality 
		Graphics2D g2d = (Graphics2D) g;
		
		// smoothen graphic
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		for (int i = 0; i < 8; i++) {
			drawCircle(g2d, i, colourList.get(i), charLists.get(i));
		}
	}
	
	private void drawCircle(Graphics2D g2d, int i, Color colour, List<String> chars) {
		double x = 0;
		double y = 0;
		double w = width - 2;
		double h = w;
		double startAng = i * 360.0 / 8.0;
		double endAng = 360.0 / 8.0;
		
		Rectangle2D.Double rec = new Rectangle2D.Double(x, y, w, h);
		int type = Arc2D.PIE;
		Arc2D arc = new Arc2D.Double(x,y,w,h,startAng,endAng,type);
		
		AffineTransform temp = g2d.getTransform();
		g2d.setColor(colour);
		g2d.fill(arc);
		g2d.setColor(Color.black);
		g2d.draw(arc);
		g2d.translate(rec.getCenterX(), rec.getCenterY());
		g2d.rotate(-Math.toRadians(startAng));
		g2d.translate(90, -50);
		
		// turn around if upside down
		if (-startAng <= -135.0 && -startAng >= -225.0) {
			g2d.rotate(Math.toRadians(180),0,0);
			g2d.translate(-100, 0);
		}
		
		// if section is blue turn text white
		if (colour.equals(Color.BLUE)) {
			g2d.setColor(Color.WHITE);
		}
		
		// draw out each letter
		for (int j = 0; j < 8; j++) {
			g2d.drawString(chars.get(j), 0, 0);
			g2d.translate(13, 0);
		}
		g2d.setTransform(temp);
	}
	
	
}
