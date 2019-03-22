import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class HanziCaryMary {

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        // Vlastni graficky obsah
        DrawingPanelHanzi drawingPanel = new DrawingPanelHanzi();
        drawingPanel.setPreferredSize(new Dimension(640, 480));
        frame.add(drawingPanel);

        // Standardni manipulace s oknem
        frame.setTitle("Kresleni");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);    
        frame.setVisible(true);             
    }

}




class DrawingPanelHanzi extends JPanel{
	
	static double scale;
	static double xOffset;
	static double yOffset;
	@Override
	public void paintComponent(Graphics g) {
		double startX = 50.0,startY = 50.0, endX = 100.0 ,endY = 100.0;
		
		
		double  xScale = this.getWidth()/640.0;
		double  yScale = this.getHeight()/480.0;
		scale = xScale<yScale?xScale:yScale;
		xOffset = 0.0;
		yOffset = 0.0;
		if ( xScale < yScale) {
			yOffset = (this.getHeight()- 480*xScale)/2;
		}
		else xOffset = (this.getWidth()- 640*yScale)/2;
		
		
		if(g instanceof Graphics2D)
			System.out.println("Alles gut");
		
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		Line2D s = new Line2D.Double(10.5, 12, 15, 90.7);
		
		
//		g2.drawOval((int)(100*scale + xOffset), (int)(100*scale + yOffset), (int)(200*scale), (int)(200*scale));
		
		g2.drawRect((int)(0*scale + xOffset), (int)(0*scale + yOffset), (int)(639*scale), (int)(479*scale));
	
		s = new Line2D.Double(startX, startY, endX, endY);
		

	
//		drawArrow(50.0,50.0,100.0,100.0,30,g2);
//		drawArrow(150.0,150.0,200.0,200.0,30,g2);
//		drawArrow(10.0,100.0,100.0,10.0,10.0,g2);
//		drawArrow(300.0,200.0,0.0,0.0,100.0,g2);
	}
	
	public static int d2wx(double dx) {
		return (int)(dx*scale + xOffset);
		
	}
	
	public static int d2wy(double dy) {
		
		return (int)(dy*scale + yOffset);
	}
	
	void drawArrow(double Ax,double Ay, double Bx, double By, double delka, Graphics2D g2) {
		double Vx = Ax - Bx;
		double Vy = Ay - By;
		
		double d = Math.sqrt(Vx*Vx + Vy*Vy);
		double V2x = Vx * delka/d;
		double V2y = Vy * delka/d;
		double Qx = Bx + V2x;
		double Qy = By + V2y;
		double V3x = V2y;
		double V3y = -V2x;
		double Cx = Qx + V3x;
		double Cy = Qy + V3y;
		double Dx = Qx - V3x;
		double Dy = Qy - V3y;
		
		g2.drawLine(d2wx(Ax), d2wy(Ay), d2wx(Bx), d2wy(By));
		g2.drawLine(d2wx(Cx), d2wy(Cy), d2wx(Bx), d2wy(By));
		g2.drawLine(d2wx(Dx), d2wy(Dy), d2wx(Bx), d2wy(By));
		
		Qx = Ax + V2x;
		Qy = Ay + V2y;
		Cx = Qx + V3x;
		Cy = Qy + V3y;
		Dx = Qx - V3x;
		Dy = Qy - V3y;
		
		g2.drawLine(d2wx(Cx), d2wy(Cy), d2wx(Ax), d2wy(Ay));
		g2.drawLine(d2wx(Dx), d2wy(Dy), d2wx(Ax), d2wy(Ay));
		g2.drawLine(d2wx(Dx), d2wy(Dy), d2wx(Cx), d2wy(Cy));
	}
	
}

