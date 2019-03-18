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



	public Traffic(RoadSegment[] roadSegments, CrossRoad[] crossroads, Car[] cars) {
		this.roadSegments = roadSegments;
		this.crossRoads = crossroads;
		this.cars = cars;		
	    setDoubleBuffered(true);
	}
	


	private void drawLane(Lane lane, Graphics2D g2d) {
		RoadSegment end_road = lane.getEndRoad();
		RoadSegment start_road = lane.getStartRoad();

//		g2d.drawLine(
//				View.x(end_road.getEndPosition().getX()), 
//				View.y(end_road.getEndPosition().getY()),
//				View.x(start_road.getStartPosition().getX()), 
//				View.y(start_road.getStartPosition().getY())
//				);
		

		
	}



	private void drawRoadSegment(RoadSegment roadSegment, Graphics2D g2d) {
		Point2D end = roadSegment.getEndPosition();
		Point2D start = roadSegment.getStartPosition();
		int backwardLanesCount = roadSegment.getBackwardLanesCount();
		int forwardLanesCount = roadSegment.getForwardLanesCount();
		int lanesCount = backwardLanesCount + forwardLanesCount;
		double laneWidth = roadSegment.getLaneWidth();
		double laneSeparatorWidth = roadSegment.getLaneSeparatorWidth();
		double roadWidth = (laneWidth * lanesCount + laneSeparatorWidth * (lanesCount  + 1));

		double first_lane_end_x = end.getX();
		double first_lane_end_y = end.getY();
		double first_lane_start_x = start.getX();
		double first_lane_start_y = start.getY();
		AffineTransform old_transform = g2d.getTransform();

		
		boolean horizontal = false;
		Shape road_shape;
		Line2D line;

		
		horizontal = is_road_horizontal(start, end);
		
		
		if (horizontal == true) {	
			road_shape = new Rectangle(
					View.x(first_lane_start_x),
					View.y((start.getY() - roadWidth / 2 + laneWidth / 2 + laneSeparatorWidth/2)),
					View.x(end.getX() - start.getX()),
					View.y(roadWidth)
					);
		} else {
			road_shape = new Rectangle(
					View.x((start.getX() - roadWidth - laneWidth / 2 - laneSeparatorWidth/2)),
					View.y(start.getY()),
					View.x(roadWidth),
					View.y(end.getY() - start.getY())
					);
		}
//		g2d.draw(road_shape);
		g2d.setColor(Color.GRAY);
		g2d.fill(road_shape);
		g2d.setColor(Color.BLACK);

		
	    for(int i = 1 ; i < forwardLanesCount; i++) {
			if (horizontal == true) {
				line = new Line2D.Double(
						View.x(first_lane_end_x), 
						View.y(first_lane_end_y + ((laneWidth + laneSeparatorWidth)/2 +(laneWidth + laneSeparatorWidth) * i)),
						View.x(first_lane_start_x), 
						View.y(first_lane_start_y + ((laneWidth + laneSeparatorWidth)/2 +(laneWidth + laneSeparatorWidth) * i))
				);
				g2d.draw(line);
				g2d.translate(0, View.y(laneSeparatorWidth));
				g2d.draw(line);
				g2d.setTransform(old_transform);
				
			} else {
				line = new Line2D.Double(
						View.x(first_lane_end_x + ((laneWidth + laneSeparatorWidth)/2 +(laneWidth + laneSeparatorWidth) * i)), 
						View.y(first_lane_end_y),
						View.x(first_lane_start_x + ((laneWidth + laneSeparatorWidth)/2 +(laneWidth + laneSeparatorWidth) * i)), 
						View.y(first_lane_start_y)
				);
				g2d.draw(line);
				g2d.translate(View.x(laneSeparatorWidth), 0);
				g2d.draw(line);
				g2d.setTransform(old_transform);

			}

	    }		
	    for(int i = 0 ; i < backwardLanesCount; i++) {

			if (horizontal == true) {
				line = new Line2D.Double(
						View.x(first_lane_end_x), 
						View.y(first_lane_end_y - ((laneWidth + laneSeparatorWidth)/2 + (laneWidth + laneSeparatorWidth) * i)),
						View.x(first_lane_start_x), 
						View.y(first_lane_start_y - ((laneWidth + laneSeparatorWidth)/2 + (laneWidth + laneSeparatorWidth) * i))
				);
				g2d.draw(line);
				g2d.translate(0, View.y(laneSeparatorWidth));
				g2d.draw(line);
				g2d.setTransform(old_transform);
				
			} else {
				line = new Line2D.Double(
						View.x(first_lane_end_x - ((laneWidth + laneSeparatorWidth)/2 + (laneWidth + laneSeparatorWidth) * i)), 
						View.y(first_lane_end_y),
						View.x(first_lane_start_x - ((laneWidth + laneSeparatorWidth)/2 + (laneWidth + laneSeparatorWidth) * i)), 
						View.y(first_lane_start_y)
				);
				g2d.draw(line);
				g2d.translate(View.x(laneSeparatorWidth), 0);
				g2d.draw(line);
				g2d.setTransform(old_transform);

			}

	    }		
	}
	
	private boolean is_road_horizontal(Point2D start, Point2D end) {
		if (start.getY() == end.getY()) {
			return true;			
		}
		return false;
	}



	private void drawCrossRoad(CrossRoad crossRoad, Graphics2D g2d) {		
	    for(Lane lane : crossRoad.getLanes()) { // maybe from 1?
	    	Polygon lane_polygon = get_crossroad_lane_polygon(lane);
			g2d.drawPolygon(lane_polygon);
	    }	
		

		//traffic lights
//		drawTrafficLights(crossRoad, g2d);
	}
	
	private Polygon get_crossroad_lane_polygon(Lane lane) {
		
		int[][] coordinates = get_crossroad_lane_coordinates(lane); // returns x coordinates at [0] and y at [1]
		return new Polygon(coordinates[0], coordinates[1], 4);		

		
		
	}



	private int[][] get_crossroad_lane_coordinates(Lane lane) {
		int[][] result = new int[2][4];
    	Point2D start_position;
    	Point2D end_position;    	
		Point2D[] start_posun;
		Point2D[] end_posun;
		

    	
		int[] lanePositionsX = new int[4];
		int[] lanePositionsY = new int[4];

        	
    	
    	start_posun = get_start_point_shifts(lane.getStartRoad(), lane.getStartLaneNumber());
    	end_posun = get_end_point_shifts(lane.getEndRoad(), lane.getEndLaneNumber());
    	

    	
    	
    	// get end of road position
    	// get center of line position
    	// get two desired points on one road - x and y
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
    	
//    	result[0] = new Point2D.Double(ax, ay);
//    	result[1] = new Point2D.Double(bx, by);
//    	result[2] = new Point2D.Double(cx, cy);
//    	result[3] = new Point2D.Double(dx, dy);

    	lanePositionsX[0] = View.x(ax);
		lanePositionsX[1] = View.x(bx);
		lanePositionsX[2] = View.x(cx);
		lanePositionsX[3] = View.x(dx);

		lanePositionsY[0] = View.y(ay);
		lanePositionsY[1] = View.y(by);
		lanePositionsY[2] = View.y(cy);
		lanePositionsY[3] = View.y(dy);
		
		result[0] = lanePositionsX;
		result[1] = lanePositionsY;
		
		return result;
	}



	private Point2D[] get_end_point_shifts(RoadSegment road, int lane_number) {
		double lane_width = road.getLaneWidth();
		double separator_width = road.getLaneSeparatorWidth();
		Point2D[] shifts = new Point2D[2];

    	if (is_road_horizontal(road)) {
    		shifts[0] = new Point2D.Double(0, -lane_width/2 - (lane_width + separator_width)*lane_number);
    		shifts[1] = new Point2D.Double(0, shifts[0].getX() + lane_width);
    	} else {
    		shifts[0] = new Point2D.Double(-lane_width/2 - (lane_width + separator_width)*lane_number, 0);
    		shifts[1] = new Point2D.Double(shifts[0].getY() + lane_width, 0);
    	}
		return shifts;
	}



	private Point2D[] get_start_point_shifts(RoadSegment road, int lane_number) {
		double lane_width = road.getLaneWidth();
		double separator_width = road.getLaneSeparatorWidth();
		Point2D[] shifts = new Point2D[2];
		
    	if (is_road_horizontal(road)) {
    		shifts[0] = new Point2D.Double(0, -lane_width/2 - (lane_width + separator_width)*lane_number);
    		shifts[1] = new Point2D.Double(0, shifts[0].getX() + lane_width);
    	} else {
    		shifts[0] = new Point2D.Double(-lane_width/2 - (lane_width + separator_width)*lane_number, 0);
    		shifts[1] = new Point2D.Double(0, shifts[0].getY() + lane_width);
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
    	if (start_lane_number > 0) {
    		//position = startposition
    		return start_road.getEndPosition();
    	} else {
    		//position = endposition
    		return start_road.getStartPosition();
    	}
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
		
		g2d.translate(View.x(position_front.getX() - length/2), View.y(position_front.getY())); // stred auta na 0,0
		g2d.rotate(orientation);
		g2d.translate(View.x(-length/2), View.y(- defaultCarSize/2)); // zadni levy roh auta na 0,0

		car_shape = new Rectangle(
				View.x(0), 
				View.y(0), 
				View.x(length),
				View.y(defaultCarSize) 
		);	
		
		g2d.setColor(Color.BLUE);
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
