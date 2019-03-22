
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class DrawingPanel1 extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2587486133693225093L;

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		Rectangle2D rect = new Rectangle2D.Double(0, 0, 100, 100);
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.draw(rect);
		
		AffineTransform old_transform = g2.getTransform();
		
		g2.translate(200, 100);
		
		g2.draw(rect);
		
		g2.rotate(Math.toRadians(45));
		
		g2.draw(rect);
		
		g2.translate(100, 100);
		
		g2.draw(rect);

		g2.setTransform(old_transform);
		
		g2.setColor(Color.RED);
		
		g2.fill(rect);
		
		g2.scale(5, 5);

		g2.draw(rect);
		
		AffineTransform current_transform = g2.getTransform();
		
		g2.setTransform(old_transform);
		
		g2.setColor(Color.GREEN);
				
		Shape rect2 = current_transform.createTransformedShape(rect);

		g2.draw(rect2);
	}
}
