import java.awt.Graphics2D;

import TrafficSim.Car;

public interface Drawable {
	
	public void draw(double transform_x, double transform_y, Graphics2D g2d);

}
