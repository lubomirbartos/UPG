import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import TrafficSim.Car;
import TrafficSim.CrossRoad;
import TrafficSim.Lane;
import TrafficSim.RoadSegment;

public class Model extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Roads roads;
	public Cars cars;
	
	public double transform_x = 1;
	public double transform_y = 1;
	
	public Model(Roads roads, Cars cars, double transform_x, double transform_y) {
		this.roads = roads;
		this.cars = cars;		
		this.transform_x = transform_x;
		this.transform_y = transform_y;		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g;

	    roads.draw(transform_x, transform_y, g2d);
	    cars.draw(transform_x, transform_y, g2d);
	}

	public void setTransformX(double transform_x) {
		this.transform_x = transform_x;
		
	}

	public void setTransformY(double transform_y) {
		this.transform_y = transform_y;	
	}
	
	public void update(Roads roads, Cars cars, double transform_x, double transform_y){
		this.roads = roads;
		this.cars = cars;		
		this.transform_x = transform_x;
		this.transform_y = transform_y;		

	}
	
	
	
}
