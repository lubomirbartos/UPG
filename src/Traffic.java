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

		
		if (end.getY() == start.getY()) {
			horizontal = true;			
		}
		
		
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
	
	private void drawCrossRoad(CrossRoad crossRoad, Graphics2D g2d) {
		RoadSegment[] roads = crossRoad.getRoads();

		for (int i = 0; i < roads.length; i++) {
			Point2D end = roads[i].getEndPosition();
			Point2D start = roads[i].getStartPosition();
			boolean horizontal = true;
			
			if (end.getX() == start.getX()) {
				horizontal = false;			
			}

			
			int backwardLanesCount = roads[i].getBackwardLanesCount();
			int forwardLanesCount = roads[i].getForwardLanesCount();
			int lanesCount = backwardLanesCount + forwardLanesCount;
			double laneWidth = roads[i].getLaneWidth();
			double laneSeparatorWidth = roads[i].getLaneSeparatorWidth();
			double roadWidth = (laneWidth * lanesCount + laneSeparatorWidth * (lanesCount ));

//			if (horizontal) {
//				endPointPositionsX[i] = View.x(roads[i].getEndPointPosition(endpoints[i]).getX());		
//				endPointPositionsX[i] = View.x(roads[i].getEndPointPosition(endpoints[i]).getX());		
//				endPointPositionsY[i] = View.y(roads[i].getEndPointPosition(endpoints[i]).getY() - roadWidth);		
//				endPointPositionsY[i] = View.y(roads[i].getEndPointPosition(endpoints[i]).getY() + roadWidth);						
//			} else {
//				endPointPositionsX[i] = View.x(roads[i].getEndPointPosition(endpoints[i]).getX() - roadWidth);		
//				endPointPositionsX[i] = View.x(roads[i].getEndPointPosition(endpoints[i]).getX() + roadWidth);		
//				endPointPositionsY[i] = View.y(roads[i].getEndPointPosition(endpoints[i]).getY());		
//				endPointPositionsY[i] = View.y(roads[i].getEndPointPosition(endpoints[i]).getY());						
//			}
//			System.out.println(roadWidth);
			
//			switch(i) {
//			case 0:
//				endPointPositionsX[2*i] = View.x(roads[i].getEndPointPosition(EndPoint.END).getX());		
//				endPointPositionsX[2*i + 1] = View.x(roads[i].getEndPointPosition(EndPoint.END).getX());		
//				endPointPositionsY[2*i] = View.y(roads[i].getEndPointPosition(EndPoint.END).getY() + roadWidth/2);		
//				endPointPositionsY[2*i + 1] = View.y(roads[i].getEndPointPosition(EndPoint.END).getY() - roadWidth/2);						
//				break;
//			case 1:
//				endPointPositionsX[2*i] = View.x(roads[i].getEndPointPosition(EndPoint.END).getX() - roadWidth/2);		
//				endPointPositionsX[2*i + 1] = View.x(roads[i].getEndPointPosition(EndPoint.END).getX() + roadWidth/2);		
//				endPointPositionsY[2*i] = View.y(roads[i].getEndPointPosition(EndPoint.END).getY());		
//				endPointPositionsY[2*i + 1] = View.y(roads[i].getEndPointPosition(EndPoint.END).getY());						
//				break;
//			case 2:
//				endPointPositionsX[2*i] = View.x(roads[i].getEndPointPosition(EndPoint.START).getX());		
//				endPointPositionsX[2*i + 1] = View.x(roads[i].getEndPointPosition(EndPoint.START).getX());		
//				endPointPositionsY[2*i] = View.y(roads[i].getEndPointPosition(EndPoint.START).getY() - roadWidth/2);		
//				endPointPositionsY[2*i + 1] = View.y(roads[i].getEndPointPosition(EndPoint.START).getY() + roadWidth/2);						
//				break;
//			case 3:
//				endPointPositionsX[2*i] = View.x(roads[i].getEndPointPosition(EndPoint.START).getX() + roadWidth/2);		
//				endPointPositionsX[2*i + 1] = View.x(roads[i].getEndPointPosition(EndPoint.START).getX() - roadWidth/2);		
//				endPointPositionsY[2*i] = View.y(roads[i].getEndPointPosition(EndPoint.START).getY());		
//				endPointPositionsY[2*i + 1] = View.y(roads[i].getEndPointPosition(EndPoint.START).getY());						
//				break;
//			}

		}
		
//		g2d.drawPolygon(endPointPositionsX, endPointPositionsY, 8);
		
	    for(Lane lane : crossRoad.getLanes()) { // maybe from 1?
	    	RoadSegment start_road = lane.getStartRoad();
	    	RoadSegment end_road = lane.getEndRoad();
	    	double start_lane_width = start_road.getLaneWidth();
	    	double end_lane_width = start_road.getLaneWidth();
	    	int start_lane_number = lane.getStartLaneNumber(); // start position linking to this lane of start road
	    	int end_lane_number = lane.getEndLaneNumber(); // end position linking to this lane of end road
	    	double start_posun_x1;
	    	double start_posun_y1;
	    	double start_posun_x2;
	    	double start_posun_y2;
	    	double end_posun_x1;
	    	double end_posun_y1;
	    	double end_posun_x2;
	    	double end_posun_y2;
	    	Point2D start_position;
	    	Point2D end_position;
	    	
			int[] lanePositionsX = new int[4];
			int[] lanePositionsY = new int[4];

	    	boolean start_road_horizontal = false;
	    	boolean end_road_horizontal = false;
	    	
	    	if (start_road.getEndPosition().getX() == start_road.getStartPosition().getX()) {
	    		start_road_horizontal = true;
	    	}

	    	if (end_road.getEndPosition().getX() == end_road.getStartPosition().getX()) {
	    		end_road_horizontal = true;
	    	}
	    	
	    	if (start_road_horizontal) {
	    		start_posun_x1 = -start_road.getLaneWidth()/2 + (start_road.getLaneWidth() + start_road.getLaneSeparatorWidth())*start_lane_number;
		    	start_posun_y1 = 0;
		    	start_posun_x2 = start_posun_x1 + start_lane_width;
		    	start_posun_y2 = 0;
	    	} else {
		    	start_posun_x1 = 0;
		    	start_posun_y1 = -start_road.getLaneWidth()/2 + (start_road.getLaneWidth() + start_road.getLaneSeparatorWidth())*start_lane_number;
		    	start_posun_x2 = 0;
		    	start_posun_y2 = start_posun_y1 + start_lane_width;
	    	}
	    	
	    	if (end_road_horizontal) {
	    		end_posun_x1 = -end_road.getLaneWidth()/2 + (end_road.getLaneWidth() + end_road.getLaneSeparatorWidth())*end_lane_number;
	    		end_posun_y1 = 0;
	    		end_posun_x2 = end_posun_x1 + end_lane_width;
	    		end_posun_y2 = 0;
	    	} else {
		    	end_posun_x1 = 0;
		    	end_posun_y1 = -end_road.getLaneWidth()/2 + (end_road.getLaneWidth() + end_road.getLaneSeparatorWidth())*end_lane_number;
		    	end_posun_x2 = 0;
		    	end_posun_y2 = end_posun_y1 + end_lane_width;
	    	}

	    	
	    	if (start_lane_number > 0) {
	    		//position = startposition
	    		start_position = start_road.getEndPosition();
	    	} else {
	    		//position = endposition
	    		start_position = start_road.getStartPosition();
	    	}

	    	lanePositionsX[1] = View.x(start_position.getX() + start_posun_x1);
    		lanePositionsX[0] = View.x(start_position.getX() + start_posun_x2);
    		lanePositionsY[1] = View.y(start_position.getY() + start_posun_y1);
    		lanePositionsY[0] = View.y(start_position.getY() + start_posun_y2);

	    	if (end_lane_number > 0) {
	    		//position = startposition
	    		end_position = end_road.getStartPosition();
	    	} else {
	    		//position = endposition
	    		end_position = end_road.getEndPosition();
	    	}

    		lanePositionsX[3] = View.x(end_position.getX() + end_posun_x1);
    		lanePositionsX[2] = View.x(end_position.getX() + end_posun_x2);
    		lanePositionsY[2] = View.y(end_position.getY() + end_posun_y1);
    		lanePositionsY[3] = View.y(end_position.getY() + end_posun_y2);
    		
    		Polygon polygon_line = new Polygon(lanePositionsX, lanePositionsY, 4);
    		
    		g2d.drawPolygon(polygon_line);
//    		g2d.fillPolygon(polygon_line);

	    	
	    	
	    }	
		

		//traffic lights
//		drawTrafficLights(crossRoad, g2d);
	}
	
	private void drawCar(Car car, Graphics2D g2d) {
		double defaultCarSize = 2.0;
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
