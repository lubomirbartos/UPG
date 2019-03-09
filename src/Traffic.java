import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

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
		double roadWidth = (laneWidth * lanesCount + laneSeparatorWidth * (lanesCount ));
		
		boolean horizontal = true;
		
		if (end.getX() == start.getX()) {
			horizontal = false;			
		}
		
		
		if (horizontal == true) {			
			g2d.drawRect(
					View.x(start.getX()),
					View.y((start.getY() - roadWidth / 2)),
					View.x(end.getX() - start.getX()),
					View.y(roadWidth)
					);
		} else {
			g2d.drawRect(
					View.x((start.getX() - roadWidth / 2)),
					View.y(start.getY()),
					View.x(roadWidth),
					View.y(end.getY() - start.getY())
					);
		}
				
		
	    for(int i = 1 ; i < forwardLanesCount; i++) {
			if (horizontal == true) {	
				g2d.drawLine(
						View.x(roadSegment.getEndPosition().getX()), 
						View.y(roadSegment.getEndPosition().getY() + ((laneWidth + laneSeparatorWidth) * i)),
						View.x(roadSegment.getStartPosition().getX()), 
						View.y(roadSegment.getStartPosition().getY() + ((laneWidth + laneSeparatorWidth) * i))
						);
				
			} else {
				g2d.drawLine(
						View.x(roadSegment.getEndPosition().getX() + ((laneWidth + laneSeparatorWidth) * i)), 
						View.y(roadSegment.getEndPosition().getY()),
						View.x(roadSegment.getStartPosition().getX() + ((laneWidth + laneSeparatorWidth) * i)), 
						View.y(roadSegment.getStartPosition().getY())
						);

			}

	    }		
	    for(int i = 1 ; i < backwardLanesCount; i++) {
			if (horizontal == true) {	
				g2d.drawLine(
						View.x(roadSegment.getEndPosition().getX()), 
						View.y(roadSegment.getEndPosition().getY() - ((laneWidth + laneSeparatorWidth) * i)),
						View.x(roadSegment.getStartPosition().getX()), 
						View.y(roadSegment.getStartPosition().getY() - ((laneWidth + laneSeparatorWidth) * i))
						);
				
			} else {
				g2d.drawLine(
						View.x(roadSegment.getEndPosition().getX() - ((laneWidth + laneSeparatorWidth) * i)), 
						View.y(roadSegment.getEndPosition().getY()),
						View.x(roadSegment.getStartPosition().getX() - ((laneWidth + laneSeparatorWidth) * i)), 
						View.y(roadSegment.getStartPosition().getY())
						);

			}

	    }		
	}
	
	private void drawCrossRoad(CrossRoad crossRoad, Graphics2D g2d) {
		RoadSegment[] roads = crossRoad.getRoads();
		int[] endPointPositionsX = new int[8];
		int[] endPointPositionsY = new int[8];

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
			
			switch(i) {
			case 0:
				endPointPositionsX[2*i] = View.x(roads[i].getEndPointPosition(EndPoint.END).getX());		
				endPointPositionsX[2*i + 1] = View.x(roads[i].getEndPointPosition(EndPoint.END).getX());		
				endPointPositionsY[2*i] = View.y(roads[i].getEndPointPosition(EndPoint.END).getY() + roadWidth/2);		
				endPointPositionsY[2*i + 1] = View.y(roads[i].getEndPointPosition(EndPoint.END).getY() - roadWidth/2);						
				break;
			case 1:
				endPointPositionsX[2*i] = View.x(roads[i].getEndPointPosition(EndPoint.END).getX() - roadWidth/2);		
				endPointPositionsX[2*i + 1] = View.x(roads[i].getEndPointPosition(EndPoint.END).getX() + roadWidth/2);		
				endPointPositionsY[2*i] = View.y(roads[i].getEndPointPosition(EndPoint.END).getY());		
				endPointPositionsY[2*i + 1] = View.y(roads[i].getEndPointPosition(EndPoint.END).getY());						
				break;
			case 2:
				endPointPositionsX[2*i] = View.x(roads[i].getEndPointPosition(EndPoint.START).getX());		
				endPointPositionsX[2*i + 1] = View.x(roads[i].getEndPointPosition(EndPoint.START).getX());		
				endPointPositionsY[2*i] = View.y(roads[i].getEndPointPosition(EndPoint.START).getY() - roadWidth/2);		
				endPointPositionsY[2*i + 1] = View.y(roads[i].getEndPointPosition(EndPoint.START).getY() + roadWidth/2);						
				break;
			case 3:
				endPointPositionsX[2*i] = View.x(roads[i].getEndPointPosition(EndPoint.START).getX() + roadWidth/2);		
				endPointPositionsX[2*i + 1] = View.x(roads[i].getEndPointPosition(EndPoint.START).getX() - roadWidth/2);		
				endPointPositionsY[2*i] = View.y(roads[i].getEndPointPosition(EndPoint.START).getY());		
				endPointPositionsY[2*i + 1] = View.y(roads[i].getEndPointPosition(EndPoint.START).getY());						
				break;
			}

		}
		
		g2d.drawPolygon(endPointPositionsX, endPointPositionsY, 8);
		
	    for(Lane lane : crossRoad.getLanes()) { // maybe from 1?
	    	RoadSegment start = lane.getStartRoad();
	    	RoadSegment end = lane.getEndRoad();
	    	double start_lane_width = start.getLaneWidth();
	    	double end_lane_width = start.getLaneWidth();
	    	int lane_number = lane.getEndLaneNumber();
	    	
	    	
	    	
			g2d.drawLine( // TODO unfinished business here
					View.x(start.getEndPosition().getX()), 
					View.y(start.getEndPosition().getY()),
					View.x(end.getEndPosition().getX()), 
					View.y(end.getEndPosition().getY())
					);

	    }	
		

	    
	    
	    
	    
	    
	    
	    
//		boolean horizontal = true;
//		
//		if (end.getX() == start.getX()) {
//			horizontal = false;			
//		}
//		
//		g2d.drawLine(
//				View.x(start.getX()),
//				View.y(start.getY()),
//				View.x(end.getX()),
//				View.y(end.getY())
//				);
//
//		
//		if (horizontal == true) {			
//			g2d.drawRect(
//					View.x(start.getX()),
//					View.y((start.getY() - roadWidth / 2)),
//					View.x(end.getX() - start.getX()),
//					View.y(roadWidth)
//					);
//		} else {
//			g2d.drawRect(
//					View.x((start.getX() - roadWidth / 2)),
//					View.y(start.getY()),
//					View.x(roadWidth),
//					View.y(end.getY() - start.getY())
//					);
//		}

	    
	    
	    
	    
	    
	    
	    
	    
	    
	    

		//traffic lights
//		drawTrafficLights(crossRoad, g2d);
	}
	
	private void drawCar(Car car, Graphics2D g2d) {
//		double currentSpeed = car.getCurrentSpeed();
		double defaultCarSize = 2.0;
		double orientation = car.getOrientation();
		double length = car.getLength();
		Point2D position_front = car.getPosition();
//		double a = 0;
//		double b = 0;
//		double c = 0;
//		double d = 0;
		
		
		
		
		double y = length*Math.sin(orientation);
		double x = length*Math.sin(Math.PI - orientation);
		Point2D position_back = new Point2D.Double(position_front.getX() + x, position_front.getY() + y);
//		System.out.println(y);
//		System.out.println(x);
//		g2d.drawLine(
//				View.x(position_front.getX()), 
//				View.y(position_front.getY()), 
//				View.x(position_back.getX()), 
//				View.y(position_back.getY())
//				);
		
		
		
		
		
		
		
		
		
		
		
		
		
		// TODO dynamic orientation
		
		if (orientation == ORIENTATION_NORTH) {
			g2d.drawRect(
					View.x(position_front.getX() - defaultCarSize/2), 
					View.y(position_front.getY()), 
					View.x(defaultCarSize), 
					View.y(length)
					);	
		}
		else if (orientation == ORIENTATION_SOUTH) {
			g2d.drawRect(
					View.x(position_front.getX() - defaultCarSize/2), 
					View.y(position_front.getY() - length), 
					View.x(defaultCarSize),
					View.y(length) 
					);	

		}
		else if (orientation == ORIENTATION_EAST) {
			g2d.drawRect(
					View.x(position_front.getX() - length), 
					View.y(position_front.getY() - defaultCarSize/2), 
					View.x(length),
					View.y(defaultCarSize) 
					);	

		}
		else if (orientation == ORIENTATION_WEST) {
			g2d.drawRect(
					View.x(position_front.getX()), 
					View.y(position_front.getY() - defaultCarSize/2), 
					View.x(length),
					View.y(defaultCarSize) 
					);	

		}
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
