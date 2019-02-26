import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;

import TrafficSim.Car;
import TrafficSim.CrossRoad;
import TrafficSim.RoadSegment;
import TrafficSim.Simulator;

public class Main extends Frame {
	
	private static int    minimumInMeters;
	private static int    maximumInMeters;
	private static float  scaleX;
	private static float  scaleY;
	private static int    moveX;
	private static int    moveY;
	private static int    height = 500;
	private static int    width = 500;
	private static Model  model;

	public static void main(String[] args) {

		//check arguments
		// create Simulator
		new Main();
		return;
//		Simulator simulator = createSimulator();
		//start loop 100 ms
//		while(true) {
//			paint(simulator);			
//		}
		//end   loop

	}

	private Main() {
		super("Crossroad");
//		computeModelDimensions(model);
		 computeModel2WindowTransformation(width, height); //height and width from window?

		setSize(width, height);
		setVisible(true);
		addWindowListener(new WindowAdapter()
			{public void windowClosing(WindowEvent e)
			     {dispose(); System.exit(0);}
			}
		);

	}

	private static void paint(Simulator sim) {
		 Point2D windowCoordinates = model2window(model);
//		 drawTrafficState(sim, g);
		
	}
	
	public void paint(Graphics2D g) {
	     g.setColor(Color.red);
	     g.drawRect(50,50,200,200);
	     g.setColor(Color.blue);
	     g.drawRect(75,75,300,200);
    }


	// Computes minimum and maximum values of given model.
	// Stores model to variable
	private void computeModelDimensions(Model model /*or simulator?*/) {
		
		this.minimumInMeters = minimumInMeters;
		this.maximumInMeters = maximumInMeters;
	}

	private static void computeModel2WindowTransformation(int width, int height) {
		
	}

	private static Point2D model2window(Model model) {
		return new Point2D.Float();
	}
	
	private static void drawCar(Point2D position, double orientation, int lenght, int width, Graphics2D g) {
		
	}
	
	private static void drawLane(Point2D start, Point2D end, int size, Graphics2D g) {
		
	}
	
	private static void drawRoadSegment(RoadSegment road, Graphics2D g) {
		
	}
	
	private static void drawCrossRoad(CrossRoad cr, Graphics2D g) {
		
	}

//	private static void drawCars(Cars cars, Graphics2D g) {
//		
//		Cars car;
//		for(Car car : cars){
//			drawCar(position, orientation, lenght, width, g);
//			
//		}
//	}
//
//	private static void drawTrafficState(Simulator sim, Graphics2D g) {
//		drawCars(cars);
//		drawLane(start, end, size, g);
//		drawRoadSegment(road, g);
//		drawCrossRoad(cr, g);
//		
//	}
}
