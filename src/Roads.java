import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import TrafficSim.CrossRoad;
import TrafficSim.Direction;
import TrafficSim.EndPoint;
import TrafficSim.Lane;
import TrafficSim.RoadSegment;
import TrafficSim.TrafficLight;
import TrafficSim.TrafficLightState;

public class Roads implements Drawable {
	
	CrossRoad[] crossRoads;
	RoadSegment[] roadSegments;


	public Roads(RoadSegment[] roadSegments, CrossRoad[] crossroads) {
		this.roadSegments = roadSegments;
		this.crossRoads = crossroads;
	}
	


	private void drawLane(Lane lane, Graphics2D g2d) {
//		g2d.drawLine((int) lane.getStartRoad().getStartPosition().getX(), 
//				(int) lane.getStartRoad().getStartPosition().getY(), 
//				(int) lane.getEndRoad().getEndPosition().getX(), 
//				(int) lane.getEndRoad().getEndPosition().getY());
		
	}



	private void drawRoadSegment(RoadSegment roadSegment, double transform_x, double transform_y, Graphics2D g2d) {
		Point2D end = roadSegment.getEndPosition();
		Point2D start = roadSegment.getStartPosition();
		int lanesCount = roadSegment.getBackwardLanesCount();
		double laneWidth = roadSegment.getLaneWidth();
		double laneSeparatorWidth = roadSegment.getLaneSeparatorWidth();
		int roadWidth = (int)(laneWidth * lanesCount + laneSeparatorWidth * (lanesCount - 1));
		
		boolean horizontal = true;
//		double laneWidth = roadSegment.getLaneWidth();
//		double laneSeparatorWidth = roadSegment.getLaneSeparatorWidth();
		
		if (end.getX() == start.getX()) {
			horizontal = false;			
		}
		
		if (horizontal == true) {
			g2d.drawLine(
					(int) (start.getX() * transform_x), 
					(int) (start.getY() * transform_y), 
					(int) (end.getX() * transform_x), 
					(int) (end.getY() * transform_y)
					);
			
			g2d.drawRect(
					(int) (start.getX() * transform_x), 
					(int) ((start.getY() - roadWidth / 2) * transform_y), 
					(int) (end.getX() * transform_x) - (int) (start.getX() * transform_x), 
					(int) (roadWidth * transform_y)
					);
		} else {
			g2d.drawLine(
					(int) (start.getX() * transform_x), 
					(int) (start.getY() * transform_y), 
					(int) (end.getX() * transform_x), 
					(int) (end.getY() * transform_y)
					);
			g2d.drawRect(
					(int) ((start.getX() - roadWidth / 2) * transform_x), 
					(int) (start.getY() * transform_y), 
					(int) (roadWidth * transform_x),
					(int) (end.getY() * transform_y) - (int) (start.getY() * transform_y)
					);
		}
		
		// ******************* asphalt start *******************
//		g2d.fillRect((int) start.getX(), (int) start.getY(), (int) end.getX() - (int) start.getX(), (int) end.getY() - (int) start.getY());
		// ******************* asphalt end *********************
		
		
	    for(int i = 1 ; i < lanesCount; i++) { // maybe from 1?
	    	drawLane(roadSegment.getLane(i), g2d);
	    }		
	}
	
	private void drawCrossRoad(CrossRoad crossRoad, double transform_x, double transform_y, Graphics2D g2d) {
		EndPoint[] endpoints = crossRoad.getEndPoints();
		RoadSegment[] roads = crossRoad.getRoads();
		int[] endPointPositionsX = new int[roads.length];
		int[] endPointPositionsY = new int[roads.length];
//		Lane[] lanes = crossRoad.getLanes();
		//crossroad
		
//		for (Lane lane : lanes) {
//	    	drawLane(lane, g2d);
//		}

		for (int i = 0; i < roads.length; i++) {			
			endPointPositionsX[i] = (int) (roads[i].getEndPointPosition(endpoints[i]).getX() * transform_x);		
			endPointPositionsY[i] = (int) (roads[i].getEndPointPosition(endpoints[i]).getY() * transform_y);		
		}
		
		g2d.drawPolygon(endPointPositionsX, endPointPositionsY, roads.length);
		
		

		//traffic lights
		
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
	public void draw(double transform_x, double transform_y, Graphics2D g2d) {
	    for(RoadSegment roadSegment : roadSegments) {
	    	drawRoadSegment(roadSegment, transform_x, transform_y, g2d);
	    }	
	    for(CrossRoad crossRoad : crossRoads) {
	    	drawCrossRoad(crossRoad, transform_x, transform_y, g2d);
	    }	
		
	}


}
