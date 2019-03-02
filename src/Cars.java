import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import TrafficSim.Car;

public class Cars implements Drawable {
	
	Car[] cars;
	
	public Cars(Car[] cars){
		this.cars = cars;
	}

	@Override
	public void draw(double transform_x, double transform_y, Graphics2D g2d) {
	    for(Car car : cars) {
	    	drawCar(car, transform_x, transform_y, g2d);
	    }	
	}

	private void drawCar(Car car, double transform_x, double transform_y, Graphics2D g2d) {
//		double currentSpeed = car.getCurrentSpeed();
		int defaultCarSize = 2;
		double orientation = car.getOrientation();
		double length = car.getLength();
		Point2D position = car.getPosition();
		g2d.setColor(Color.BLACK);
//		System.out.println(orientation);
		
		// TODO car orientation
		
		g2d.drawRect((int) (position.getX() * transform_x), (int) (position.getY() * transform_y), (int) (defaultCarSize * transform_x), (int) (length * transform_y));	
		
		


	}
	



}
