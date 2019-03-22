import TrafficSim.Car;
import TrafficSim.RoadSegment;

public class View {
	
	private final double model_width;
	private final double model_height;	
	private double window_width;
	private double window_height;
	private double transform_x; // timhle budeme nasobit souradnice modelu ze simulatoru
	private double transform_y;
	private double transform;	
	private double offset_x;
	private double offset_y;
	
	
	public int x(double x) {
		return (int) (x * this.get_transform() + this.get_offset_x());
	}
	public int y(double y) {
		return (int) (y * this.get_transform() + this.get_offset_y());
	}	
	public int scale(double number) {
		return (int) (number * this.get_transform());
	}	
	
	public View(double model_width, double model_height) {
		this.model_width = model_width;
		this.model_height = model_height;
		this.transform_x = 1;
		this.transform_y = 1;
		this.transform = 1;
		this.window_width = 640;
		this.window_height = 640;
		this.offset_x = 0;
		this.offset_y = 0;
	}
	
	
	public static double get_max_road_y(RoadSegment[] roadSegments) {
		double max_y = 0;
		for (RoadSegment road : roadSegments) {
			if (road.getStartPosition().getY() > max_y) {
				max_y = road.getStartPosition().getY() + road.getLaneWidth() * road.getBackwardLanesCount();				
			}
			if (road.getEndPosition().getY() > max_y) {
				max_y = road.getEndPosition().getY() + road.getLaneWidth() * road.getBackwardLanesCount();				
			}
		}
		return max_y;
	}

	public static double get_max_car_y(Car[] cars) {
		double max_y = 0;
		for (Car car : cars) {
			if (car.getPosition().getY() > max_y) {
				max_y = car.getPosition().getY() + car.getLength();				
			}			
		}
		return max_y;
	}

	public static double get_max_road_x(RoadSegment[] roadSegments) {
		double max_x = 0;
		for (RoadSegment road : roadSegments) {
			if (road.getStartPosition().getX() > max_x) {
				max_x = road.getStartPosition().getX() + road.getLaneWidth() * road.getBackwardLanesCount();				
			}
			if (road.getEndPosition().getX() > max_x) {
				max_x = road.getEndPosition().getX() + road.getLaneWidth() * road.getBackwardLanesCount();				
			}
		}
		return max_x;
	}

	public static double get_max_car_x(Car[] cars) {
		double max_x = 0;
		for (Car car : cars) {
			if (car.getPosition().getX() > max_x) {
				max_x = car.getPosition().getX() + car.getLength();				
			}
		}
		return max_x;
	}	

	
	public double get_model_height() {
		return model_height;
	}
	
	public double get_model_width() {
		return model_width;
	}
	
	public double get_window_width() {
		return window_width;
	}
	
	public void set_window_width(double window_width) {
		this.window_width = window_width;
	}
	
	public double get_window_height() {
		return window_height;
	}
	
	public void set_window_height(double window_height) {
		this.window_height = window_height;
	}
	
	public double get_transform_x() {
		return transform_x;
	}
	
	public void set_transform_x(double transform_x) {
		this.transform_x = transform_x;
	}
	
	public double get_offset_x() {
		return this.offset_x;
	}
	
	public double get_offset_y() {
		return this.offset_y;
	}
	
	public double get_transform() {
		return transform;
	}
	
	public void set_transform(double transform) {
		this.transform = transform;
	}

	public double get_transform_y() {
		return transform_y;
	}
	
	public void set_transform_y(double transform_y) {
		this.transform_y = transform_y;
	}
	
	public void set_offset_y(double offset_y) {
		this.offset_y = offset_y;
		
	}	
	
	public void set_offset_x(double offset_x) {
		this.offset_x = offset_x;
	}	
}
