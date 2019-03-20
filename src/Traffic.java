import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JPanel;

import TrafficSim.Car;
import TrafficSim.CrossRoad;
import TrafficSim.Direction;
import TrafficSim.EndPoint;
import TrafficSim.Lane;
import TrafficSim.RoadSegment;
import TrafficSim.TrafficLight;
import TrafficSim.TrafficLightState;

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
		compute_transformations();
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
		
		g2d.setColor(Color.WHITE);
		g2d.translate(view.x(start.getX()), view.y(start.getY()));
		g2d.rotate(rotation);

		
		// drawing separator
		

		// translate to upper left corner of road
//		g2d.translate(0, view.scale(separatorWidth + laneWidth - roadWidth)/2);

		// drawing road


		Rectangle road_shape = new Rectangle(
			0,
			view.scale(separatorWidth + laneWidth - roadWidth)/2,
			view.scale(road_length),
			view.scale(roadWidth)
		);

		g2d.setColor(Color.WHITE);
		g2d.draw(road_shape);
		Line2D line = new Line2D.Double(
				0, 
				view.scale(laneWidth/2),
				view.scale(road_length), 
				view.scale(laneWidth/2)
		);
		g2d.draw(line);
		line = new Line2D.Double(
				0, 
				view.scale(laneWidth/2 + separatorWidth),
				view.scale(road_length), 
				view.scale(laneWidth/2 + separatorWidth)
		);
		g2d.draw(line);

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
			draw_lane(lane, g2d);
	    }		

		//traffic lights
//		drawTrafficLights(crossRoad, g2d);
	}
	
	private void draw_lane(Lane lane, Graphics2D g2d) {
		AffineTransform old_transform = g2d.getTransform();
		

    	Point2D start_position;
    	Point2D end_position;    
       	start_position = get_start_road_position(lane.getStartLaneNumber(), lane.getStartRoad());
    	end_position = get_end_road_position(lane.getEndLaneNumber(), lane.getEndRoad());
    	Point2D[] start_posun = get_start_point_shifts(lane.getStartRoad(), lane.getStartLaneNumber());
    	Point2D[] end_posun = get_end_point_shifts(lane.getEndRoad(), lane.getEndLaneNumber());

		double laneWidth = lane.getEndRoad().getLaneWidth();
		double prepona_x = end_position.getX() - start_position.getX(); 
		double prepona_y = end_position.getY() - start_position.getY(); 
		double lane_length = Math.sqrt(prepona_x*prepona_x + prepona_y*prepona_y);
		double rotation = Math.atan((prepona_y) / (prepona_x)); 
		
//		if (lane.getStartLaneNumber() > 0) {
			g2d.setColor(Color.WHITE);
			g2d.translate(view.x(start_position.getX()), view.y(start_position.getY()));
			g2d.rotate(rotation);

			Rectangle lane_rectangle = new Rectangle(
					0,
					view.scale(-laneWidth/2),
					view.scale(lane_length),
					view.scale(laneWidth)
				);
			
			Line2D testlane = new Line2D.Double(0,0,view.scale(lane_length), 0);


			g2d.draw(testlane);
//			g2d.draw(lane_rectangle);
			
			g2d.setTransform(old_transform);

//		}

		
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

	private int[][] get_crossroad_lane_coordinates(Lane lane) {
		int[][] result = new int[2][4];
		int[] lanePositionsX = new int[4];
		int[] lanePositionsY = new int[4];
    	Point2D start_position;
    	Point2D end_position;    	
		Point2D[] start_posun;
		Point2D[] end_posun;		
		
    	start_posun = get_start_point_shifts(lane.getStartRoad(), lane.getStartLaneNumber());
    	end_posun = get_end_point_shifts(lane.getEndRoad(), lane.getEndLaneNumber());
    	
       	start_position = get_start_road_position(lane.getStartLaneNumber(), lane.getStartRoad());
    	end_position = get_end_road_position(lane.getEndLaneNumber(), lane.getEndRoad());    	
    	
    	double ax = start_position.getX() + start_posun[0].getX();
    	double bx = start_position.getX() + start_posun[1].getX();
    	double cx = end_position.getX() + end_posun[0].getX();
    	double dx = end_position.getX() + end_posun[1].getX();
    	
    	double ay = start_position.getY() + start_posun[0].getY();
    	double by = start_position.getY() + start_posun[1].getY();
    	double cy = end_position.getY() + end_posun[0].getY();
    	double dy = end_position.getY() + end_posun[1].getY();
    	
    	lanePositionsX[0] = view.x(ax);
		lanePositionsX[1] = view.x(bx);
		lanePositionsX[2] = view.x(cx);
		lanePositionsX[3] = view.x(dx);

		lanePositionsY[0] = view.y(ay);
		lanePositionsY[1] = view.y(by);
		lanePositionsY[2] = view.y(cy);
		lanePositionsY[3] = view.y(dy);
		
		result[0] = lanePositionsX;
		result[1] = lanePositionsY;
		
		return result;
	}



	private Point2D[] get_end_point_shifts(RoadSegment road, int lane_number) {
		double lane_width = road.getLaneWidth();
		Point2D[] shifts = new Point2D[2];
		double shift = -lane_width/2 - (lane_width)*lane_number;

    	if (is_road_horizontal(road)) {
    		shifts[0] = new Point2D.Double(0, shift);
    		shifts[1] = new Point2D.Double(0, shifts[0].getX() + lane_width);
    	} else {
    		shifts[0] = new Point2D.Double(shift, 0);
    		shifts[1] = new Point2D.Double(shifts[0].getY() + lane_width, 0);
    	}
		return shifts;
	}


	private Point2D[] get_start_point_shifts(RoadSegment road, int lane_number) {
		double lane_width = road.getLaneWidth();
		Point2D[] shifts = new Point2D[2];
		double shift = -lane_width/2 - (lane_width + road.getLaneSeparatorWidth())*lane_number; // maybe different for each
		
    	if (is_road_horizontal(road)) {
    		shifts[0] = new Point2D.Double(0, shift);
    		shifts[1] = new Point2D.Double(0, shifts[0].getX() + lane_width);
    	} else {
    		shifts[0] = new Point2D.Double(shift, 0);
    		shifts[1] = new Point2D.Double(shifts[0].getY() + lane_width, 0);
    	}
		return shifts;
	}



	private Point2D get_end_road_position(int end_lane_number, RoadSegment end_road) {
    	if (end_lane_number > 0) {
    		//position = startposition
    		return end_road.getStartPosition();
    	} else {
    		//position = endposition
    		return end_road.getEndPosition();
    	}
	}



	private Point2D get_start_road_position(int start_lane_number, RoadSegment start_road) {
		double lane_width = start_road.getLaneWidth();
		double shift_x = -lane_width/2 - (lane_width + start_road.getLaneSeparatorWidth())*start_lane_number; // maybe different for each
		double shift_y = -lane_width/2 - (lane_width + start_road.getLaneSeparatorWidth())*start_lane_number; // maybe different for each
		Point2D center;

		
    	if (is_road_horizontal(start_road)) {
    		shift_x = 0;
    		shift_y = -lane_width/2 - (lane_width + start_road.getLaneSeparatorWidth())*start_lane_number;
    	} else {
    		shift_x = 0;
    		shift_y = -lane_width/2 - (lane_width + start_road.getLaneSeparatorWidth())*start_lane_number;
    	}

    	if (start_lane_number > 0) {
    		//position = startposition
    		center = start_road.getEndPosition();
    	} else {
    		//position = endposition
    		center =  start_road.getStartPosition();
    	}
    	return new Point2D.Double(center.getX() + shift_x, center.getY() + shift_y);
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
	
	private void drawTrafficLights(CrossRoad crossRoad, Graphics2D g2d) {
		for (Direction direction : Direction.values()) {
			TrafficLight[] trafficLights = crossRoad.getTrafficLights(direction);
			for (TrafficLight trafficLight : trafficLights) {
				if (trafficLight != null) {
					Color currentColor = Color.RED;
					currentColor = getColorFromTrafficLightState(trafficLight.getCurrentState());					
					Color rememberColor = g2d.getColor();
					g2d.setColor(currentColor);
					switch (direction) {
						case Entry:
							g2d.drawString("↑", 200, 200 - 10);
							break;
						case Right:
							g2d.drawString("→", 200 + 10, 200);
							break;
						case Opposite:
							g2d.drawString("↓", 200, 200);
							break;
						case Left:
							g2d.drawString("←", 200 - 15, 200);
							break;
						default:
							break;
					}
					g2d.setColor(rememberColor);	
				}
			}
		}		
	}



	private static Color getColorFromTrafficLightState(TrafficLightState state) {
		switch (state) {
			case OFF :
				return Color.GRAY;					
			case GO :
				return Color.GREEN;						
			case PREPARE_TO_GO :
				return Color.ORANGE;						
			case PREPARE_TO_STOP :
				return Color.ORANGE;						
			case STOP :
				return Color.RED;						
			default:
				return Color.CYAN; // something went wrong
		}		
	}
	
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
