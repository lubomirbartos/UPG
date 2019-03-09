import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import TrafficSim.Car;

public class Cars extends JComponent {
	
	public static final double ORIENTATION_EAST = 0.0;
	public static final double ORIENTATION_NORTH = -1.5707963267948966;
	public static final double ORIENTATION_WEST = 3.141592653589793;
	public static final double ORIENTATION_SOUTH = 1.5707963267948966;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4978879806979781151L;
	Car[] cars;
	
	public Cars(Car[] cars){
		this.cars = cars;
	    setDoubleBuffered(true);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    for(Car car : cars) {
	    	drawCar(car, g2d);
	    }	
	}


	private void drawCar(Car car, Graphics2D g2d) {
//		double currentSpeed = car.getCurrentSpeed();
		int defaultCarSize = 2;
		double orientation = car.getOrientation();
		double length = car.getLength();
		Point2D position = car.getPosition();
		g2d.setColor(Color.BLACK);
		System.out.println(orientation);
		
		// TODO car orientation

		if (orientation == ORIENTATION_NORTH || orientation == ORIENTATION_SOUTH) {
			g2d.drawRect(
					View.x(position.getX()), 
					View.y(position.getY()), 
					View.x(defaultCarSize), 
					View.y(length)
					);	
		}
		else if (orientation == ORIENTATION_EAST || orientation == ORIENTATION_WEST) {
//			g2d.drawRect((int) (position.getX() * transform_x), (int) (position.getY() * transform_y), (int) (length * transform_x), (int) (defaultCarSize * transform_y));
			g2d.drawRect(
					View.x(position.getX()), 
					View.y(position.getY()), 
					View.x(length),
					View.y(defaultCarSize) 
					);	

		}
		
//        g2d.setTransform(old);



	}

	public void update(Car[] cars) {
		this.cars = cars;
		
	}
	



}
