
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel {
	
	double transform = 1;
	double offSetX = 0;
	double offSetY = 0;


	@Override
	public void paintComponent(Graphics g) {
		
		if (g instanceof Graphics2D) { // can be offspring of Graphics2D
			System.out.println("Je to dobre");
		}

		double transformX = this.getWidth()/640.0;
		double transformY = this.getHeight()/480.0;
		

		if (transformX < transformY) {
			transform = transformX;
			offSetY = (this.getHeight() - 480 * transform)/2;
		} else {
			transform = transformY;
			offSetX = (this.getWidth() - 640 * transform)/2;
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawUkol(this.getWidth(), this.getHeight(), g2d);
//		g2d.drawOval(d2wx(100), d2wy(100), (int) (200 * transform), (int) (200 * transform));
//		g2d.drawRect(d2wx(0), d2wy(0), (int) (639 * transform), (int) (479 * transform));
//		g2d.drawRect(d2wx(100), d2wy(100), (int) (440 * transform), (int) (280 * transform));
		
		/*
		 * kreslime sipku
		 * potrebujem jednotkovy vektor, vektor, delku vektoru, kolmost 
		 */

//		Shape [] shapes = new Shape[5];
//		Line2D line = new Line2D.Double(10.5, 12, 15, 90.7);
//		Stroke stroke = new BasicStroke(5);
//		g2d.setStroke(stroke);
//		shapes[0] = line;
//		
//		for (Shape shape : shapes) {
//			if (shape != null) {
//				g2d.draw(shape);				
//			}			
//		}		
	}
	
	void drawArrow(double Ax, double Ay, double Bx, double By, double s, Graphics2D g2d) {
		double Vx = Ax - Bx;
		double Vy = Ay - By;
		double d = Math.sqrt(Vx * Vx + Vy * Vy);
		double V2x = Vx * s / d;
		double V2y = Vy * s / d;
		double Qx = Bx + V2x;
		double Qy = By + V2y;
		double V3x = V2y * 0.5;
		double V3y = -V2x * 0.5;
		double Cx = Qx + V3x;
		double Cy = Qy + V3y;
		double Dx = Qx - V3x;
		double Dy = Qy - V3y;
		g2d.setColor(Color.BLACK);
		g2d.drawLine(d2wx(Ax), d2wy(Ay), d2wx(Bx), d2wy(By));
		g2d.drawLine(d2wx(Cx), d2wy(Cy), d2wx(Bx), d2wy(By));
		g2d.drawLine(d2wx(Dx), d2wy(Dy), d2wx(Bx), d2wy(By));
		
		
		double Q2x = Ax + V2x;
		double Q2y = Ay + V2y;
		double C2x = Q2x + V3x;
		double C2y = Q2y + V3y;
		double D2x = Q2x - V3x;
		double D2y = Q2y - V3y;
		g2d.drawLine(d2wx(C2x), d2wy(C2y), d2wx(Ax), d2wy(Ay));
		g2d.drawLine(d2wx(D2x), d2wy(D2y), d2wx(Ax), d2wy(Ay));
		g2d.drawLine(d2wx(D2x), d2wy(D2y), d2wx(C2x), d2wy(C2y));

		
	}
	
	void drawUkol(int w, int h, Graphics2D g2d) {
		drawArrow(100, 100,  w - 100, h - 100, 30, g2d);
		drawArrow(100, h - 100, w - 100, 100, 30, g2d);

	
	}

	
	
	
	public int d2wx(double dx) {
		return (int) (dx * transform + offSetX);
	}
	public int d2wy(double dy) { // design to window y
		return (int) (dy * transform + offSetY);
	}
}
