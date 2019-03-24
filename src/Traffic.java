import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

import TrafficSim.Car;
import TrafficSim.CrossRoad;
import TrafficSim.Lane;
import TrafficSim.RoadSegment;
import TrafficSim.Simulator;

public class Traffic extends JComponent {
	
	/**
	 * Random generated serial version id.
	 */
	private static final long serialVersionUID = 6746894399395930340L;
	CrossRoad[] crossroads;
	RoadSegment[] road_segments;
	Car[] cars;
	
	/**
	 * @param Storage for coordinates transformations.
	 */
	View view;
	
	/*
	 * Constructor.
	 * 
	 * @param RoadSegment[] roadSegments array of road segments
	 * @param CrossRoad[]   crossroads   array of crossroads
	 * @param Car[]         cars         array of cars
	 * @param View          view         view with scaling and offsets
	 */
	public Traffic(RoadSegment[] road_segments, CrossRoad[] crossroads, Car[] cars, View view) {
		this.road_segments = road_segments;
		this.crossroads = crossroads;
		this.cars = cars;		
		this.view = view;	
		setDoubleBuffered(true);
	}



	/*
	 * Draws road segment with lane separator.
	 * 
	 * @param RoadSegment roadSegment road to draw
	 * @param Graphics2D g2d          graphic context
	 */
	private void draw_road_segment(RoadSegment road, Graphics2D g2d) {
		AffineTransform old_transform = g2d.getTransform();
		Rectangle road_shape;
		Rectangle separator_shape;
		
		Point2D end = road.getEndPosition();
		Point2D start = road.getStartPosition();
		
		int lanes_count = road.getBackwardLanesCount() + road.getForwardLanesCount();
		double lane_width = road.getLaneWidth();
		double separator_width = road.getLaneSeparatorWidth();		
		double road_width = lane_width * lanes_count + separator_width;
		
		double prepona_x = end.getX() - start.getX(); 
		double prepona_y = end.getY() - start.getY(); 
		double road_length = Math.sqrt(prepona_x*prepona_x + prepona_y*prepona_y);
		double rotation = Math.atan((prepona_y) / (prepona_x)); 
		
		// translate to road's start position and rotate
		g2d.translate(view.x(start.getX()), view.y(start.getY()));
		g2d.rotate(rotation);
		// translate to upper left corner of road
		g2d.translate(0, view.scale(lane_width - separator_width/2));

		road_shape = new Rectangle(
				0,
				- view.scale(road_width/2),
				view.scale(road_length),
				view.scale(road_width)
			);
		
		separator_shape = new Rectangle(
				0, 
				-view.scale(separator_width/2),
				view.scale(road_length), 
				view.scale(separator_width)
			);
		
		g2d.setColor(Color.GRAY);
		g2d.fill(road_shape);
		g2d.setColor(Color.BLACK);
		g2d.fill(separator_shape);

		g2d.setTransform(old_transform);
		
	    for(int i = 1; i <= road.getForwardLanesCount(); i++) { // maybe from 1?
			draw_crossroad_lane(road.getLane(i), g2d);
	    }
	    
	    for(int i = 1; i <= road.getBackwardLanesCount(); i++) { // maybe from 1?
			draw_crossroad_lane(road.getLane(-i), g2d);
	    }		

	}
	
	/*
	 * Draws all crossroad components: lanes.
	 * 
	 * @param CrossRoad crossRoad crossroad
	 * @param Graphics2D g2d      graphics context
	 */
	private void draw_cross_road(CrossRoad crossroad, Graphics2D g2d) {		
	    for(Lane lane : crossroad.getLanes()) { // maybe from 1?
			draw_crossroad_lane(lane, g2d);
	    }		

		//traffic lights
//		drawTrafficLights(crossRoad, g2d);
	}
	
	/*
	 * Draws crossroad lane.
	 * 
	 * @param Lane lane      lane to draw
	 * @param Graphics2D g2d graphic context
	 */
	private void draw_crossroad_lane(Lane lane, Graphics2D g2d) {
		AffineTransform old_transform = g2d.getTransform();
		Rectangle lane_rectangle;

    	Point2D start_position;
    	Point2D end_position;    
    	double lane_width;
    	double odvesna_x;
    	double odvesna_y;
    	double lane_length;
    	double rotation;
    	
       	start_position = get_start_road_position(lane.getStartLaneNumber(), lane.getStartRoad());
    	end_position = get_end_road_position(lane.getEndLaneNumber(), lane.getEndRoad());

		lane_width = lane.getStartRoad().getLaneWidth()/2;
		odvesna_x = end_position.getX() - start_position.getX(); 
		odvesna_y = end_position.getY() - start_position.getY(); 

		lane_length = Math.sqrt(odvesna_x*odvesna_x + odvesna_y*odvesna_y);
		
		if (odvesna_x < 0 && odvesna_y < 0) {
			rotation = Math.PI - Math.atan((odvesna_y) / (odvesna_x));
		} else if (odvesna_x < 0) {
			rotation = Math.PI + Math.atan((odvesna_y) / (-odvesna_x)); 
		} else if (odvesna_y < 0) {
			rotation = Math.atan((-odvesna_y) / (odvesna_x)); 
		} else {
			rotation = 2*Math.PI - Math.atan((odvesna_y) / (odvesna_x));
		}

		g2d.translate(view.x(start_position.getX()), view.y(start_position.getY()));
		g2d.rotate(-rotation);

		lane_rectangle = new Rectangle(
				0,
				0,
				view.scale(lane_length),
				view.scale(lane_width)
			);
		g2d.setColor(Color.GRAY);
		g2d.fill(lane_rectangle);
		g2d.setTransform(old_transform);		
	}


	/*
	 * Obtains position of end of crossroad lane.
	 * 
	 * @param int lane_number  number of lane
	 * @param RoadSegment road road from which we calculate shifts
	 * 
	 * @return                 Point2D position of line end
	 */
	private Point2D get_end_road_position(int lane_number, RoadSegment road) {    	
		double[] shift;
		Point2D center;

    	if (lane_number < 0) {
    		center = road.getEndPosition();
    	} else {
    		center =  road.getStartPosition();
    	}
    	shift = get_lane_shift(lane_number, road);
    	
    	return new Point2D.Double(center.getX() + shift[0], center.getY() + shift[1]);
	}
	
	/*
	 * Obtains position of start of crossroad lane.
	 * 
	 * @param int lane_number  number of lane
	 * @param RoadSegment road road from which we calculate shifts
	 * 
	 * @return                 Point2D position of line start
	 */
	private Point2D get_start_road_position(int lane_number, RoadSegment road) {
		double[] shift;
		Point2D center;		

    	if (lane_number > 0) {
    		center = road.getEndPosition();
    	} else {
    		center =  road.getStartPosition();
    	}
    	shift = get_lane_shift(lane_number, road);

    	return new Point2D.Double(center.getX() + shift[0], center.getY() + shift[1]);
	}

	/*
	 * Calculates x and y shifts from road end/start position of crossroad lane.
	 * 
	 * @param int lane_number  number of lane
	 * @param RoadSegment road road from which we calculate shifts
	 * 
	 * @return                 double[2]: [0] = x shift, [1] = y shift
	 */
	private double[] get_lane_shift(int lane_number, RoadSegment road) {
    	double[] result = new double[2];
		double lane_width = road.getLaneWidth();
		double shift = 0;
		double shift_x = 0;
		double shift_y = 0;
    	
		Point2D start = road.getStartPosition();
		Point2D end = road.getEndPosition();
		double separator_width = road.getLaneSeparatorWidth();

		if (lane_number < 0) {
			shift = lane_width*(lane_number) + separator_width/2 - lane_width/2;
		} else {
			shift = lane_width*(lane_number) + separator_width/2 - 3*lane_width/2;
		}

		double odvesna_x = end.getX() - start.getX(); 
		double odvesna_y = end.getY() - start.getY(); 
		
		double rotation;
		if (odvesna_x < 0 && odvesna_y < 0) {
			rotation = Math.PI - Math.atan((odvesna_y) / (odvesna_x));
			shift_x = shift*Math.cos(-Math.PI - Math.PI/2 - rotation);	
			shift_y = shift*Math.sin(-Math.PI - Math.PI/2 - rotation);
		} else if (odvesna_x < 0) {
			rotation = Math.PI + Math.atan((odvesna_y) / (-odvesna_x)); 	
			shift_x = shift*Math.cos(2*Math.PI - Math.PI/2 + rotation);	
			shift_y = shift*Math.sin(2*Math.PI - Math.PI/2 + rotation);
		} else if (odvesna_y < 0) {
			rotation = Math.atan((-odvesna_y) / (odvesna_x)); 	
			shift_x = shift*Math.cos(Math.PI/2 + rotation);	
			shift_y = shift*Math.sin(Math.PI/2 + rotation);	
		} else {
			rotation = 2*Math.PI - Math.atan((odvesna_y) / (odvesna_x)); 	
			shift_x = shift*Math.cos(-Math.PI/2 - rotation);	
			shift_y = shift*Math.sin(-Math.PI/2 - rotation);
		}
				
		if (shift_x > -0.01 && shift_x < 0.01) {
			shift_x = 0;
		}
		if (shift_y > -0.01 && shift_y < 0.01) {
			shift_y = 0;
		}

    	result[0] = shift_x;
    	result[1] = shift_y;
    	return result;
	}

	/*
	 * Transforms to car's location and draws car.
	 * 
	 * @param Car car        car
	 * @param Graphics2D g2d graphics context
	 * 
	 * @return                 double[2]: [0] = x shift, [1] = y shift
	 */
	private void draw_car(Car car, Graphics2D g2d) {
		double default_car_size = 1.5;
		double orientation = car.getOrientation();
		double length = car.getLength();
		Point2D position_front = car.getPosition();
		Shape car_shape = new Rectangle();		
		AffineTransform old_transform = g2d.getTransform();
		
		g2d.translate(view.x(position_front.getX()) - view.scale(length/2), view.y(position_front.getY())); // stred auta na 0,0
		g2d.rotate(orientation);
		g2d.translate(-view.scale(length)/2, view.scale(-default_car_size/2)); // zadni levy roh auta na 0,0

		car_shape = new Rectangle(
				0, 
				0, 
				view.scale(length),
				view.scale(default_car_size) 
		);	
		
		g2d.setColor(Color.YELLOW);
		g2d.fill(car_shape);
		g2d.setTransform(old_transform);		
	}

//	private void drawTrafficLights(CrossRoad crossRoad, Graphics2D g2d) {
//		for (Direction direction : Direction.values()) {
//			TrafficLight[] trafficLights = crossRoad.getTrafficLights(direction);
//			for (TrafficLight trafficLight : trafficLights) {
//				if (trafficLight != null) {
//					Color currentColor = Color.RED;
//					currentColor = getColorFromTrafficLightState(trafficLight.getCurrentState());					
//					Color rememberColor = g2d.getColor();
//					g2d.setColor(currentColor);
//					switch (direction) {
//						case Entry:
//							g2d.drawString("↑", 200, 200 - 10);
//							break;
//						case Right:
//							g2d.drawString("→", 200 + 10, 200);
//							break;
//						case Opposite:
//							g2d.drawString("↓", 200, 200);
//							break;
//						case Left:
//							g2d.drawString("←", 200 - 15, 200);
//							break;
//						default:
//							break;
//					}
//					g2d.setColor(rememberColor);	
//				}
//			}
//		}		
//	}



//	private static Color getColorFromTrafficLightState(TrafficLightState state) {
//		switch (state) {
//			case OFF :
//				return Color.GRAY;					
//			case GO :
//				return Color.GREEN;						
//			case PREPARE_TO_GO :
//				return Color.ORANGE;						
//			case PREPARE_TO_STOP :
//				return Color.ORANGE;						
//			case STOP :
//				return Color.RED;						
//			default:
//				return Color.CYAN; // something went wrong
//		}		
//	}
	
	/*
	 * Paints all the traffic.
	 * 
	 * @param Graphics g graphics context
	 */
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		this.view.compute_transformations(this.getWidth(), this.getHeight());
		g2d.setColor(Color.BLACK);
		g2d.fillRect(view.panel_x(0), view.panel_y(0), view.scale(view.get_model_width()), view.scale(view.get_model_height()));

	    for(RoadSegment roadSegment : road_segments) {
	    	draw_road_segment(roadSegment, g2d);
	    }	
	    for(CrossRoad crossRoad : crossroads) {
	    	draw_cross_road(crossRoad, g2d);
	    }	
	    for(Car car : cars) {
	    	draw_car(car, g2d);
	    }	
	}

	/*
	 * Updates all components of traffic.
	 * 
	 * @param RoadSegment[] road_segments array of road segments
	 * @param CrossRoad[] crossroads      array of crossroads
	 * @param Car[] cars                  array of road segments
	 */
	public void update(Simulator simulator) {
		this.road_segments = simulator.getRoadSegments();
		this.crossroads = simulator.getCrossroads();
		this.cars = simulator.getCars();
		this.view.update_view(simulator);
	}
}
