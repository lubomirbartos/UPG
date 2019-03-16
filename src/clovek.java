
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class clovek extends JPanel {
	
	double time = 0;
	
	public void setTime(double time) {
		this.time = time;
	}

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2587486132693225093L;

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Rectangle2D rect = new Rectangle2D.Double(-10, -10, 60, 20);

		g2.translate(400, 400);
		g2.rotate(-time);		
		
		g2.scale(2, 2);

		
		g2.draw(rect);
		
		g2.translate(40, 0);		
		g2.rotate(time);		
		g2.scale(0.9, 0.9);
		g2.draw(rect);
		
		g2.translate(40, 0);		
		g2.rotate(time);		
		g2.scale(0.9, 0.9);
		g2.draw(rect);
		
		g2.translate(40, 0);		
		g2.rotate(time);		
		g2.scale(0.5, 0.5);
		g2.draw(rect);


		g2.translate(40, 0);		

		AffineTransform transform = g2.getTransform();
		
		for (int i = 0; i < 5 ; i++) {
			g2.rotate(Math.toRadians(-i*25));	
			

			g2.draw(rect);
			
			g2.translate(60, 0);
			g2.rotate(Math.toRadians(i*25));
			g2.rotate(-2*time);		

			switch (i) {
			case 0:
				g2.drawString("Malicek", 0, 0);
				break;
			case 1:
				g2.drawString("Prstenicek", 0, 0);
				break;
			case 2:
				g2.drawString("Prostrednicek", 0, 0);
				break;
			case 3:
				g2.drawString("Ukazovacek", 0, 0);
				break;
			case 4:
				g2.drawString("Palec", 0, 0);
				break;
			}

			g2.setTransform(transform);

		}

		
		
	}
}
