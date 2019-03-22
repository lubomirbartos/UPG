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

public class Traffic extends JComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6746894399395930340L;
	CrossRoad[] crossRoads;
	RoadSegment[] roadSegments;
	Car[] cars;
	View view;
	
	public static final double ORIENTATION_EAST = 0.0;
	public static final double ORIENTATION_NORTH = -1.5707963267948966;
	public static final double ORIENTATION_WEST = 3.141592653589793;
	public static final double ORIENTATION_SOUTH = 1.5707963267948966;
	


	public Traffic(RoadSegment[] roadSegments, CrossRoad[] crossroads, Car[] cars, View view) {
		this.roadSegments = roadSegments;
		this.crossRoads = crossroads;
		this.cars = cars;		
	    setDoubleBuffered(true);
		this.view = view;	
	}

	private void drawRoadSegment(RoadSegment roadSegment, Graphics2D g2d) {
		Point2D end = roadSegment.getEndPosition();
		Point2D start = roadSegment.getStartPosition();
		int lanesCount = roadSegment.getBackwardLanesCount() + roadSegment.getForwardLanesCount();
		double laneWidth = roadSegment.getLaneWidth();
		double separatorWidth = roadSegment.getLaneSeparatorWidth();
		AffineTransform old_transform = g2d.getTransform();
		
		double roadWidth = laneWidth * lanesCount + separatorWidth;
		double prepona_x = end.getX() - start.getX(); 
		double prepona_y = end.getY() - start.getY(); 
		double road_length = Math.sqrt(prepona_x*prepona_x + prepona_y*prepona_y);
		double rotation = Math.atan((prepona_y) / (prepona_x)); 
		Rectangle road_shape;
		Rectangle separator_shape;
		
		g2d.setColor(Color.WHITE);
		g2d.translate(view.x(start.getX()), view.y(start.getY()));
		g2d.rotate(rotation);		

		// translate to upper left corner of road
		g2d.translate(0, view.scale(laneWidth - separatorWidth/2));

		// drawing road


		road_shape = new Rectangle(
				0,
				- view.scale(roadWidth/2),
				view.scale(road_length),
				view.scale(roadWidth)
			);
		
		separator_shape = new Rectangle(
				0, 
				0,
				view.scale(road_length), 
				view.scale(separatorWidth)
			);
		
		g2d.setColor(Color.GRAY);
		g2d.fill(road_shape);
		g2d.setColor(Color.BLACK);
		g2d.fill(separator_shape);

		g2d.setTransform(old_transform);
	}
	


	private boolean is_road_horizontal(Point2D start, Point2D end) {
		if (start.getY() == end.getY()) {
			return true;			
		}
		return false;
	}



	private void drawCrossRoad(CrossRoad crossRoad, Graphics2D g2d) {		
	    for(Lane lane : crossRoad.getLanes()) { // maybe from 1?
			draw_crossroad_lane(lane, g2d);
	    }		

		//traffic lights
//		drawTrafficLights(crossRoad, g2d);
	}
	
	private void draw_crossroad_lane(Lane lane, Graphics2D g2d) {
		AffineTransform old_transform = g2d.getTransform();

    	Point2D start_position;
    	Point2D end_position;    
       	start_position = get_start_road_position(lane.getStartLaneNumber(), lane.getStartRoad());
    	end_position = get_end_road_position(lane.getEndLaneNumber(), lane.getEndRoad());

		double laneWidth = lane.getStartRoad().getLaneWidth()/2;
		double prepona_x = end_position.getX() - start_position.getX(); 
		double prepona_y = end_position.getY() - start_position.getY(); 
		double lane_length = Math.sqrt(prepona_x*prepona_x + prepona_y*prepona_y);
		double rotation;
		if (prepona_x < 0 && prepona_y < 0) {
			rotation = Math.PI - Math.atan((-prepona_y) / (prepona_x)); 			
		} else if (prepona_x < 0) {
			rotation = Math.PI - Math.atan((-prepona_y) / (prepona_x)); 			
		} else if (prepona_y < 0) {
			rotation = Math.atan((prepona_y) / (prepona_x)); 			
		} else {
			rotation = Math.atan((prepona_y) / (prepona_x)); 			
		}
		
		g2d.translate(view.x(start_position.getX()), view.y(start_position.getY()));
		g2d.rotate(rotation);

		Rectangle lane_rectangle = new Rectangle(
				0,
				view.scale(-laneWidth/2),
				view.scale(lane_length),
				view.scale(laneWidth)
			);
			
		g2d.setColor(Color.GRAY);
		g2d.fill(lane_rectangle);

		g2d.setTransform(old_transform);		
	}

	public void compute_transformations(){
		this.view.set_transform_x(this.getWidth()/(this.view.get_model_width()));
		this.view.set_transform_y(this.getHeight()/(this.view.get_model_height()));		

		if (this.view.get_transform_x() < this.view.get_transform_y()) {
			this.view.set_transform(this.view.get_transform_x());
			this.view.set_offset_y((this.getHeight() - this.view.get_model_height() * this.view.get_transform_x())/2);
		} else {
			this.view.set_transform(this.view.get_transform_y());
			this.view.set_offset_x((this.getWidth() - this.view.get_model_width() * this.view.get_transform_y())/2);
		}
	}

	private Point2D get_end_road_position(int lane_number, RoadSegment road) {    	
		double[] shift;
		Point2D center;

    	if (lane_number < 0) {
    		//position = startposition
    		center = road.getEndPosition();
    	} else {
    		//position = endposition
    		center =  road.getStartPosition();
    	}
    	shift = get_lane_shift(lane_number, road);
    	
    	return new Point2D.Double(center.getX() + shift[0], center.getY() + shift[1]);

	}



	private double[] get_lane_shift(int lane_number, RoadSegment end_road) {
		double lane_width = end_road.getLaneWidth();
		double shift_x = 0;
		double shift_y = 0;
		double additional = 0;
		
    	if (lane_number < 0) {
    		additional = lane_width;
    	} else {
    		additional = 0;
    	}
		
    	if (is_road_horizontal(end_road)) {
    		shift_x += 0;
    		shift_y += (lane_width)*lane_number + lane_width/2 + additional;
    	} else {
    		shift_x += (lane_width)*lane_number - lane_width*1.5 + additional;
    		shift_y += 0;
    	}
    	
//		Point2D start = road.getStartPosition();
//		Point2D end = road.getEndPosition();
//		double lane_width = road.getLaneWidth();
//		double separatorWidth = road.getLaneSeparatorWidth();
//		
//		double prepona_x = end.getX() - start.getX(); 
//		double prepona_y = end.getY() - start.getY(); 
//		double rotation = Math.PI/2 - Math.atan((prepona_y) / (prepona_x));
//
//		if (lane_number < 0) {
//			shift = lane_width*lane_number - separatorWidth;
//		} else {
//			shift = lane_width*lane_number;
//		}
//		
//		shift_x = shift/Math.cos(rotation);
//		shift_y = shift/(Math.sin(rotation));
//		
//		System.out.println(shift_y);

    	double[] result = new double[2];
    	result[0] = shift_x;
    	result[1] = shift_y;
    	return result;
	}

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



	private boolean is_road_horizontal(RoadSegment road) {
		return is_road_horizontal(road.getStartPosition(), road.getEndPosition());
	}



	private void drawCar(Car car, Graphics2D g2d) {
		double defaultCarSize = 2;
		double orientation = car.getOrientation();
		double length = car.getLength();
		Point2D position_front = car.getPosition();
		Shape car_shape = new Rectangle();		
		AffineTransform old_transform = g2d.getTransform();
		
		g2d.translate(view.x(position_front.getX()) - view.scale(length/2), view.y(position_front.getY())); // stred auta na 0,0
		g2d.rotate(orientation);
		g2d.translate(-view.scale(length)/2, view.scale(-defaultCarSize/2)); // zadni levy roh auta na 0,0

		car_shape = new Rectangle(
				0, 
				0, 
				view.scale(length),
				view.scale(defaultCarSize) 
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
	
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		this.compute_transformations();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(view.x(0), view.y(0), view.scale(view.get_model_width()), view.scale(view.get_model_height()));

	    for(RoadSegment roadSegment : roadSegments) {
	    	drawRoadSegment(roadSegment, g2d);
	    }	
	    for(CrossRoad crossRoad : crossRoads) {
	    	drawCrossRoad(crossRoad, g2d);
	    }	
	    for(Car car : cars) {
	    	drawCar(car, g2d);
	    }	
	}

	public void update(RoadSegment[] roadSegments, CrossRoad[] crossroads, Car[] cars) {
		this.roadSegments = roadSegments;
		this.crossRoads = crossroads;
		this.cars = cars;		
	}
}
