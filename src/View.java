import TrafficSim.Car;
import TrafficSim.RoadSegment;
import TrafficSim.Simulator;

public class View {
	
/*
 * Here are stored scaling and offset information and methods for x and y transformations.
 */
	private double model_width;
	private double model_height;	
	private double window_width;
	private double window_height;
	private double scale_x; // timhle budeme nasobit souradnice modelu ze simulatoru
	private double scale_y;
	private double scale;	
	private double offset_x;
	private double offset_y;
	private double negative_offset_x;
	private double negative_offset_y;
	
	
	/*
	 * Constructor for view.
	 * 
	 * @param double model_width  model width
	 * @param double model_height model height
	 */
	public View(double model_width, double model_height, double minimum_x, double minimum_y) {
		this.model_width = model_width;
		this.model_height = model_height;
		this.negative_offset_x = minimum_x;
		this.negative_offset_y = minimum_y;
	}
	
	/*
	 * Constructor for view.
	 * 
	 * @param Simulator simulator simulator
	 */
	public View(Simulator simulator) {
		this.scale_x = 1;
		this.scale_y = 1;
		this.scale = 1;
		this.window_width = 640;
		this.window_height = 640;
		this.offset_x = 0;
		this.offset_y = 0;
		model_width = find_max_x(simulator);
		model_height = find_max_y(simulator);
		negative_offset_x = find_min_x(simulator);
		negative_offset_y = find_min_y(simulator);			

	}
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * */
	/* * * * * * * * TRANSFORMATION METHODS  * * * * * * * */
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * */	

	/*
	 * Computes window x coordinate from model coordinate.
	 * 
	 * @param double x model x coordinate
	 */
	public int x(double x) {
		return (int) (x * this.get_scale() + this.get_offset_x() - negative_offset_x * this.get_scale()/2);
	}
	
	/*
	 * Computes window y coordinate from model coordinate.
	 * 
	 * @param double y model y coordinate
	 */
	public int y(double y) {
		return (int) (y * this.get_scale() + this.get_offset_y() - negative_offset_y * this.get_scale()/2);
	}

	/*
	 * Computes window y coordinate from model coordinate.
	 * 
	 * @param double y model y coordinate
	 */
	public int panel_x(double x) {
		return (int) (x * this.get_scale() + this.get_offset_x());
	}

	/*
	 * Computes window y coordinate from model coordinate.
	 * 
	 * @param double y model y coordinate
	 */
	public int panel_y(double y) {
		return (int) (y * this.get_scale() + this.get_offset_y());
	}

	/*
	 * Computes window y coordinate from model coordinate.
	 * 
	 * @param double y model y coordinate
	 */
	public int scale(double number) {
		return (int) (number * this.get_scale());
	}		
	
	/*
	 * Computes minimum and maximum values of given model and returns new view.
	 * 
	 * @param double model_width  model width
	 * @param double model_height model height
	 */
	public void update_view(Simulator simulator) {
		double max_x = find_max_x(simulator);
		double max_y = find_max_y(simulator);
		double min_x = find_min_x(simulator);
		double min_y = find_min_y(simulator);
		if (Math.abs(model_width - max_x) > 10) {
			model_width = max_x;
		}

		if (Math.abs(model_height - max_y) > 10) {
			model_height = max_y;
		}

		if (Math.abs(negative_offset_x - min_x) > 10) {
			negative_offset_x = min_x;
			model_width += negative_offset_x;
		}

		if (Math.abs(negative_offset_y - min_y) > 10) {
			negative_offset_y = min_y;
			model_height += negative_offset_y;
		}
	}
	
	

	private double find_min_y(Simulator simulator) {
		double minimum_y = 0;
		double y;
		y = find_min_car_y(simulator.getCars());
		if (y < minimum_y) {
			minimum_y = y;
		}
		
		y = find_min_road_y(simulator.getRoadSegments());
		if (y < minimum_y) {
			minimum_y = y;
		}

		return minimum_y;
	}

	private double find_min_x(Simulator simulator) {
		double minimum_x = 0;
		double x;
		x = find_min_car_x(simulator.getCars());
		if (x < minimum_x) {
			minimum_x = x;
		}
		
		x = find_min_road_x(simulator.getRoadSegments());
		if (x < minimum_x) {
			minimum_x = x;
		}
		
		return minimum_x;
	}

	private double find_max_y(Simulator simulator) {
		double maximum_y = 0;
		double y;
		y = find_max_car_y(simulator.getCars());
		if (y > maximum_y) {
			maximum_y = y;
		}
		
		y = find_max_road_y(simulator.getRoadSegments());
		if (y > maximum_y) {
			maximum_y = y;
		}
		
		return maximum_y;
	}

	private double find_max_x(Simulator simulator) {
		double maximum_x = 0;
		double x;
		x = find_max_car_x(simulator.getCars());
		if (x > maximum_x) {
			maximum_x = x;
		}
		
		x = find_max_road_x(simulator.getRoadSegments());
		if (x > maximum_x) {
			maximum_x = x;
		}
		
		return maximum_x;
	}

	/*
	 * Computes scaling and offset transformations from model width and model height for later displaying of objects on canvas.
	 */
	public void compute_transformations(double width, double height){
		if (this.get_model_width() == 0 || this.get_model_height() == 0) {
			return;
		}
		this.set_scale_x(width/(this.get_model_width()));
		this.set_scale_y(height/(this.get_model_height()));		

		if (this.get_scale_x() < this.get_scale_y()) {
			this.set_scale(this.get_scale_x());
			this.set_offset_y((height - this.get_model_height() * this.get_scale())/2);
		} else {
			this.set_scale(this.get_scale_y());
			this.set_offset_x((width - this.get_model_width() * this.get_scale())/2);
		}
	}	
	
	/*
	 * Iterates over road segments and stores maximum road y.
	 * 
	 * @param RoadSegment[] road_segments array of road segments
	 * 
	 * @return                            maximum model y
	 */
	public static double find_max_road_y(RoadSegment[] road_segments) {
		double max_y = 0;
		for (RoadSegment road : road_segments) {
			if (road.getStartPosition().getY() > max_y) {
				max_y = road.getStartPosition().getY() + road.getLaneWidth() * road.getBackwardLanesCount();				
			}
			if (road.getEndPosition().getY() > max_y) {
				max_y = road.getEndPosition().getY() + road.getLaneWidth() * road.getBackwardLanesCount();				
			}
		}
		return max_y;
	}


	/*
	 * Iterates over road segments and stores maximum road x.
	 * 
	 * @param RoadSegment[] road_segments array of road segments
	 * 
	 * @return                            maximum model x
	 */
	public static double find_max_road_x(RoadSegment[] road_segments) {
		double max_x = 0;
		for (RoadSegment road : road_segments) {
			if (road.getStartPosition().getX() > max_x) {
				max_x = road.getStartPosition().getX() + road.getLaneWidth() * road.getBackwardLanesCount();				
			}
			if (road.getEndPosition().getX() > max_x) {
				max_x = road.getEndPosition().getX() + road.getLaneWidth() * road.getBackwardLanesCount();				
			}
		}
		return max_x;
	}
	
	/*
	 * Iterates over road segments and stores minimum road x.
	 * 
	 * @param RoadSegment[] road_segments array of road segments
	 * 
	 * @return                            minimum model x
	 */
	public static double find_min_road_x(RoadSegment[] road_segments) {
		double min_x = 1000;
		for (RoadSegment road : road_segments) {
			if (road.getStartPosition().getX() < min_x) {
				min_x = road.getStartPosition().getX() - road.getLaneWidth() * road.getBackwardLanesCount();				
			}
			if (road.getEndPosition().getX() < min_x) {
				min_x = road.getEndPosition().getX() - road.getLaneWidth() * road.getBackwardLanesCount();				
			}
		}

		return min_x;
	}

	/*
	 * Iterates over road segments and stores minimum road y.
	 * 
	 * @param RoadSegment[] road_segments array of road segments
	 * 
	 * @return                            minimum model y
	 */
	public static double find_min_road_y(RoadSegment[] road_segments) {
		double min_y = 1000;
		for (RoadSegment road : road_segments) {
			if (road.getStartPosition().getY() < min_y) {
				min_y = road.getStartPosition().getY() - road.getLaneWidth() * road.getBackwardLanesCount();				
			}
			if (road.getEndPosition().getY() < min_y) {
				min_y = road.getEndPosition().getY() - road.getLaneWidth() * road.getBackwardLanesCount();				
			}
		}

		return min_y;
	}

	/*
	 * Iterates over cars and stores maximum car x.
	 * 
	 * @param Car[] cars array of cars
	 * 
	 * @return           maximum model x
	 */
	public static double find_max_car_x(Car[] cars) {
		double max_x = 0;
		for (Car car : cars) {
			if (car.getPosition().getX() > max_x) {
				max_x = car.getPosition().getX() + car.getLength();				
			}
		}
		
		return max_x;
	}	
	
	/*
	 * Iterates over cars and stores maximum car y.
	 * 
	 * @param Car[] cars array of cars
	 * 
	 * @return           maximum model y
	 */
	public static double find_max_car_y(Car[] cars) {
		double max_y = 0;
		for (Car car : cars) {
			if (car.getPosition().getY() > max_y) {
				max_y = car.getPosition().getY() + car.getLength();				
			}			
		}
		return max_y;
	}

	/*
	 * Iterates over cars and stores minimum car x.
	 * 
	 * @param Car[] cars array of cars
	 * 
	 * @return           minimum model x
	 */
	public static double find_min_car_x(Car[] cars) {
		double min_x = 1000;
		for (Car car : cars) {
			if (car.getPosition().getX() < min_x) {
				min_x = car.getPosition().getX() - car.getLength();				
			}
		}

		return min_x;
	}	
	
	/*
	 * Iterates over cars and stores minimum car y.
	 * 
	 * @param Car[] cars array of cars
	 * 
	 * @return           maximum model y
	 */
	public static double find_min_car_y(Car[] cars) {
		double min_y = 1000;
		for (Car car : cars) {
			if (car.getPosition().getY() < min_y) {
				min_y = car.getPosition().getY() - car.getLength();				
			}
		}

		return min_y;
	}	

	/* * * * * * * * * * * * * * * * * * * */
	/* * * * * * * * GETTERS * * * * * * * */
	/* * * * * * * * * * * * * * * * * * * */

	/*
	 * Gets model height.
	 * 
	 * @return model height
	 */
	public double get_model_height() {
		return model_height;
	}
	
	/*
	 * Gets model width.
	 * 
	 * @return model width
	 */
	public double get_model_width() {
		return model_width;
	}
	
	/*
	 * Gets window width.
	 * 
	 * @return window width
	 */
	public double get_window_width() {
		return window_width;
	}	
	
	/*
	 * Gets window height.
	 * 
	 * @return window height
	 */
	public double get_window_height() {
		return window_height;
	}	
	
	/*
	 * Gets x scaling.
	 * 
	 * @return scale x
	 */
	public double get_scale_x() {
		return scale_x;
	}	
	
	/*
	 * Gets x offset.
	 * 
	 * @return x offset
	 */
	public double get_offset_x() {
		return this.offset_x;
	}
	
	/*
	 * Gets y offset.
	 * 
	 * @return y offset
	 */
	public double get_offset_y() {
		return this.offset_y;
	}
	
	/*
	 * Gets scaling.
	 * 
	 * @return scaling
	 */
	public double get_scale_y() {
		return scale_y;
	}
	
	/*
	 * Gets scaling.
	 * 
	 * @return scaling
	 */
	public double get_scale() {
		return scale;
	}

	
	/* * * * * * * * * * * * * * * * * * * */
	/* * * * * * * * SETTERS * * * * * * * */
	/* * * * * * * * * * * * * * * * * * * */
	
	public void set_scale(double scale) {
		this.scale = scale;
	}
	
	public void set_scale_y(double scale_y) {
		this.scale_y = scale_y;
	}
	
	public void set_offset_y(double offset_y) {
		this.offset_y = offset_y;		
	}	
	
	public void set_offset_x(double offset_x) {
		this.offset_x = offset_x;
	}
	
	public void set_scale_x(double scale_x) {
		this.scale_x = scale_x;
	}
	
	public void set_window_height(double window_height) {
		this.window_height = window_height;
	}
	
	public void set_window_width(double window_width) {
		this.window_width = window_width;
	}



}
